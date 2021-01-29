package com.example.jonny.n0t3s;

/**
 * Created by jonny on 2/15/2018.
 */

public class User {
    //global variables for helper class
    public String userID;
    public String name;
    public String receiverName;
    public String title;
    public String details;
    public String ema;
    public String year;
    public String timeStampMe;
    public String likeCounter = null;
    public boolean userLike = false;

    public double strikeNum;
    public String money;

    public boolean reportState = false;
    public String detailsReport;

    //constructors
    public User(String userID, String timeStampMe, String details, String title, String email,
                String year, String money, String likeCounter, double strikeNum, String detailsReport) {
        this.userID = userID;
        this.timeStampMe = timeStampMe;
        this.details = details;
        this.ema = email;
        this.title = title;
        this.year = year;
        this.money = money;

        this.strikeNum = strikeNum;
        this.likeCounter = likeCounter;
        this.detailsReport = detailsReport;


    }

    public User(String title, String details, String year, String timeStampMe, String likeCounter,
                String ema, boolean reportState) {
        this.title = title;
        this.details = details;
        this.year = year;
        this.timeStampMe = timeStampMe;
        this.likeCounter = likeCounter;
        this.ema = ema;
        this.reportState = reportState;
    }

    public User(String userid) {
        this.userID = userid;

    }

    public User() {

    }

    //setters
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void settimeStampMe(String timeStampMe) {
        this.timeStampMe = timeStampMe;

    }

    public void setStrikeNum(double strikeNumber) {

        this.strikeNum = strikeNumber;
    }

    public void setMoneyAmount(String money) {
        this.money = money;

    }

    public void setDetailsReport(String detailsReport) {
        this.detailsReport =detailsReport;

    }


    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setLikeCounter(String likeCounter){
        this.likeCounter =likeCounter;
    }

    public void setReportState(boolean reportState){

        this.reportState = reportState;
    }
    public void setEma(String email){
        this.ema = email;
    }
    public void setYear(String year){
        this.year =year;
    }
    public void setUserLike(boolean userlike){
        this.userLike = userlike;
    }

    //getters
    public String getUserID()
    {
        return this.userID;
    }
    public String getName() {
        return this.name;
    }

    public String getReceiverName(){
        return this.receiverName;
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
    public String getLikeCounter(){return this.likeCounter;}

    public double getStrikeNum(){
        return this.strikeNum;
    }
    public boolean getUserLike() {
        return userLike;
    }

    public String getDetailsReport(){
        return detailsReport;
    }


    public String getMoneyAmount(){return this.money;}

    public boolean getReportState(){
        return this.reportState;
    }
}
