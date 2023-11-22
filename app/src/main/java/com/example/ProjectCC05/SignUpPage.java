package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SignUpPage extends AppCompatActivity {

    // SignUp page. The user inputs their credentials, and then it will be used to log in into the app.
    private EditText et_usernameRegister, et_passwordRegister;
    private Button btn_register;
    private TextView clickableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        color();
        initializers();
        onClickListeners();

    }
    private void onClickListeners() {
        clickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInPage();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_usernameRegister.getText().toString();
                String password = et_passwordRegister.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(SignUpPage.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent (SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void initializers() {
        et_usernameRegister = findViewById(R.id.et_usernameRegister);
        et_passwordRegister = findViewById(R.id.et_passwordRegister);
        btn_register = findViewById(R.id.btn_register);
        clickableTextView = findViewById(R.id.signin);
    }

    private void color() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        }
    }

    public void openSignInPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}
