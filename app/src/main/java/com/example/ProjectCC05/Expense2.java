package com.example.ProjectCC05;

public class Expense2 {

// History

    private String expenseName;
    private float amount;
    //private int pictureNumber2;

    public String getDate() {
        return date;
    }

    private String date;

    public Expense2(String expenseName, float amount, /* int pictureNumber, */ String date) {
         // super(expenseName, amount, date);
         this.expenseName = expenseName;
         this.amount = amount;
         //this.pictureNumber = pictureNumber;
         this.date = date;

    }


    // compareTo for sorting
    public int compareTo(Expense2 expense2) {
        return 0;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    /*
    public int getPictureNumber() {
        return pictureNumber2;
    }

    public void setPictureNumber(int pictureNumber2) {
        this.pictureNumber2 = pictureNumber2;
    }
    */

    public void setDate(String date) {
        this.date = date;
    }


}


