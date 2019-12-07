package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequestQueue {

   private Context myContext;

    private RequestQueue myRequest;
    private static MyRequestQueue queueInstance;
    public MyRequestQueue(Context base) {
        myContext = base;
        myRequest = getRequestQueue();
    }



    public static synchronized MyRequestQueue getInstance(Context context) {
        if (queueInstance == null) {
            queueInstance = new MyRequestQueue(context);
        }
        return queueInstance;
    }


    public RequestQueue getRequestQueue() {
        if (myRequest == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            myRequest = Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return myRequest;
    }

    public <T> void addToRequestQueue(Request<T> req) {

        getRequestQueue().add(req);
    }







}
