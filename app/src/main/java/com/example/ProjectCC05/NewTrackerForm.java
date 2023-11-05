package com.example.ProjectCC05;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTrackerForm extends AppCompatActivity {

    Button btn_ok, btn_cancel;
    EditText et_expensename, et_amount, /*et_picturenumber, */et_date;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tracker_form);

        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        et_amount = findViewById(R.id.et_amount);
        et_expensename = findViewById(R.id.et_expensename);
        // et_picturenumber = findViewById(R.id.et_picturenumber);
        et_date = findViewById(R.id.et_date);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewTrackerForm.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Log.d(TAG, "onDateSet: mm/dd/yyyy: ");

                String date = month + "/" + day + "/" + year;
                et_date.setText(date);
            }
        };



        // listen for incoming data

        Bundle incomingIntent = getIntent().getExtras();

        if (incomingIntent != null) {
            String expenseName = incomingIntent.getString("name");
            int amount = incomingIntent.getInt("age");
            // int pictureNumber = incomingIntent.getInt("picturenumber");
            String date = incomingIntent.getString("date");
            positionToEdit = incomingIntent.getInt("edit");

            // fill in the form
            et_expensename.setText(expenseName);
            et_date.setText(date);
            et_amount.setText(Integer.toString( amount));
            // et_picturenumber.setText(Integer.toString(pictureNumber));



        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get strings from et_ view objects
                String newName = et_expensename.getText().toString();
                String newAge = et_amount.getText().toString();
                // String newPictureNumber = et_picturenumber.getText().toString();
                String newDate = et_date.getText().toString();


                // put strings into a message for MainActivity
                Intent i = new Intent(view.getContext(), MainPage.class);

                i.putExtra("edit", positionToEdit);
                i.putExtra("name", newName);
                i.putExtra("age", newAge);
                i.putExtra("date", newDate);
                // i.putExtra("picturenumber", newPictureNumber);

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