package com.example.ProjectCC05;

import androidx.annotation.NonNull;

public class Expense extends History implements Comparable<Expense> {

    private String expenseName;
    private float amount;
    private String date;
    private boolean setReminder;

    public Expense(String expenseName, float amount, String date) {
        super(expenseName, amount, date);
        this.expenseName = expenseName;
        this.amount = amount;
        this.date = date;
    }

    // compareTo for sorting
    @Override
    public int compareTo(@NonNull Expense other) {

        return this.expenseName.compareTo(other.expenseName);
    }

    public boolean isSetReminder() {
        return setReminder;
    }

    public void setSetReminder(boolean setReminder) {
        this.setReminder = setReminder;
    }
}


