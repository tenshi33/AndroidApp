package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tv_totalAmount;
    ImageButton btn_history, homebtn;
    Button btn_add;
    Spinner spin_sort;

    ListView lv_listOfExpenses;

    ExpenseAdapter adapter;
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

        myExpenses = ((MyApplication) this.getApplication()).getMyExpense();
        myExpense2 = ((MyApplication) this.getApplication()).getMyExpenses2();

        btn_add = findViewById(R.id.btn_add);
        btn_history = findViewById(R.id.btn_history);
        spin_sort = findViewById(R.id.spin_sort);
        tv_totalAmount = findViewById(R.id.tv_totalAmount);
        lv_listOfExpenses = findViewById(R.id.lv_listOfExpenses);

        adapter = new ExpenseAdapter(MainPage.this, myExpenses);
        adapter2 = new ExpenseAdapter2(MainPage.this, myExpense2);

        lv_listOfExpenses.setAdapter(adapter);

        // listen for incoming messages or see if there is an Intent coming to this
        Bundle incomingMessages = getIntent().getExtras();

        // capture incoming data
        if (incomingMessages != null) {

            try {
                String expenseName = incomingMessages.getString("name");
                float amount = Float.parseFloat(incomingMessages.getString("age"));
                // int pictureNumber = Integer.parseInt(incomingMessages.getString("picturenumber"));
                int positionEdited = incomingMessages.getInt("edit");
                String dateString = incomingMessages.getString("date");


                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date dueDate = sdf.parse(dateString);
                    long dueDateMillis = dueDate.getTime();

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent (this, NotificationReceiver.class);

                    intent.putExtra("dueDateMillis", dateString);

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
                Expense e = new Expense(expenseName, amount, /*pictureNumber,*/ dateString);

                // add person to the list and update adapter
                if (positionEdited > -1) {
                    myExpenses.getMyExpenseList().remove(positionEdited);
                }
                myExpenses.getMyExpenseList().add(e);
                adapter.notifyDataSetChanged();

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
                        adapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
                return true;
            }
        });

        // Calculates the total of the expenses then displays it to the tv_totalAmount
        float totalAmount = 0;
        for (Expense item : myExpenses.myExpenseList) {
            totalAmount += item.getAmount();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTotalAmount = "₱ " + decimalFormat.format(totalAmount);
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
        if (spin_sort.getSelectedItem().toString().equals("Alphabet")) {
            Toast.makeText(MainPage.this, "Sort by Alphabet", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList());
            adapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Amount")) {
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
            adapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Date")) {
            Toast.makeText(MainPage.this, "Sort by Date", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyExpenseList(), new Comparator<Expense>() {
                @Override
                public int compare(Expense d1, Expense d2) {
                    return d1.getDate().compareTo(d2.getDate());
                }
            });
            adapter.notifyDataSetChanged();
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
}

