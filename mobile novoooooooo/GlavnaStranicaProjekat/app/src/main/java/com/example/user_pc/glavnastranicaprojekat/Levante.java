package com.example.user_pc.glavnastranicaprojekat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Levante extends AppCompatActivity implements View.OnClickListener{
    private EditText mEditText;
    private Button mButton;
    private LinearLayout scroll;

  //  float curRate;
    int count;
    float broj;
    float currentRating =0;
    float zavrsni =0;

    private ImageView profileImageView;
    private Button pickImage;

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private boolean hasImageChanged = false;
    DbHelper dbHelper;

    Bitmap thumbnail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levante);

        MediaController mediaController;

        VideoView video =  findViewById(R.id.videoView);

        String videopath = "android.resources://com.example.user_pc.trecdio/"+ R.raw.video  ;
        Uri uri = Uri.parse(videopath);
        video.setVideoURI(uri);
        mediaController = new MediaController(Levante.this);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
        video.start();

        RatingBar ratingBar = findViewById(R.id.ratingBar);

        final TextView countText =  findViewById(R.id.textView);
        SharedPreferences sharedPreferences =Levante.this.getSharedPreferences("com.example.user_pc.glavnastranicaprojekat", MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();

        broj = sharedPreferences.getFloat("rating",  broj);
        count = sharedPreferences.getInt("brojac",  count);
        zavrsni = sharedPreferences.getFloat("zavrsni", zavrsni);
        countText.setText(broj + " Ratings");


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                currentRating = ratingBar.getRating();
                Toast.makeText(Levante.this, "Rating changed, current rating " + currentRating, Toast.LENGTH_SHORT).show();

                count++;
                zavrsni = zavrsni+currentRating;
                broj = zavrsni/count;
                countText.setText(broj + " Ratings");


                editor.putInt("brojac", count).apply();
                editor.putFloat("zavrsni", zavrsni).apply();
                editor.putFloat("rating" ,  broj).apply();

            }
        });


        scroll =  findViewById(R.id.scroll);
        mEditText =  findViewById(R.id.editText);
        mButton =  findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.addView(createNewTextView(mEditText.getText().toString()));


            }
        });
        TextView textView = new TextView(this);
        textView.setText("New text");




        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        pickImage = (Button) findViewById(R.id.pick_image);

        pickImage.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(Levante.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            profileImageView.setEnabled(false);
            ActivityCompat.requestPermissions(Levante.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {
            profileImageView.setEnabled(true);
        }

        dbHelper = new DbHelper(this);

    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText("New Comment: " + text);
        return textView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {

            case R.id.pick_image:
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0:
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAPTURE_PHOTO);
                                        break;
                                    case 2:
                                        profileImageView.setImageResource(R.drawable.ic_account_circle_black);
                                        break;
                                }
                            }
                        })
                        .show();
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                profileImageView.setEnabled(true);
            }
        }
    }

    public void setProgressBar(){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < 100){
                    progressBarStatus += 30;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }

            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //set Progress Bar
                    setProgressBar();
                    //set profile picture form gallery
                    profileImageView.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sort:
                Intent intent = new Intent(this, DetailsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");

        //set Progress Bar
        setProgressBar();
        //set profile picture form camera
        profileImageView.setMaxWidth(200);
        profileImageView.setImageBitmap(thumbnail);

    }

    public void addToDb(View view){

        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
        Bitmap bitmap = profileImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        dbHelper.addToDb(data);
        Toast.makeText(this, "Image saved to DB successfully", Toast.LENGTH_SHORT).show();

    }



}
