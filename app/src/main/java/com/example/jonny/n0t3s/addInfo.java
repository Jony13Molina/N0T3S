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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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

public class addInfo extends AppCompatActivity implements  View.OnClickListener, addInfoView {

    FloatingActionButton doneButton;
    FirebaseFirestore myCollection;
    Switch privateTrue;
    FirebaseUser mainUser;
    User user;
    public String myDetails;
    public String myTitle;
    public String myTime;
    public String likeCount;
    public static String toastMessage;
    public static String pToastMessage;
    public static String privateMessage;


    public addInfoPresenterImp myPresenter;
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

        myPresenter = new addInfoPresenterImp(this);



        dp = (DatePicker) findViewById(R.id.datePicker);


        //switch view
        privateTrue = (Switch) findViewById(R.id.privateSwitch);
        //button view
        doneButton= (FloatingActionButton) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }


   public void onClick(View v)
   {
        if (v.getId() == R.id.doneButton)
        {
            pushNotes();
        }
    }




    @Override
    public void pushNotes() {
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
        User user = new User ();
        //set User values
        user.setTitle(myTitle);
        user.setDetails(myDetails);

        user.setYear(date);
//
        user.settimeStampMe(Utils.timeStampMe());
        myTime = user.gettimeStampMe();
        mainUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setEma(mainUser.getEmail());

        user.setLikeCounter(likeCount);
        user.setUserLike(false);



        //pushData(mainUser.getUid(), myTime,myUser, privateTrue);

        myPresenter.pushNotes(user, privateTrue);

    }

    @Override
    public void pushData(String id, String timeStampMe, User myUser, Switch mySwitch) {
        myPresenter.pushData(id, timeStampMe, mySwitch);


    }
}
