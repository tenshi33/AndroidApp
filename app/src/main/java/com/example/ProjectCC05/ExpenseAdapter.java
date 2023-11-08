package com.example.ProjectCC05;

import static java.util.Collections.addAll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
        View oneExpenseLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneExpenseLine = inflater.inflate(R.layout.tracker_one_line, vG, false);

        TextView tv_expenseName = oneExpenseLine.findViewById(R.id.tv_expensename);
        TextView tv_amount = oneExpenseLine.findViewById(R.id.tv_amountvalue);
        TextView tv_date = oneExpenseLine.findViewById(R.id.tv_date);

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

        // clear();
        addAll(expenses);
        notifyDataSetChanged();
        //addAll(expenses);
        //notifyDataSetChanged();
        //this.myExpenses = myExpenses;
    }
}
