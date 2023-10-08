package com.example.ProjectCC05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewTrackerForm extends AppCompatActivity {

    Button btn_ok, btn_cancel;
    EditText et_expensename, et_amount, et_picturenumber;

    int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tracker_form);

        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        et_amount = findViewById(R.id.et_amount);
        et_expensename = findViewById(R.id.et_expensename);
        et_picturenumber = findViewById(R.id.et_picturenumber);

        // listen for incoming data

        Bundle incomingIntent = getIntent().getExtras();

        if (incomingIntent != null) {
            String expenseName = incomingIntent.getString("name");
            int amount = incomingIntent.getInt("age");
            int pictureNumber = incomingIntent.getInt("picturenumber");
            positionToEdit = incomingIntent.getInt("edit");

            // fill in the form
            et_expensename.setText(expenseName);
            et_amount.setText(Integer.toString( amount));
            et_picturenumber.setText(Integer.toString(pictureNumber));



        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get strings from et_ view objects
                String newName = et_expensename.getText().toString();
                String newAge = et_amount.getText().toString();
                String newPictureNumber = et_picturenumber.getText().toString();


                // put strings into a message for MainActivity
                Intent i = new Intent(view.getContext(), MainPage.class);

                i.putExtra("edit", positionToEdit);
                i.putExtra("name", newName);
                i.putExtra("age", newAge);
                i.putExtra("picturenumber", newPictureNumber);

                // start main Activity again
                startActivity(i);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}