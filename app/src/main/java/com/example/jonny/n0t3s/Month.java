package com.example.jonny.n0t3s;

import java.util.ArrayList;

public class Month {


    public String monthName;
    public int count;
    String jobCompCount;
    public ArrayList<String> dayList;
    public  Month(String monthName, String jobCompCount){
        this.monthName = monthName;
        this.jobCompCount = jobCompCount;
    }

    public Month(){

    }
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setJobCompCount(String jobCompCount){
        this.jobCompCount = jobCompCount;
    }

    public String getMonthName(){
        return this.monthName;
    }

    public String getJobCompCount(){
        return this.jobCompCount;
    }
    public int getMonthCount(){
        return this.dayList.size();
    }
}
