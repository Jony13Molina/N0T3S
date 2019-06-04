package com.example.jonny.n0t3s;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by jonny on 2/15/2018.
 */

public class User{
    //global variables for helper class
    public String userID;
    public String name;
    public String title;
    public String details;
    public String ema;
    public String year;
    public String timeStampMe;
    public boolean statusPrivate = false;

    //constructors
    public User(String userID, String timeStampMe, String details, String title, String email, String year)
    {
        this.userID = userID;
        this.timeStampMe = timeStampMe;
        this.details= details;
        this.ema = email;
        this.title = title;
        this.year = year;

    }
    public User(String title, String details, String year,String email, String timeStampMe){
        this.title = title;
        this.details = details;
        this.year = year;
        this.ema= email;
        this.timeStampMe = timeStampMe;
    }

    public User(String userid)
    {
        this.userID = userid;

    }
    public User()
    {

    }
    //setters
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setDetails(String details){
        this.details = details;
    }
    public void settimeStampMe(String timeStampMe){
        this.timeStampMe = timeStampMe;

    }

    public void setEma(String email){
        this.ema = email;
    }
    public void setYear(String year){
        this.year =year;
    }

    //getters
    public String getUserID()
    {
        return this.userID;
    }
    public String getName() {
        return this.name;
    }
    public String getTitle()
    {
        return this.title;
    }
    public String getDetails()
    {
        return this.details;
    }
    public String getYear(){
        return this.year;
    }
    public String getEma(){
        return this.ema;
    }
    public String gettimeStampMe(){
        return this.timeStampMe;
    }



}
