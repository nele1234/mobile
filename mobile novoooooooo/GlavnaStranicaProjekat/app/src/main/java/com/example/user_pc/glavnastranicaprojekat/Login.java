package com.example.user_pc.glavnastranicaprojekat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Button btnlog, btnreg;
    EditText txtusername, txtpasslog;
    Cursor cursor;
    private CheckBox checkBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openHelper = new DatabaseHelper(this);
        db= openHelper.getReadableDatabase();
        btnlog = findViewById(R.id.btnlog);
        btnreg = findViewById(R.id.btnreg);
        txtpasslog = findViewById(R.id.txtpasslog);
        txtusername = findViewById(R.id.txtusername);

        checkBox = findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtusername.setText(loginPreferences.getString("username", ""));
            txtpasslog.setText(loginPreferences.getString("password", ""));
            checkBox.setChecked(true);
        }

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= txtusername.getText().toString();
                String pass= txtpasslog.getText().toString();
                cursor=db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_2+ " =? AND "+ DatabaseHelper.COL_3+ " =?", new String[] {username, pass});
                if (cursor!=null)
                {
                    if (cursor.getCount()>0 )
                    {
                        cursor.moveToNext();
                        Toast.makeText(Login.this, "Log in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Login.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                }

                if (checkBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", pass);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegActivity.class);
                startActivity(intent);
            }
        });


    }


}
