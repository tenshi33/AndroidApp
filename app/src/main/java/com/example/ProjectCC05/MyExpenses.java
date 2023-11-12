package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MyExpenses {
    private List<Expense> myExpenseList;

    public MyExpenses(List<Expense> myExpenseList) {
        this.myExpenseList = myExpenseList;
    }

    public MyExpenses(){
        this.myExpenseList = new ArrayList<>();
    }

    public List<Expense> getMyExpenseList() {
        return myExpenseList;
    }

    public void setMyExpenseList(List<Expense> myExpenseList) {
        this.myExpenseList = myExpenseList;
    }


}
