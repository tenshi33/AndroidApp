package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;


public class MainPage extends AppCompatActivity {

    Button btn_add, btn_sortABC, btn_sortAMOUNT;

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
        btn_sortABC = findViewById(R.id.btn_sortABC);
        btn_sortAMOUNT = findViewById(R.id.btn_sortAMOUNT);
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

                // create new person object
                Expense e = new Expense(expenseName, amount, pictureNumber);

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

        btn_sortAMOUNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(myExpenses.getMyFriendsList(), new Comparator<Expense>() {
                    @Override
                    public int compare(Expense p1, Expense p2) {
                        /*
                            Sort by amount, lowest to highest
                         */
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
        i.putExtra("picturenumber", p.getPictureNumber());

        startActivity(i);

    }


}