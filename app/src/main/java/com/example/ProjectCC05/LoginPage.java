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

        /*
            If the credentials the user inputted is the same as the one they input in the register,
            then they are redirected to the MainPage.class. Else, it shows a message saying Login Failed.
         */
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
