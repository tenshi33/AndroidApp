package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;


public class MyHistoryExpenses {

    // History
    List<History> myExpenseList2;

    public MyHistoryExpenses(List<History> myExpenseList) {
        this.myExpenseList2 = myExpenseList2;
    }

    public MyHistoryExpenses(){
        this.myExpenseList2 = new ArrayList<>();
    }

    public List<History> getMyExpenseList2() {
        return myExpenseList2;
    }

    public void setMyExpenseList(List<History> myExpenseList2) {
        this.myExpenseList2 = myExpenseList2;
    }

}
