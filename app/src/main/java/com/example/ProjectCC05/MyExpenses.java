package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MyExpenses {
    List<Expense> myExpenseList;

    public MyExpenses(List<Expense> myExpenseList) {
        this.myExpenseList = myExpenseList;
    }

    public MyExpenses(){
        String[] startingExpenses = {"Water Bill", "Electric Bill", "Tuition"};

        this.myExpenseList = new ArrayList<>();

        Random rand = new Random();
        for (int i = 0; i<startingExpenses.length; i++){
            Expense p = new Expense(startingExpenses[i], rand.nextInt(1500) + 100, rand.nextInt(50));
            myExpenseList.add(p);
        }

    }

    public List<Expense> getMyFriendsList() {
        return myExpenseList;
    }

    public void setMyExpenseList(List<Expense> myExpenseList) {
        this.myExpenseList = myExpenseList;
    }
}
