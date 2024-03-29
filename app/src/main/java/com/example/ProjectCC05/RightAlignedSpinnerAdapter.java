package com.example.ProjectCC05;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

    // Supposedly for aligning the spin_sign values into the right. But Changed it into the middle
public class RightAlignedSpinnerAdapter extends ArrayAdapter<CharSequence> {

    public RightAlignedSpinnerAdapter (Context context, int resource, CharSequence[] objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setGravity(Gravity.RIGHT);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setGravity(Gravity.RIGHT);
        textView.setPadding(82, 35, 8, 8);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return textView;
    }
}
