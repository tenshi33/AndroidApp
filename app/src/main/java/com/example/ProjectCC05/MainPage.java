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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.core.content.ContextCompat;

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
import java.util.Locale;

    // This is the MainPage where all the activity is connected, and major parts of the code is focused in here

public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tv_totalAmount;
    ImageButton btn_history, btn_home;
    Button btn_add, btn_addBalance, btn_subtractBalance;
    Spinner spin_sort;
    ListView lv_listOfExpenses;
    ExpenseAdapter expenseAdapter;
    HistoryAdapter historyAdapter;
    MyExpenses myExpenses;
    MyHistoryExpenses myHistoryExpenses;
    private int positionEdited = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // Calls the method for coloring the status bar
        color();
        // Calling the method for initialization and finding the variables based on their ID on the xml
        initializers();
        // Loading and displaying data saved from the SharedPreferences
        loadAndDisplayData();
        float savedTotal = getSavedTotalAmount();
        displayTotalAmount(savedTotal);
        // This array contains the spin_sort where the sort method is located
        spinSortArray();
        // Calling the method for creating NotificationChannel.
        oreoAndAbove();
        // Calling the method for adding new items
        addNewItem();
        // Calling the method for all the onClickListeners of the MainPage.
        onClickListeners();
    }

    // Sets the status bar color with the same color as the background
    private void color() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        }
    }
    // Initialization and finding the variables based on their ID on the xml
    private void initializers() {
        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_add = findViewById(R.id.btn_add);
        btn_history = findViewById(R.id.btn_history);
        btn_addBalance = findViewById(R.id.btn_addBalance);
        btn_subtractBalance = findViewById(R.id.btn_subtractBalance);
        spin_sort = findViewById(R.id.spin_sort);
        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        lv_listOfExpenses = findViewById(R.id.lv_listOfExpenses);
        /*
            The getMyExpense() and getMyExpenses2() methods are called on the MyApplication object to retrieve two lists of expenses.
            These lists are then assigned to the myExpenses and myHistoryExpenses variables.
         */
        myExpenses = ((MyApplication) this.getApplication()).getMyExpense();
        myHistoryExpenses = ((MyApplication) this.getApplication()).getMyExpenses2();
        // Instantiation the custom adapters
        expenseAdapter = new ExpenseAdapter(MainPage.this, myExpenses);
        historyAdapter = new HistoryAdapter(MainPage.this, myHistoryExpenses);
        // Setting the adapter for the lv_listOfExpenses
        lv_listOfExpenses.setAdapter(expenseAdapter);
    }

    // All the .onClickListeners of the buttons and items in the MainPage
    private void onClickListeners() {
        // Button for starting the HistoryPage.class
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), HistoryPage.class);
                startActivity(i);
            }
        });

        // Button for starting NewTrackerForm, which adds an item to the lv_listOfExpenses.
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NewTrackerForm.class);
                startActivity(i);
            }
        });

        // Button for adding a balance to the total balance
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
                            float enteredBalance = Float.parseFloat(input.getText().toString());

                            // Add the balance to the total amount
                            float currentTotal = getTotalAmount();
                            float newTotal = currentTotal + Math.abs(enteredBalance);

                            // Save the new total amount
                            saveTotalAmount(newTotal);
                            // Display the updated total
                            displayTotalAmount(newTotal);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainPage.this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        // Button for subtracting a balance to the total balance
        btn_subtractBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                builder.setTitle("Subtract Balance");

                final EditText input = new EditText(MainPage.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            // Get entered amount to subtract
                            float enteredAmount = Float.parseFloat(input.getText().toString());

                            // Subtract the amount from the total
                            float currentTotal = getTotalAmount();
                            /*
                                Math.abs is used for the absolute amount. If the amount is
                                negative and the sign for this is -, then the enteredAmount
                                will add instead of subtract. By using the absolute amount,
                                it will remove the negative sign and it will be subtracted
                             */
                            float newTotal = currentTotal - Math.abs(enteredAmount);
                            // Save the new total amount
                            saveTotalAmount(newTotal);

                            // Display the updated total
                            displayTotalAmount(newTotal);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainPage.this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        // For Opening the MainPage
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainPage();
            }
        });

        // For editing the items when tapped
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
                        // Get the item that was selected in the list
                        Object selectedItem = lv_listOfExpenses.getItemAtPosition(position);
                        // Remove the selected item from the list of expenses
                        myExpenses.getMyExpenseList().remove(selectedItem);
                        // Add the selected item to the history
                        addHistoryItem((History) selectedItem);
                        // Remove the selected item from SharedPreferences
                        removeItemFromSharedPreferences((History) selectedItem);
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

    // Creates NotificationChannel if Android version is Oreo and above. This is required.
    private void oreoAndAbove() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("expense_channel_id", "Expense Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    // This array contains the spin_sort where the sort method is located
    private void spinSortArray() {
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this, R.array.sort, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sort.setAdapter(arr);
        spin_sort.setOnItemSelectedListener(this);
    }

    // Saves the amount of the totalBalance in the SharedPreferences
    private void saveTotalAmount (float amount) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("totalBalance", amount);
        editor.apply();
    }

    // Loads the amount that was saved from saveTotalAmount method
    private float getSavedTotalAmount () {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        return preferences.getFloat("totalBalance", 0.0f);
    }

    // Get the total amount for adding and subtracting purposes
    private float getTotalAmount() {
        // Get the numeric value from the tv_totalAmount
        String totalAmountString = tv_totalAmount.getText().toString();

        // Check if the total amount string is not empty
        if (!totalAmountString.isEmpty()) {
            if (totalAmountString.contains("-")) {
                //Remove all non-numeric characters except minus sign and the decimal
                totalAmountString = totalAmountString.replaceAll("[^0-9.-]", "");
            } else {
                totalAmountString = totalAmountString.replaceAll("[^0-9.]", "");
            }
            // Parse the totalAmountString to a float then return it
            return Float.parseFloat(totalAmountString);
        } else {
            return 0.0f;
        }
    }

    // Displays the totalAmount in the tv_totalAmount field and changes its color depending on its value
    public void displayTotalAmount (float amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", symbols);
        String formattedTotalAmount = "₱" + decimalFormat.format(amount);

        int color;
        if (amount >= 0) {
            color = Color.parseColor("#AEF395");
        } else {
            color = Color.parseColor("#A91B0D");
        }

        tv_totalAmount.setText(formattedTotalAmount);
        tv_totalAmount.setTextColor(color);
    }

    /*
        For editing an item when tapped. It redirects to the NewTrackerForm with all its field
        filled out with the current values of the variable.
     */
    public void editPerson(int position) {
        Intent i = new Intent(getApplicationContext(), NewTrackerForm.class);
        // get the contents of person at position
        Expense p = myExpenses.getMyExpenseList().get(position);

        positionEdited = position;
        i.putExtra("edit", positionEdited);
        i.putExtra("name", p.getExpenseName());
        i.putExtra("amount", p.getAmount());
        i.putExtra("date", p.getDate());
        i.putExtra("setReminder", p.isSetReminder());

        startActivity(i);
    }

    // For sorting purposes
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // Alphabetical
        if (spin_sort.getSelectedItem().toString().equals("A-Z")) {
            Toast.makeText(MainPage.this, "Sort by Alphabet", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList());
            expenseAdapter.notifyDataSetChanged();
        }
        // Amount
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
        // Date
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
        // Do not delete this block of code, it will cause an error.
    }

    // For opening MainPage
    public void openMainPage(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

    /*
        If the navigation button back was pressed, it will prompt the user to log out
        instead of going back to the previous activity.
     */
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
    }

    // Adds the item to the history page
    public void addHistoryItem (History history) {
        if (!myHistoryExpenses.getMyHistoryList().contains(history)) {
            myHistoryExpenses.getMyHistoryList().add(history);
            historyAdapter.notifyDataSetChanged();
            saveHistoryToSharedPreferences(myHistoryExpenses.getMyHistoryList());
        }
    }

    /*
        Saves the list to the SharedPreferences. Imagine a magical box where
        we can keep a list of stories about the expenses. We want to save these
        so that we can read them later.

        Open the magical box: (SharedPreferences)
        First, we open the magical box called "MyAppPrefs" where we keep all our
        special this.

        Prepare a Blank Page: (Editor)
        Next, we take out a blank page from the box.
        This is where we can write down our stories

        Write down the stories: (JSON Array)
        Now, we start writing down our stories on this blank page.
        Each story is about an expense, and we write the name, amount, and the date.

        Put the page back in the box: (editor.putString("history", jsonArray.toString()) and editor.apply());
        After writing down all our stories, we put this page back into the
        magical box (SharedPreferences) so we can find it later.

        Closing the box:
        We close the magical box, and now the stories about expenses is safely stored
        inside it.
     */
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

    /*
        In this method, we load and display the data
        that were saved in the SharedPreferences.
        This is where we take out the Page that we wrote and
        put in the magic box, and read it.
     */
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
                    boolean setReminder = jsonExpense.getBoolean("setReminder");
                    Expense loadedExpense = new Expense(expenseName, amount, dateString, setReminder);
                    myExpenses.getMyExpenseList().add(loadedExpense);
                }

                expenseAdapter.setMyExpenses(myExpenses.getMyExpenseList());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // Removing item from SharedPreferences. See HistoryPage.class for the same explanation
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
                    boolean setReminder = jsonExpense.getBoolean("setReminder");

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

    // Save to SharedPreferences. See saveHistoryExpensesToSharedPreferences method for more explanation
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
                jsonExpense.put("setReminder", expense.isSetReminder());
                jsonArray.put(jsonExpense);
            }

            editor.putString("expenses", jsonArray.toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // The adding of new item from the NewTrackerForm to the MainPage's lv_listOfExpenses.
    private void addNewItem() {

        // listen for incoming messages or see if there is an Intent coming to this
        Bundle incomingMessages = getIntent().getExtras();

        // capture incoming data
        if (incomingMessages != null) {
            String expenseName = incomingMessages.getString("name");
            float amount = incomingMessages.getFloat("amount", 0.0f);
            positionEdited = incomingMessages.getInt("edit", -1);
            String dateString = incomingMessages.getString("date");
            boolean setReminder = incomingMessages.getBoolean("setReminder", false);
            // Check if the amount has changed
            float oldAmount = (positionEdited > -1 && positionEdited < myExpenses.getMyExpenseList().size())
                    ? myExpenses.getMyExpenseList().get(positionEdited).getAmount()
                    : 0.0f;

            if (amount != oldAmount) {
                // Adjust the total balance only if the amount has changed
                float amountToAdd = Math.abs(amount - oldAmount);
                float newTotal;
                if (amount >= 0) {
                    newTotal = getTotalAmount() + amountToAdd;
                } else {
                    newTotal = getTotalAmount() - amountToAdd;
                }
                saveTotalAmount(newTotal);
                displayTotalAmount(newTotal);
            }


            if (positionEdited > -1 && positionEdited < myExpenses.getMyExpenseList().size()) {
                // Remove the item at the specified position if it is an edit operation
                Expense e = new Expense(expenseName, amount, dateString, setReminder);
                myExpenses.getMyExpenseList().set(positionEdited, e);
            } else {
                Expense e = new Expense(expenseName, amount, dateString, setReminder);
                myExpenses.getMyExpenseList().add(e);
            }

            // Updating the list, and saving it to the SharedPreferences
            expenseAdapter.notifyDataSetChanged();
            saveExpensesToSharedPreferences(myExpenses.getMyExpenseList());
            saveHistoryToSharedPreferences(myHistoryExpenses.getMyHistoryList());

            // If setReminder checkbox is checked, set a notification
            if (setReminder) {
                setAlarmManager(dateString);
            }

            positionEdited = -1;
        }
    }

    // For setting up a notification at the date inputted by the user.
    private void setAlarmManager (String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
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



