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
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tv_totalAmount;
    ImageButton btn_history, btn_home;
    Button btn_add, btn_addBalance;
    Spinner spin_sort;
    ListView lv_listOfExpenses;
    ExpenseAdapter expenseAdapter;
    HistoryAdapter historyAdapter;
    MyExpenses myExpenses;
    MyHistoryExpenses myHistoryExpenses;

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

        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_add = findViewById(R.id.btn_add);
        btn_history = findViewById(R.id.btn_history);
        btn_addBalance = findViewById(R.id.btn_addBalance);
        spin_sort = findViewById(R.id.spin_sort);
        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        lv_listOfExpenses = findViewById(R.id.lv_listOfExpenses);

        myExpenses = ((MyApplication) this.getApplication()).getMyExpense();
        myHistoryExpenses = ((MyApplication) this.getApplication()).getMyExpenses2();
        expenseAdapter = new ExpenseAdapter(MainPage.this, myExpenses);
        historyAdapter = new HistoryAdapter(MainPage.this, myHistoryExpenses);
        // Loading and displaying data from the SharedPreferences
        loadAndDisplayData();

        lv_listOfExpenses.setAdapter(expenseAdapter);
        // Calling the method for adding new items
        addNewItem();

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

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });

        btn_addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setTitle("Add Balance");

                // Set up input
                final EditText input = new EditText(MainPage.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            // Get entered Balance
                            float enteredBalance = Float.parseFloat(input.getText().toString());

                            // Add the balance to the total amount
                            float currentTotal = getTotalAmount();
                            float newTotal = currentTotal + enteredBalance;

                            // Display the updated total
                            displayTotalAmount(newTotal);

                        } catch (NumberFormatException e) {
                            Toast.makeText(MainPage.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
         //   For editing the items when tapped
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to delete this item?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Object selectedItem = lv_listOfExpenses.getItemAtPosition(position);
                        myExpenses.getMyExpenseList().remove(selectedItem);

                        addHistoryItem((History) selectedItem);
                        //myExpense2.getMyExpenseList2().add((Expense2) selectedItem);
                        removeItemFromSharedPreferences((History) selectedItem);
                        // removeItemFromHistorySharedPreferences((Expense2) selectedItem);

                        expenseAdapter.notifyDataSetChanged();
                        historyAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
                return true;
            }
        });


    }

    private float getTotalAmount() {
        // Get the numeric value from the tv_totalAmount
        String totalAmountString = tv_totalAmount.getText().toString().replaceAll("[^0-9.]", "");

        if (!totalAmountString.isEmpty()) {
            return Float.parseFloat(totalAmountString);
        }
        else {
            return 0.0f;
        }
    }

    private void displayTotalAmount(float amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", symbols);
        String formattedTotalAmount = "₱" + decimalFormat.format(amount);

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
                }
            });
            expenseAdapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Date")) {
            Toast.makeText(MainPage.this, "Sort by Date", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList(), new Comparator<Expense>() {
                @Override
                public int compare(Expense d1, Expense d2) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                    try {
                        Date date1 = dateFormat.parse(d1.getDate());
                        Date date2 = dateFormat.parse(d2.getDate());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
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

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        //super.onBackPressed();
    }

    public void addHistoryItem (History history) {
        if (!myHistoryExpenses.getMyExpenseList2().contains(history)) {
            myHistoryExpenses.getMyExpenseList2().add(history);
            historyAdapter.notifyDataSetChanged();
            saveHistoryToSharedPreferences(myHistoryExpenses.getMyExpenseList2());
        }
    }

    private void saveHistoryToSharedPreferences (List<History> history) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for (History item : history) {
                JSONObject jsonExpense = new JSONObject();
                jsonExpense.put("expenseName", item.getExpenseName());
                jsonExpense.put("amount", item.getAmount());
                jsonExpense.put("dateString", item.getDate());
                jsonArray.put(jsonExpense);
            }

            editor.putString("history", jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    }

    public void removeItemFromSharedPreferences(History history) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String expenseJson = sharedPreferences.getString("expenses", null);

        if (expenseJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(expenseJson);
                JSONArray updatedArray = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonExpense = jsonArray.getJSONObject(i);
                    String expenseName = jsonExpense.getString("expenseName");
                    float amount = (float) jsonExpense.getDouble("amount");
                    String dateString = jsonExpense.getString("dateString");

                    // Check if the loaded expense matches the one to be removed
                    if (history.getExpenseName().equals(expenseName)
                            && history.getAmount() == amount
                            && history.getDate().equals(dateString)) {
                        // Do not add the item to the updated array (effectively removing it)
                        continue;
                    }

                    updatedArray.put(jsonExpense);
                }

                editor.putString("expenses", updatedArray.toString());
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            String expenseName = incomingMessages.getString("name");
            float amount = incomingMessages.getFloat("amount", 0.0f);
            int positionEdited = incomingMessages.getInt("edit");
            String dateString = incomingMessages.getString("date");
            boolean setReminder = incomingMessages.getBoolean("setReminder");

            if (positionEdited > -1) {
                    // Remove the item at the specified position if it is an edit operation
                myExpenses.getMyExpenseList().remove(positionEdited);
            }

            boolean itemExistInHistory = false;
            for (History history : myHistoryExpenses.getMyExpenseList2()) {
                if (history.getExpenseName().equals(expenseName)
                        && history.getAmount() == amount
                        && history.getDate().equals(dateString)) {
                    itemExistInHistory = true;
                    myHistoryExpenses.getMyExpenseList2().remove(history);
                    break;
                }
            }

            if (!itemExistInHistory) {
                Expense e = new Expense(expenseName, amount, dateString);
                myExpenses.getMyExpenseList().add(e);
                expenseAdapter.notifyDataSetChanged();
                saveExpensesToSharedPreferences(myExpenses.getMyExpenseList());
            }

            if (setReminder) {
                setAlarmManager(dateString);
            }
        }
    }

    private void setAlarmManager(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            Date dueDate = sdf.parse(dateString);
            long dueDateMillis = dueDate.getTime();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class);

            intent.putExtra("dueDateMillis", dueDateMillis);

            int flags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags = PendingIntent.FLAG_IMMUTABLE;
            } else {
                flags = 0;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);
            alarmManager.set(AlarmManager.RTC, dueDateMillis, pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

