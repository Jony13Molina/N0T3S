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
    public String userEmail;
    public String year;

    //constructors
    public User(String userID, String name, String details, String title, String userEmail, String year)
    {
        this.userID = userID;
        this.name = name;
        this.details= details;
        this.userEmail = userEmail;
        this.title = title;
        this.year = year;

    }
    public User(String title, String details, String year){
        this.title = title;
        this.details = details;
        this.year = year;
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

    public void setUserEmail(String email){
        this.userEmail = email;
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
    public String getUserEmail(){
        return this.userEmail;
    }

}
