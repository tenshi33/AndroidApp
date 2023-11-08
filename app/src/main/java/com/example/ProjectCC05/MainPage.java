package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tv_totalAmount;
    ImageButton btn_history, homebtn;
    Button btn_add;
    Spinner spin_sort;

    ListView lv_listOfExpenses;

    ExpenseAdapter expenseAdapter;
    ExpenseAdapter2 adapter2;

    MyExpenses myExpenses;
    MyExpenses2 myExpense2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("expense_channel_id", "Expense Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        homebtn = (ImageButton) findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });

        btn_add = findViewById(R.id.btn_add);
        btn_history = findViewById(R.id.btn_history);
        spin_sort = findViewById(R.id.spin_sort);
        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        lv_listOfExpenses = findViewById(R.id.lv_listOfExpenses);

        myExpenses = ((MyApplication) this.getApplication()).getMyExpense();
        myExpense2 = ((MyApplication) this.getApplication()).getMyExpenses2();
        expenseAdapter = new ExpenseAdapter(MainPage.this, myExpenses);
        adapter2 = new ExpenseAdapter2(MainPage.this, myExpense2);

        loadAndDisplayData();

        lv_listOfExpenses.setAdapter(expenseAdapter);

        addNewItem();

        /*
        // listen for incoming messages or see if there is an Intent coming to this
        Bundle incomingMessages = getIntent().getExtras();

        if (incomingMessages != null) {
            try {
                String expenseName = incomingMessages.getString("name");
                float amount = Float.parseFloat(incomingMessages.getString("age"));
                int positionEdited = incomingMessages.getInt("edit");
                String dateString = incomingMessages.getString("date");

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date dueDate = sdf.parse(dateString);
                    long dueDateMillis = dueDate.getTime();

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent (this, NotificationReceiver.class);

                    intent.putExtra("dueDateMillis", dueDateMillis);

                    int flags;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        flags = PendingIntent.FLAG_IMMUTABLE;
                    }
                    else {
                        flags = 0;
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);

                    alarmManager.set(AlarmManager.RTC, dueDateMillis, pendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Expense e = new Expense(expenseName, amount, dateString);

                if (positionEdited >-1) {
                    myExpenses.getMyExpenseList().remove(positionEdited);
                }
                myExpenses.getMyExpenseList().add(e);
                expenseAdapter.notifyDataSetChanged();

                saveDataToSharedPreferences(expenseName, amount, dateString);

            } catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setCancelable(false);
                builder.setTitle("You have encountered an error!");
                builder.setMessage("You entered the wrong input. Please try again");

                builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // To go back to the New Expense Form activity after pressing try again

                        Intent tryAgain = new Intent(getApplicationContext(), NewTrackerForm.class);
                        startActivity(tryAgain);
                    }
                });
                builder.show();
            }
        }
        */



        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), HistoryPage.class);
                startActivity(i);
            }
        });

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this, R.array.sort, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sort.setAdapter(arr);
        spin_sort.setOnItemSelectedListener(this);


        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NewTrackerForm.class);
                startActivity(i);
            }
        });


        /*
            For editing the items when tapped
         */
        lv_listOfExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                editPerson(position);
            }
        });


        // Deleting an Item and then transferring it to the history listview
        lv_listOfExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int item = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to delete this item?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Object selectedItem = lv_listOfExpenses.getItemAtPosition(position);
                        myExpenses.getMyExpenseList().remove(selectedItem);
                        myExpense2.getMyExpenseList2().add((Expense2) selectedItem);
                        expenseAdapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
                return true;
            }
        });

       totalAmount();





    }

    public void totalAmount() {
        // Calculates the total of the expenses then displays it to the tv_totalAmount
        float totalAmount = 0;
        for (Expense item : myExpenses.getMyExpenseList()) {
            totalAmount += item.getAmount();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");  // Set the currency symbol

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", symbols);
        String formattedTotalAmount = "₱" + decimalFormat.format(totalAmount);

        tv_totalAmount.setText(formattedTotalAmount);
    }


    public void editPerson(int position) {
        Intent i = new Intent(getApplicationContext(), NewTrackerForm.class);

        // get the contents of person at position
        Expense p = myExpenses.getMyExpenseList().get(position);

        i.putExtra("edit", position);
        i.putExtra("name", p.getExpenseName());
        i.putExtra("age", p.getAmount());
        i.putExtra("date", p.getDate());
        // i.putExtra("picturenumber", p.getPictureNumber());

        startActivity(i);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (spin_sort.getSelectedItem().toString().equals("A-Z")) {
            Toast.makeText(MainPage.this, "Sort by Alphabet", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList());
            expenseAdapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Low to High")) {
            Toast.makeText(MainPage.this, "Sort by Amount", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList(), new Comparator<Expense>() {
                @Override
                public int compare(Expense e1, Expense e2) {
                    float expense1 = e1.getAmount();
                    float expense2 = e2.getAmount();

                    if (expense1 < expense2) {
                        return -1; // e1 < e2
                    }
                    else if (expense1 > expense2) {
                        return 1; // e1 > e2
                    }
                    else {
                        return 0; // e1 = e2
                    }
                    //return e1.getAmount() - e2.getAmount();
                }
            });
            expenseAdapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Date")) {
            Toast.makeText(MainPage.this, "Sort by Date", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList(), new Comparator<Expense>() {
                @Override
                public int compare(Expense d1, Expense d2) {
                    return d1.getDate().compareTo(d2.getDate());
                }
            });
            expenseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void openMainPage(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setCancelable(true);
        builder.setTitle("Log Out");
        builder.setMessage("Do you want to log out?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent logOut = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(logOut);
            }
        });
        builder.show();
        //super.onBackPressed();
    }



    private void loadAndDisplayData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String expenseJson = sharedPreferences.getString("expenses", null);

        if (expenseJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(expenseJson);

                myExpenses.getMyExpenseList().clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonExpense = jsonArray.getJSONObject(i);
                    String expenseName = jsonExpense.getString("expenseName");
                    float amount = (float) jsonExpense.getDouble("amount");
                    String dateString = jsonExpense.getString("dateString");
                    Expense loadedExpense = new Expense(expenseName, amount, dateString);
                    myExpenses.getMyExpenseList().add(loadedExpense);
                }

                expenseAdapter.setMyExpenses(myExpenses.getMyExpenseList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //int expenseCount = sharedPreferences.getInt("expenseCount", 0);

        /*
        for (int i = 0; i < expenseCount; i++) {
            String savedExpenseName = sharedPreferences.getString("expenseName" + i, "");
            float savedAmount = sharedPreferences.getFloat("amount" + i , 0.0f);
            String savedDateString = sharedPreferences.getString("dateString" + i, "");

            // Creates an Expense object with the loaded data
            Expense loadedExpense = new Expense(savedExpenseName, savedAmount, savedDateString);

            if (!myExpenses.getMyExpenseList().contains(loadedExpense)) {
                myExpenses.getMyExpenseList().add(loadedExpense);
            }
        }
        */
        // Notify the adapter of the changes

    }

    public void saveExpensesToSharedPreferences(List<Expense> expenses) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for (Expense expense: expenses) {
                JSONObject jsonExpense = new JSONObject();
                jsonExpense.put("expenseName", expense.getExpenseName());
                jsonExpense.put("amount", expense.getAmount());
                jsonExpense.put("dateString", expense.getDate());
                jsonArray.put(jsonExpense);
            }

            editor.putString("expenses", jsonArray.toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addNewItem() {
        // listen for incoming messages or see if there is an Intent coming to this
        Bundle incomingMessages = getIntent().getExtras();

        // capture incoming data
        if (incomingMessages != null) {
            try {
                String expenseName = incomingMessages.getString("name");
                float amount = Float.parseFloat(incomingMessages.getString("age"));
                int positionEdited = incomingMessages.getInt("edit");
                String dateString = incomingMessages.getString("date");

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                try {
                    Date dueDate = sdf.parse(dateString);
                    long dueDateMillis = dueDate.getTime();

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent (this, NotificationReceiver.class);

                    intent.putExtra("dueDateMillis", dueDateMillis);

                    int flags;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        flags = PendingIntent.FLAG_IMMUTABLE;
                    }
                    else {
                        flags = 0;
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);
                    alarmManager.set(AlarmManager.RTC, dueDateMillis, pendingIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // create new person object
                Expense e = new Expense(expenseName, amount, dateString);

                // add person to the list and update adapter
                if (positionEdited > -1) {
                    myExpenses.getMyExpenseList().remove(positionEdited);
                }
                myExpenses.getMyExpenseList().add(e);
                expenseAdapter.notifyDataSetChanged();

                saveExpensesToSharedPreferences(myExpenses.getMyExpenseList());
                /*
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                int expenseCount = myExpenses.getMyExpenseList().size();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("expenseCount", expenseCount);

                for (int i = 0; i < expenseCount; i++) {
                    Expense expense = myExpenses.getMyExpenseList().get(i);
                    editor.putString("expenseName" + i, expense.getExpenseName());
                    editor.putFloat("amount" + i, expense.getAmount());
                    editor.putString("dateString" + i, expense.getDate());
                }

                editor.apply();
                //saveDataToSharedPreferences(expenseName, amount, dateString);
                */
            } catch(Exception e) {
                /*
                    To show error when Input Mismatch Error occurred
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setCancelable(false);
                builder.setTitle("You have encountered an error!");
                builder.setMessage("You entered the wrong input. Please try again");

                builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*
                            To go back to the New Expense Form activity after pressing try again
                         */
                        Intent tryAgain = new Intent(getApplicationContext(), NewTrackerForm.class);
                        startActivity(tryAgain);
                    }
                });
                builder.show();

            }
        }
    }
}

