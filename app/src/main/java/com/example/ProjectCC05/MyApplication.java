package com.example.ProjectCC05;

import android.app.Application;

public class MyApplication extends Application {

    private MyExpenses myExpense = new MyExpenses();
    private MyHistoryExpenses myHistoryExpenses = new MyHistoryExpenses();

     public MyExpenses getMyExpense() {
        return myExpense;
    }

    public MyHistoryExpenses getMyExpenses2() {
         return myHistoryExpenses;
    }

    public void setMyExpense(MyExpenses myExpense) {
        this.myExpense = myExpense;
    }

}
