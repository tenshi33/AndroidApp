package com.example.ProjectCC05;

import androidx.annotation.NonNull;

public class Expense implements Comparable<Expense> {

    private String expenseName;
    private int amount;
    private int pictureNumber;

    public Expense(String expenseName, int amount, int pictureNumber) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.pictureNumber = pictureNumber;

    }

    // compareTo for sorting
    @Override
    public int compareTo(@NonNull Expense other) {

        return this.expenseName.compareTo(other.expenseName);
    }


    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPictureNumber() {
        return pictureNumber;
    }

    public void setPictureNumber(int pictureNumber) {
        this.pictureNumber = pictureNumber;
    }

}


