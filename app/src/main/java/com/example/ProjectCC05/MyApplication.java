package com.example.ProjectCC05;

import android.app.Application;

public class MyApplication extends Application {

    private MyExpenses myExpense = new MyExpenses();
    private MyExpenses2 myExpenses2 = new MyExpenses2();

     public MyExpenses getMyExpense() {
        return myExpense;
    }

    public MyExpenses2 getMyExpenses2() {
         return myExpenses2;
    }

    public void setMyExpense(MyExpenses myExpense) {
        this.myExpense = myExpense;
    }

}
