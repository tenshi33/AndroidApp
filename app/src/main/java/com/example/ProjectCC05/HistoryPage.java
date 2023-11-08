package com.example.ProjectCC05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryPage extends AppCompatActivity {
    ImageButton btn_back;
    ListView lv_historyOfExpenses;
    ExpenseAdapter2 adapter2;
    MyExpenses2 myExpenses2;
    //HistoryAdapter adapter2;
    //MyHistory myHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);

        //myHistory = ((MyApplication) this.getApplication()).getMyHistory();
        myExpenses2 = ((MyApplication) this.getApplication()).getMyExpenses2();
        btn_back = findViewById(R.id.btn_back);
        lv_historyOfExpenses = findViewById(R.id.lv_historyOfExpenses);
        adapter2 = new ExpenseAdapter2(HistoryPage.this, myExpenses2);
        //adapter2 = new HistoryAdapter(HistoryPage.this, myHistory);

        lv_historyOfExpenses.setAdapter(adapter2);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}
