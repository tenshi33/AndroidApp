package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    private Button loginbtn;
    private EditText et_username, et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextView clickableTextView = findViewById(R.id.signup);

        clickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                String savedUsername = sharedPreferences.getString("username", "");
                String savedPassword = sharedPreferences.getString("password", "");

                if (username.equals(savedUsername) && password.equals(savedPassword)) {
                    Toast.makeText(LoginPage.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginPage.this, MainPage.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginPage.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /*
    public void loginUser () {

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        loginbtn = findViewById(R.id.loginbtn);



        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (username.equals(savedUsername) && password.equals(savedPassword)) {
                    Toast.makeText(LoginPage.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (LoginPage.this, MainPage.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginPage.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    */


    public void openActivity2() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        openMainActivity();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivityLoginPage.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}
