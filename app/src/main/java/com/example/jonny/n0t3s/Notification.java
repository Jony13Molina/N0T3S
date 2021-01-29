package com.example.jonny.n0t3s;

public class Notification extends User {

    String senderNoti, messageNoti, token, timeStamp, email, ownerEmail, receivedMsg, sentMsg;
    String ownerName, receiverName;
    Double ratingValue;

    boolean buttonState;


    public Notification(){

    }

    public Notification(String senderN, String messageN,String timeStamp, String email,
                        String ownerEmail, Double ratingValue,boolean buttonState){
        this.senderNoti = senderN;
        this.messageNoti = messageN;
        this.timeStamp = timeStamp;
        this.email = email;
        this.ownerEmail = ownerEmail;
        this.ratingValue = ratingValue;
        this.buttonState = buttonState;
    }

    public void setReceivedMsg(String receivedMsg){

        this.receivedMsg = receivedMsg;

    }

    public void setOwnerName(String ownerName){
            this.ownerName = ownerName;
    }
    public void setReceiverName(String receiverName){
        this.receiverName = receiverName;
    }

    public void setSentMsg(String sentMsg){
        this.sentMsg = sentMsg;
    }
    public void setButtonState(boolean buttonState) {
        this.buttonState = buttonState;
    }

    public void setMessageNoti(String messageN){
        this.messageNoti = messageN;
    }

    public void setSenderNoti(String senderN){
        this.senderNoti = senderN;
    }


    public void setRatingValue(Double ratingvalue){
        this.ratingValue = ratingvalue;

    }

    public void setOwnerEmail(String email){
        this.ownerEmail=email;
    }
    public void setSenderEmail(String email){
        this.email = email;

    }
    public void  setToken(String myToken){
        this.token = myToken;
    }
    public String getToken(){
        return token;
    }


    public void  setTimeStamp(String timeStamp){
        this.timeStamp=timeStamp;

    }

    //getters
    public String getOwnerEmail(){
        return this.ownerEmail;
    }
    public String getSenderEmail(){
        return this.email;
    }
    public  String getTimeStamp(){
        return this.timeStamp;
    }

    public Double getRatingValue(){
        return this.ratingValue;
    }
    public String getMessageNoti(){
        return messageNoti;
    }


   public  String getSenderNoti(){
        return senderNoti;
    }
    public boolean getButtonState(){
        return this.buttonState;
    }

    public String getReceivedMsg() {
        return this.receivedMsg;
    }

    public String getSentMsg(){
        return this.sentMsg;
    }

    public String getReceiverName(){
        return this.receiverName;
    }
    public String getOwnerName(){return this.ownerName;}
}
