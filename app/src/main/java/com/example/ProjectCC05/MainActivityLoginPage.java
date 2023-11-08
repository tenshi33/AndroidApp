package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityLoginPage extends AppCompatActivity {
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loginpage);

        registerButton = findViewById(R.id.btn_register);
        loginButton = findViewById(R.id.loginbtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInPage();
            }
        });
    }

    public void openSignInPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}
