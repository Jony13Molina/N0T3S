package com.example.jonny.n0t3s;

public class Notification {

    String senderNoti, messageNoti, token, timeStamp, email;



    public Notification(){

    }
    public Notification(String senderN, String messageN,String timeStamp, String email){
        this.senderNoti = senderN;
        this.messageNoti = messageN;
        this.timeStamp = timeStamp;
        this.email = email;
    }

    public void setMessageNoti(String messageN){
        this.messageNoti = messageN;
    }

    public void setSenderNoti(String senderN){
        this.senderNoti = senderN;
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

    public String getSenderEmail(){
        return this.email;
    }
    public  String getTimeStamp(){
        return this.timeStamp;
    }

    public String getMessageNoti(){
        return messageNoti;
    }


   public  String getSenderNoti(){
        return senderNoti;
    }
}
