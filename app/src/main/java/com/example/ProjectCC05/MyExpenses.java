package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MyExpenses {

    /*
        This is our list! We call this class to access the list. For example:
        MyExpenses myExpenses;
        myExpenses.getMyExpenseList()
        in here we can either add, remove, clear, etc items from the list.
        myExpenses.getMyExpenseList().add(); or
        myExpenses.getMyExpenseList().remove();
     */
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
