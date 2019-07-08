package com.example.jonny.n0t3s;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    //-------------------------------------------------------------------------------
    // this is a timestamp method to set a time to our notes for firestore reference
    //-------------------------------------------------------------------------------
    public static String timeStampMe() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();


        return ts;
    }

    //---------------------------------------------------------------------------------------
    //Utility function to make a toast message
    //---------------------------------------------------------------------------------------
    public static void toastMessage(String message, Context currentActivity)
    {
        Toast.makeText(currentActivity,message,Toast.LENGTH_SHORT).show();
    }


}
