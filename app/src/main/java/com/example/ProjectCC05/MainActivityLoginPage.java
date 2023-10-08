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

        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("1234")) {
                    Toast.makeText(MainActivityLoginPage.this, "LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
                    openActivity2();
                }else
                    Toast.makeText(MainActivityLoginPage.this, "LOGIN FAILED",Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void openActivity2(){
        Intent intent = new Intent(this, MainActivityMainPage.class);
        startActivity(intent);
    }
}

//Ang POGI ko

/*
    Toledo Test Commit
 */