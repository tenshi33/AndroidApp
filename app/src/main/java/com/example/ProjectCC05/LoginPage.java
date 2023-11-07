package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    private Button button;

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

        button = findViewById(R.id.loginbtn);
        final TextView username = findViewById(R.id.username);
        final TextView password = findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("admin") && enteredPassword.equals("1234")) {
                    Toast.makeText(LoginPage.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    openActivity2();
                } else {
                    Toast.makeText(LoginPage.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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
