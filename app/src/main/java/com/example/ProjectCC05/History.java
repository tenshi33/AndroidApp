package com.example.ProjectCC05;

public class History {

      /*
        This is the class that contains all variables in the lv_historyOfExpenses list. Namely, the expenseName,
        amount, date, and setReminder. Removing one will cause an error, and adding one will
        require lots of changes.
     */

    private String expenseName;
    private float amount;
    public String getDate() {
        return date;
    }

    private String date;

    public History(String expenseName, float amount, String date) {
         this.expenseName = expenseName;
         this.amount = amount;
         this.date = date;

    }

    // compareTo for sorting
    public int compareTo(History history) {
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

    public void setDate(String date) {
        this.date = date;
    }


}


