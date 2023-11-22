package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTrackerForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*
        This is class is where we add new Items in the lv_listOfExpenses in the MainPage.class.
        In here, we put the values on the et_expensename, et_amount, and et_date, as well as whether
        the cbox_setReminder is checked or not. After clicking the btn_ok, it transfers the data to the
        MainPage using the i.putExtra of the Intent.
     */
    Button btn_ok;
    ImageButton btn_cancel;
    EditText et_expensename, et_amount, et_date;
    CheckBox cbox_setReminder;
    Spinner spin_sign;

    int positionToEdit = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tracker_form);

        color();
        initializers();
        onClickListeners();
        incomingIntent();
    }

    private void incomingIntent() {
        // Listen for incoming data. This will fill out the form if the item was tapped to edit.
        Bundle incomingIntent = getIntent().getExtras();

        if (incomingIntent != null) {
            String expenseName = incomingIntent.getString("name");
            float amount = incomingIntent.getFloat("amount", 0.0f);

            if (amount < 0) {
                spin_sign.setSelection(1);
            } else {
                spin_sign.setSelection(0);
            }

            String date = incomingIntent.getString("date");
            positionToEdit = incomingIntent.getInt("edit");
            boolean setReminder = incomingIntent.getBoolean("setReminder", false);
            // fill in the form
            et_expensename.setText(expenseName);
            et_date.setText(date);
            et_amount.setText(String.valueOf(Math.abs(amount)));
            cbox_setReminder.setChecked(setReminder);
        }
    }

    private void onClickListeners() {
        // If date field is clicked, a calendar will pop-up using the DatePickerDialogFragment.class
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        // This will send the values of the fields into the MainPage's listview.
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get strings from et_ view objects
                    String newName = et_expensename.getText().toString();
                    String newAmount = et_amount.getText().toString();
                    String newDate = et_date.getText().toString();
                    Float newAmountFloat = Float.parseFloat(newAmount);
                    boolean setReminder = cbox_setReminder.isChecked();
                    // If date is not in correct format, it will show an error.
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                        Date parsedDate = sdf.parse(newDate);
                    } catch (ParseException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewTrackerForm.this);
                        builder.setCancelable(false);
                        builder.setTitle("Date Format Error!");
                        builder.setMessage("Please enter the date in the format MMMM dd, yyyy");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        return;
                    }
                    // put strings into a message for MainActivity
                    Intent i = new Intent(view.getContext(), MainPage.class);
                    //i.putExtra("edit", positionToEdit);
                    i.putExtra("name", newName);
                    i.putExtra("amount", addAmount());
                    i.putExtra("date", newDate);
                    i.putExtra("setReminder", setReminder);

                    if (positionToEdit > -1) {
                        i.putExtra("edit", positionToEdit);
                    }

                    // start main Activity again
                    startActivity(i);
                } catch (NumberFormatException e) {
                    // To show an error when the user inputted string or character on the amount field
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTrackerForm.this);
                    builder.setCancelable(false);
                    builder.setTitle("Input Error!");
                    builder.setMessage("Please enter a valid number for the amount field.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });
        // Cancels the NewTrackerForm and goes back to the MainPage
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initializers() {
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        et_amount = findViewById(R.id.et_amount);
        et_expensename = findViewById(R.id.et_expensename);
        et_date = findViewById(R.id.et_date);
        cbox_setReminder = findViewById(R.id.cbox_setReminder);
        spin_sign = findViewById(R.id.spin_sign);
        // This is the array for the positive + and negative - signs in the amount value.
        CharSequence[] items = getResources().getTextArray(R.array.signs);
        RightAlignedSpinnerAdapter signAdapter = new RightAlignedSpinnerAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);
        signAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sign.setAdapter(signAdapter);
    }
    private void color() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        }
    }

   // To parse the value into amount and negate its absolute value if "-" sign is selected.
    private float addAmount() {
        String selectedSign = spin_sign.getSelectedItem().toString();
        String amountString = et_amount.getText().toString();
        float amount = 0.0f;

        if (!amountString.isEmpty()) {
            try {
                amount = Float.parseFloat(amountString);

                if ("-".equals(selectedSign)) {
                    amount = -Math.abs(amount);
                }
                return amount;
            } catch (NumberFormatException e) {
                // If it cannot be parsed, shows an error.
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTrackerForm.this);
                builder.setCancelable(false);
                builder.setTitle("Input Error!");
                builder.setMessage("Please enter a valid number for the amount field.");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        }
        return 0.0f;
    }

    // After pressing the date on the calendar, the date selected will be inputted automatically in the field.
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String selectedDate = dateFormat.format(cal.getTime());

        et_date.setText(selectedDate);
    }
}