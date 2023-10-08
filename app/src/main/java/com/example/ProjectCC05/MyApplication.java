package com.example.ProjectCC05;

import android.app.Application;

public class MyApplication extends Application {

    private MyExpenses myExpense = new MyExpenses();

     public MyExpenses getMyExpense() {
        return myExpense;
    }

    public void setMyExpense(MyExpenses myExpense) {
        this.myExpense = myExpense;
    }

}
