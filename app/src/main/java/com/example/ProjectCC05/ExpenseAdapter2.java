package com.example.ProjectCC05;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExpenseAdapter2 extends BaseAdapter {

    Activity mActivity;
    MyExpenses2 myExpenses2;

    public ExpenseAdapter2(Activity mActivity, MyExpenses2 myExpenses2) {
        this.mActivity = mActivity;
        this.myExpenses2 = myExpenses2;
    }

    @Override
    public int getCount() {
        return myExpenses2.getMyFriendsList().size();
    }

    @Override
    public Expense2 getItem(int position) {
        return myExpenses2.getMyFriendsList().get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup vG) {

        View oneHistoryLine;

        //Creates an inflater associated with the activity
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneHistoryLine = inflater.inflate(R.layout.history_one_line, vG, false);

        TextView tv_expenseName = oneHistoryLine.findViewById(R.id.tv_historyExpenseName);
        TextView tv_amount = oneHistoryLine.findViewById(R.id.tv_historyAmountValue);
        // ImageView iv_icon = oneExpenseLine.findViewById(R.id.iv_tracker_icon);
        TextView tv_date = oneHistoryLine.findViewById(R.id.tv_historyDate);



        Expense2 p = this.getItem(position);

        tv_expenseName.setText(p.getExpenseName());
        tv_amount.setText(Integer.toString(p.getAmount()));
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

        return oneHistoryLine;
    }


}
