package com.example.ProjectCC05;

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

public class HistoryAdapter extends BaseAdapter {

    // The same as ExpenseAdapter. The custom adapter for the History
    Activity mActivity;
    MyHistoryExpenses myHistoryExpenses;

    public HistoryAdapter(Activity mActivity, MyHistoryExpenses myHistoryExpenses) {
        this.mActivity = mActivity;
        this.myHistoryExpenses = myHistoryExpenses;
    }

    @Override
    public int getCount() {
        return myHistoryExpenses.getMyHistoryList().size();
    }

    @Override
    public History getItem(int position) {
        return myHistoryExpenses.getMyHistoryList().get(position);

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
        View oneHistoryLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneHistoryLine = inflater.inflate(R.layout.history_one_line, vG, false);

        TextView tv_expenseName = oneHistoryLine.findViewById(R.id.tv_historyExpenseName);
        TextView tv_amount = oneHistoryLine.findViewById(R.id.tv_historyAmountValue);
        TextView tv_date = oneHistoryLine.findViewById(R.id.tv_historyDate);

        History currentHistory = getItem(position);

        tv_amount.setText(String.valueOf(currentHistory.getAmount()));
        if (currentHistory.getAmount() >= 0) {
            tv_amount.setTextColor(Color.parseColor("#85BB65"));
        } else {
            tv_amount.setTextColor(Color.parseColor("#A91B0D"));
        }

        History p = this.getItem(position);

        // For setting the decimal format with the currency sign
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("₱");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##", symbols);
        String formattedAmount = "₱ " + decimalFormat.format(p.getAmount());

        tv_expenseName.setText(p.getExpenseName());
        tv_amount.setText(formattedAmount);
        tv_date.setText(p.getDate());

        return oneHistoryLine;
    }


}
