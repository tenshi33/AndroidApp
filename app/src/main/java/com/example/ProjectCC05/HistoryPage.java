package com.example.ProjectCC05;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends AppCompatActivity {
    ImageButton btn_back;
    ListView lv_historyOfExpenses;
    HistoryAdapter historyAdapter;
    MyHistoryExpenses myHistoryExpenses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        myHistoryExpenses = ((MyApplication) this.getApplication()).getMyExpenses2();
        btn_back = findViewById(R.id.btn_back);
        lv_historyOfExpenses = findViewById(R.id.lv_historyOfExpenses);
        historyAdapter = new HistoryAdapter(HistoryPage.this, myHistoryExpenses);
        // For loading the data in the SharedPreferences
        myHistoryExpenses.getMyExpenseList2().clear();
        myHistoryExpenses.getMyExpenseList2().addAll(getHistoryFromSharedPreferences());
        lv_historyOfExpenses.setAdapter(historyAdapter);

        // Deleting an Item on the history listview using long click after confirming with the AlertDialog
        lv_historyOfExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPage.this);
                builder.setCancelable(true);
                builder.setTitle("Delete Item From History");
                builder.setMessage("Do you want to delete this item from the history? You will not be able to " +
                        "recover this item after deletion.");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the position of the item
                        History selectedExpense = myHistoryExpenses.getMyExpenseList2().get(position);
                        // Remove the item from the SharedPreferences
                        removeItemFromHistorySharedPreferences(selectedExpense);
                        // Remove the item from the list
                        myHistoryExpenses.getMyExpenseList2().remove(selectedExpense);
                        // Update the list. notifyDataSetChanged somehow not working.
                        historyAdapter = new HistoryAdapter(HistoryPage.this, myHistoryExpenses);
                        lv_historyOfExpenses.setAdapter(historyAdapter);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });

        // Back button to finish the activity, can use Intent to start the MainPage.class
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
        SharedPreferences is a way to store an item. Kind of like a Database but for local and for little amount of
        data only. removeItemFromHistorySharedPreferences removes the item selected from the SharedPreferences (database)
        And you will not see it again after restarting the app.
     */
    public void removeItemFromHistorySharedPreferences (History history) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String expenseJson = sharedPreferences.getString("history", null);

        /*
            If the expenseJson string is not null, this creates a new JSONArray object and
            iterates over the items in the original JSON array using a for loop. For each item in the array,
            the method checks if it matches the item to be removed using the equals() method.
            If the item matches, it is not added to the new JSON array, effectively removing it from the list

            Imagine having a bunch of toys (represented as items) in a toy box (represented as a JSON array).
            Now, you want to take out a specific toy from the toy box.

            Check the Toy Box: (if (expenseJson != null))
            First, look inside the toy box to see if there are any toys in there.
            This is like checking if the JSON string (which holds information about the toys) is not empty.

            Look at Each Toy: (for loop)
            Now, start looking at each toy in the toy box one by one.
            Use a special tool to go through each toy.

            Compare Toys: (if .equals(), or ==)
            For each toy, compare it to the toy you want to take out.
            Look at each toy and ask, "Is this the toy I want to remove?"
            Do this by using a method called equals(), which helps you see if two toys are the same.

            Remove the Toy: (continue;)
            If you find a toy that is the same as the one you want to remove,
            and decided not to put it in your new toy box. This is like saying, "Okay, you stay out;
            I don't want you in my toy box anymore." For the toys that are not the one you want to remove,
            you put them in your new toy box.

            In simple terms, it's like checking each toy in the toy box.
            If you find the toy you don't like, you leave it out; otherwise, you keep it.
            This way, you end up with a toy box that doesn't have the toy you wanted to remove.
         */
        if (expenseJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(expenseJson);
                JSONArray updatedArray = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonExpense = jsonArray.getJSONObject(i);
                    String expenseName = jsonExpense.getString("expenseName");
                    float amount = (float) jsonExpense.getDouble("amount");
                    String dateString = jsonExpense.getString("dateString");

                    // Check if the selected history matches the one to be removed
                    if (history.getExpenseName().equals(expenseName)
                            && history.getAmount() == amount
                            && history.getDate().equals(dateString)) {
                        // Do not add the item to the updated array, effectively removing it
                        continue;
                    }
                    // If there is no match, the current expense is added to the new array.
                    updatedArray.put(jsonExpense);
                }

                // Finally, the updated array is converted to a JSON string and stored back in the SharedPreferences.
                editor.putString("history", updatedArray.toString());
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        The same as the removeItemFromHistorySharedPreferences, but this one is for loading the list after opening the
        HistoryPage. You can see this code above.
        myHistoryExpenses.getMyExpenseList2().addAll(getHistoryFromSharedPreferences());
     */

    private List<History> getHistoryFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String historyJson = sharedPreferences.getString("history", null);

        List<History> historyList = new ArrayList<>();

        if (historyJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(historyJson);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonExpense = jsonArray.getJSONObject(i);
                    String expenseName = jsonExpense.getString("expenseName");
                    float amount = (float) jsonExpense.getDouble("amount");
                    String dateString = jsonExpense.getString("dateString");

                    History loadedExpense = new History(expenseName, amount, dateString);
                    historyList.add(loadedExpense);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return historyList;
    }
}
