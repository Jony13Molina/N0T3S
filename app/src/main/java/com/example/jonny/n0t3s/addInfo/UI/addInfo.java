package com.example.jonny.n0t3s.addInfo.UI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.addInfo.addInfoPresenterImp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class addInfo extends AppCompatActivity implements  View.OnClickListener, addInfoView {

    FloatingActionButton doneButton;
    FirebaseFirestore myCollection;
    Switch privateTrue;
    FirebaseUser mainUser;
    User user;
    public String myDetails;
    public String myTitle;
    public String myMoney;
    public String likeCount;
    public static String toastMessage;
    public static String pToastMessage;
    public static String privateMessage;



    private static final String TAG = "addInfo";
    public addInfoPresenterImp myPresenter;
    //variables to get the monthly value.
    int monthly;
    String date;



    Calendar calendar = Calendar.getInstance();
    int year;
    int month;
    int dayOfMonth;

    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        myPresenter = new addInfoPresenterImp(this);



       // dp = (DatePicker) findViewById(R.id.datePicker);


        //switch view
        privateTrue = (Switch) findViewById(R.id.privateSwitch);
        //button view
        doneButton= (FloatingActionButton) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);

        final EditText dateEdit = (EditText)findViewById(R.id.dateInfo);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(dateEdit);



            }
        });


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

        EditText m_Edit = (EditText)findViewById(R.id.moneyInfo);
        myMoney = m_Edit.getText().toString();

        EditText d_edit = (EditText) findViewById(R.id.dateInfo);
        date = d_edit.getText().toString();
        //initializing writing year taken
        //listDrop = (Spinner)findViewById(R.id.dropDownls);
        //set Year
        //myYear = listDrop.getSelectedItem().toString();
       // monthly = dp.getMonth()+1;
        //date = monthly+"/"+dp.getDayOfMonth()+"/"+dp.getYear();
        //dateText.setText(dateText.getText() + "" +monthly + "/" + dp.getDayOfMonth() + "/"+dp.getYear());
        //dp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //myYear = dp.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));



        //pushData(mainUser.getUid(), myTime,myUser, privateTrue);

        myPresenter.pushNotes(myTitle, myDetails, date, myMoney,privateTrue);

    }

    @Override
    public void pushData(String id, String timeStampMe, User myUser, Switch mySwitch) {
        myPresenter.pushData(id, timeStampMe, mySwitch);


    }

    public void setDate(final EditText dateText ){




        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateText.setText( (monthOfYear+1) +"-"+ dayOfMonth + "-" + year);


                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();




    }
}
