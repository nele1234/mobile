package com.example.user_pc.glavnastranicaprojekat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;


public class RegActivity extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button btnreg, btnlog;
    private EditText txtUsername, txtpass, txtemail ;
    private AwesomeValidation awesomeValidation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        openHelper = new DatabaseHelper(this);

        txtUsername =  findViewById(R.id.txtUsername);
        txtpass =  findViewById(R.id.txtpass);
        txtemail =  findViewById(R.id.txtemail);

        btnreg =  findViewById(R.id.btnreg);


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

                db = openHelper.getWritableDatabase();

                String fname = txtUsername.getText().toString();
                String pass = txtpass.getText().toString();
                String email = txtemail.getText().toString();

                awesomeValidation.addValidation(RegActivity.this, R.id.txtUsername, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
                awesomeValidation.addValidation(RegActivity.this, R.id.txtemail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
                awesomeValidation.addValidation(RegActivity.this, R.id.txtpass, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passworderror);

                if (awesomeValidation.validate()) {
                    insertdata(fname,  pass, email);
                    Toast.makeText(RegActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });

        Button btnlog = findViewById(R.id.btnlog);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public void insertdata(String fname, String pass, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, fname);
        contentValues.put(DatabaseHelper.COL_3, pass);
        contentValues.put(DatabaseHelper.COL_4, email);
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

}