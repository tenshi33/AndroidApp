package com.example.ProjectCC05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ProjectCC05.R;


public class MainActivityLoginPage extends AppCompatActivity {

    private  Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loginpage);

        button = (Button) findViewById(R.id.loginbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInPage();

            }
        });

    }
    public void openSignInPage(){
        Intent intent = new Intent(this, Sign_In_Page.class);
        startActivity(intent);
    }
}
