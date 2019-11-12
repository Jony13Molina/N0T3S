package com.example.jonny.n0t3s.Main;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;

import com.example.jonny.n0t3s.BuildConfig;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import static android.content.ContentValues.TAG;

public class SendMessageHelperUtil extends ContextWrapper {


    final private String myServerKey = BuildConfig.ApiKey;
    final private String jsonContent = "application/json";
    final private String fcmSendAdress = BuildConfig.FcmAdress;

    Context myContext;
    public SendMessageHelperUtil(Context base) {
        super(base);
        myContext = base;
    }



}
