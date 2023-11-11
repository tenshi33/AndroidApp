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
    //HistoryAdapter adapter2;
    //MyHistory myHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        //myHistory = ((MyApplication) this.getApplication()).getMyHistory();
        myHistoryExpenses = ((MyApplication) this.getApplication()).getMyExpenses2();
        btn_back = findViewById(R.id.btn_back);
        lv_historyOfExpenses = findViewById(R.id.lv_historyOfExpenses);
        historyAdapter = new HistoryAdapter(HistoryPage.this, myHistoryExpenses);
        //adapter2 = new HistoryAdapter(HistoryPage.this, myHistory);


        // For loading the data in the SharedPreferences
        myHistoryExpenses.getMyExpenseList2().clear();
        myHistoryExpenses.getMyExpenseList2().addAll(getHistoryFromSharedPreferences());

        lv_historyOfExpenses.setAdapter(historyAdapter);

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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void removeItemFromHistorySharedPreferences (History history) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String expenseJson = sharedPreferences.getString("history", null);

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
                        // Do not add the item to the updated array, effectively removing it
                        continue;
                    }

                    updatedArray.put(jsonExpense);
                }

                editor.putString("history", updatedArray.toString());
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
