package com.example.ProjectCC05;

import static java.util.Collections.addAll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;


public class ExpenseAdapter extends BaseAdapter {

    /*
        The custom adapter for the Expense.
        The ExpenseAdapter class is designed to serve as a custom adapter for
        handling the display of expense-related data.
        It is initialized with references to the activity and the data it will adapt.
     */

    Activity mActivity;

    MyExpenses myExpenses;

    public ExpenseAdapter(Activity mActivity, MyExpenses myExpenses) {
        this.mActivity = mActivity;
        this.myExpenses = myExpenses;
    }

    @Override
    public int getCount() {
        return myExpenses.getMyExpenseList().size();
    }

    @Override
    public Expense getItem(int position) {
        return myExpenses.getMyExpenseList().get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup vG) {
        /*
            The LayoutInflater inflater is used as a way to get the information of the UI in the tracker_one_line.xml
            As the name suggests, it automatically layouts the variables located in the xml to the lv_listOfExpenses
         */
        View oneExpenseLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneExpenseLine = inflater.inflate(R.layout.tracker_one_line, vG, false);

        TextView tv_expenseName = oneExpenseLine.findViewById(R.id.tv_expensename);
        TextView tv_amount = oneExpenseLine.findViewById(R.id.tv_amountvalue);
        TextView tv_date = oneExpenseLine.findViewById(R.id.tv_date);

        Expense currentExpense = getItem(position);

        tv_amount.setText(String.valueOf(currentExpense.getAmount()));
        if (currentExpense.getAmount() >= 0) {
            tv_amount.setTextColor(Color.parseColor("#AEF395"));
        } else {
            tv_amount.setTextColor(Color.parseColor("#A91B0D"));
        }

        Expense p = this.getItem(position);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", symbols);
        String formattedAmount = "₱ " + decimalFormat.format(p.getAmount());

        tv_expenseName.setText(p.getExpenseName());
        tv_amount.setText(formattedAmount);
        tv_date.setText(p.getDate());

        return oneExpenseLine;
    }

    public void setMyExpenses(List<Expense> expenses) {
        addAll(expenses);
        notifyDataSetChanged();
    }
}
