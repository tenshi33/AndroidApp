package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MyExpenses2{

    // History
    List<Expense2> myExpenseList2;

    public MyExpenses2(List<Expense2> myExpenseList) {
        this.myExpenseList2 = myExpenseList2;
    }

    public MyExpenses2(){
        //String[] startingExpenses = {"Water Bill", "Electric Bill", "Tuition"};
        //String[] startingDate = {"01/02/2024", "01/03/2024", "01/04/2024"};

        this.myExpenseList2 = new ArrayList<>();

        /*
        Random rand = new Random();
        for (int i = 0; i<startingExpenses.length; i++){
            Expense2 p = new Expense2(startingExpenses[i], rand.nextInt(1500) + 100, /*rand.nextInt(50),/ startingDate[i]);
            myExpenseList2.add(p);
        }
        */
    }

    public List<Expense2> getMyExpenseList2() {
        return myExpenseList2;
    }

    public void setMyExpenseList(List<Expense2> myExpenseList2) {
        this.myExpenseList2 = myExpenseList2;
    }

}
