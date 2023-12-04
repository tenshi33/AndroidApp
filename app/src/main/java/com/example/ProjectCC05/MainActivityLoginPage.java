package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivityLoginPage extends AppCompatActivity {
    private ImageButton forwardbtn;

    /*
        This activity is the starting activity of the Application.
        It only has 2 buttons as the UI and the background.
        These buttons redirect you to either LoginPage or the SignUpPage.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loginpage);

        color();
        initializers();
        onClickListeners();

    }

    private void initializers() {
        forwardbtn = findViewById(R.id.forwardbtn);

    }

    private void onClickListeners() {

        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInPage();
            }
        });
    }

    private void color() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void openSignInPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}