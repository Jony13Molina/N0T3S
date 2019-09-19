package com.example.jonny.n0t3s;

public class Notification {

    String senderNoti, messageNoti, token;



    public Notification(){

    }
    public Notification(String senderN, String messageN){
        this.senderNoti = senderN;
        this.messageNoti = messageN;
    }

    public void setMessageNoti(String messageN){
        this.messageNoti = messageN;
    }

    public void setSenderNoti(String senderN){
        this.senderNoti = senderN;
    }



    public void  setToken(String myToken){
        this.token = myToken;
    }
    public String getToken(){
        return token;
    }

    public String getMessageNoti(){
        return messageNoti;
    }


   public  String getSenderNoti(){
        return senderNoti;
    }
}
