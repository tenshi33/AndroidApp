package com.example.ProjectCC05;

import java.util.ArrayList;
import java.util.List;


public class MyHistoryExpenses {

    // The list for the HistoryPage. See MyExpenses.class for more information
    List<History> myHistoryList;

    public MyHistoryExpenses(List<History> myHistoryList) {
        this.myHistoryList = myHistoryList;
    }

    public MyHistoryExpenses(){
        this.myHistoryList = new ArrayList<>();
    }

    public List<History> getMyHistoryList() {
        return myHistoryList;
    }

    public void setMyExpenseList(List<History> myHistoryList) {
        this.myHistoryList = myHistoryList;
    }

}
