package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        //Creates an inflater associated with the activity
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneExpenseLine = inflater.inflate(R.layout.tracker_one_line, vG, false);

        TextView tv_expenseName = oneExpenseLine.findViewById(R.id.tv_expensename);
        TextView tv_amount = oneExpenseLine.findViewById(R.id.tv_amountvalue);
        // ImageView iv_icon = oneExpenseLine.findViewById(R.id.iv_tracker_icon);
        TextView tv_date = oneExpenseLine.findViewById(R.id.tv_date);

        Expense p = this.getItem(position);

        tv_expenseName.setText(p.getExpenseName());
        tv_amount.setText(Integer.toString((int) p.getAmount()));
        tv_date.setText(p.getDate());

        /*
        int icon_resource_numbers [] = {
                R.drawable.car_loan_icon, //1
                R.drawable.debt_icon, // 2
                R.drawable.electricity_icon, // 3
                R.drawable.graduation_cap_icon, // 4
                R.drawable.netflix_icon, // 5
                R.drawable.rent_icon, // 6
                R.drawable.water_icon // 7
        };
        iv_icon.setImageResource(icon_resource_numbers[position]);
        */
        return oneExpenseLine;
    }
}
