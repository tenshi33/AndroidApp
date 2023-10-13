package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;


public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_add, btn_sortABC, btn_sortAMOUNT;
    Spinner spin_sort;

    ListView lv_listOfExpenses;

    ExpenseAdapter adapter;

    MyExpenses myExpenses;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        myExpenses = ((MyApplication) this.getApplication()).getMyExpense();

        btn_add = findViewById(R.id.btn_add);
        spin_sort = findViewById(R.id.spin_sort);
        /*
        btn_sortABC = findViewById(R.id.btn_sortABC);
        btn_sortAMOUNT = findViewById(R.id.btn_sortAMOUNT);
         */
        lv_listOfExpenses = findViewById(R.id.lv_listOfExpenses);

        adapter = new ExpenseAdapter(MainPage.this, myExpenses);

        lv_listOfExpenses.setAdapter(adapter);

        // listen for incoming messages or see if there is an Intent coming to this
        Bundle incomingMessages = getIntent().getExtras();

        // capture incoming data
        if (incomingMessages != null) {

            try {
                String expenseName = incomingMessages.getString("name");
                int amount = Integer.parseInt(incomingMessages.getString("age"));
                int pictureNumber = Integer.parseInt(incomingMessages.getString("picturenumber"));
                int positionEdited = incomingMessages.getInt("edit");
                String date = incomingMessages.getString("date");

                // create new person object
                Expense e = new Expense(expenseName, amount, pictureNumber, date);

                // add person to the list and update adapter
                if (positionEdited > -1) {
                    myExpenses.getMyFriendsList().remove(positionEdited);
                }
                myExpenses.getMyFriendsList().add(e);
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sort.setAdapter(adapter);
        spin_sort.setOnItemSelectedListener(this);


        btn_add.setOnClickListener(new View.OnClickListener() {
            /*
                Intent: the .setOnClickListener is for the pop up of another xml page
                after clicking or pressing the button
             */
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NewTrackerForm.class);
                /*
                    The two parameters of the Intent i are 1. Context, and 2. Destination
                    View view is the button btn_add is where it comes from, while
                    NewPersonForm is the java code is where it goes to
                    2 types of intent: Explicit Intent and Implicit Intent
                    This is an explicit intent where we specify directly the destination
                    of the activity. Implicit is where the user will decide where to go
                    like "send a phone call" will open a dialer or "open maps" will likely open
                    google maps but there could be another map app
                 */
                startActivity(i);
            }
        });


        /*
        btn_sortAMOUNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(myExpenses.getMyFriendsList(), new Comparator<Expense>() {
                    @Override
                    public int compare(Expense p1, Expense p2) {

                            Sort by amount, lowest to highest

                        return p1.getAmount() - p2.getAmount();
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });

        btn_sortABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(myExpenses.getMyFriendsList());
                adapter.notifyDataSetChanged();
            }
        });
         */


        /*
            For editing the items when tapped
         */
        lv_listOfExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                editPerson(position);
            }
        });

    }

    public void editPerson(int position) {
        Intent i = new Intent(getApplicationContext(), NewTrackerForm.class);

        // get the contents of person at position
        Expense p = myExpenses.getMyFriendsList().get(position);

        i.putExtra("edit", position);
        i.putExtra("name", p.getExpenseName());
        i.putExtra("age", p.getAmount());
        i.putExtra("date", p.getDate());
        i.putExtra("picturenumber", p.getPictureNumber());

        startActivity(i);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (spin_sort.getSelectedItem().toString().equals("Alphabet")) {
            Toast.makeText(MainPage.this, "Sort by Alphabet", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyFriendsList());
            adapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Amount")) {
            Toast.makeText(MainPage.this, "Sort by Amount", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyFriendsList(), new Comparator<Expense>() {
                @Override
                public int compare(Expense e1, Expense e2) {
                    return e1.getAmount() - e2.getAmount();
                }
            });
            adapter.notifyDataSetChanged();
        }
        if (spin_sort.getSelectedItem().toString().equals("Date")) {
            Toast.makeText(MainPage.this, "Sort by Date", Toast.LENGTH_SHORT).show();
            Collections.sort(myExpenses.getMyFriendsList(), new Comparator<Expense>() {
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
}