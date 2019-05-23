package com.example.jonny.n0t3s;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.renderscript.Script;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import static io.opencensus.tags.TagValue.MAX_LENGTH;

public class addInfo extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton doneButton;
    FirebaseFirestore myCollection;

    FirebaseUser mainUser;
    User user;
    public String myDetails;
    public String myTitle;
    public String myTime;
    //variables to get the monthly value.
    int monthly;
    String date;


    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH)+1;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        //get a reference of our database
        myCollection = FirebaseFirestore.getInstance();



        dp = (DatePicker) findViewById(R.id.datePicker);



        //button view
        doneButton= (FloatingActionButton) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }


   public void onClick(View v)
   {
       user = new User();
        if (v.getId() == R.id.doneButton)
        {

                //set descriotion
                EditText t_Edit = (EditText)findViewById(R.id.enterInfo);

                myTitle = t_Edit.getText().toString();
                //set details
                EditText p_Edit  = (EditText)findViewById(R.id.plusInfo);
                myDetails = p_Edit.getText().toString();
                //initializing writing year taken
                //listDrop = (Spinner)findViewById(R.id.dropDownls);

                //set Year
                //myYear = listDrop.getSelectedItem().toString();
               monthly = dp.getMonth()+1;
               date = monthly+"/"+dp.getDayOfMonth()+"/"+dp.getYear();
                //dateText.setText(dateText.getText() + "" +monthly + "/" + dp.getDayOfMonth() + "/"+dp.getYear());
                dp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //myYear = dp.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

               //set User values
                user.setTitle(myTitle);
                user.setDetails(myDetails);

                user.setYear(date);

                user.settimeStampMe(timeStampMe());
                myTime = user.gettimeStampMe();
                mainUser = FirebaseAuth.getInstance().getCurrentUser();
                user.setEma(mainUser.getEmail());
                //writing to database photo name, photographer, and year taken
                if( !myTitle.equals("") && !myDetails.equals("")){
                    Map <String, Object> notes = new HashMap<>();
                    //notes.put("userID",mainUser.getUid());

                    notes.put("title", user.getTitle());

                    notes.put("details", user.getDetails());
                    notes.put("year", user.getYear());
                    notes.put("ema", user.getEma());
                    notes.put("timeStampMe",myTime);


                    user.setUserID(mainUser.getUid());

                    myCollection.collection(user.getUserID()).document(user.getUserID()+user.gettimeStampMe()).set(notes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addInfo.this, "Special Notes Saved!", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addInfo.this, "ERROR" + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("TAG", e.toString());

                                }
                            });

                }else{
                    Toast.makeText(this, "Please don't live any fields blank",Toast.LENGTH_LONG).show();
                }






        }
    }


    //timeStampMe method
    public static String timeStampMe() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();


        return ts;
    }

}
