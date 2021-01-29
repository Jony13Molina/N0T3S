package com.example.jonny.n0t3s;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class reportUser extends AppCompatActivity implements View.OnClickListener {

    private TextView titleReport;

    double strikeNum = 0;
    EditText nameReport, detailsReport;

    FirebaseAuth mainUser = FirebaseAuth.getInstance();
    FirebaseUser fireUser = mainUser.getCurrentUser();
    FirebaseFirestore myCollection = FirebaseFirestore.getInstance();

    User myUser = new User();
    Button reportUser;

    ArrayList <String> reportValues = new ArrayList<String>();

    Map <String, Object > userReport = new HashMap<>();
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        titleReport = (TextView) findViewById(R.id.titleReport);
        titleReport.setText("Make Report");



        nameReport = (EditText) findViewById(R.id.nameInfo);
        detailsReport = (EditText)findViewById(R.id.detailsInfo);


        reportUser = (Button) findViewById(R.id.reportButton);

        reportUser.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        submitReport();
    }

    public void submitReport() {


        final String name = nameReport.getText().toString();


        final String details = detailsReport.getText().toString();


        if (nameReport.equals("") && detailsReport.equals("")) {

            Utils.toastMessage("Don't leave any fields blank", reportUser.this);


        } else {


            myCollection.collection("users").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                         //(double) document.get("strikeNum");

                        //(boolean)  document.get("reportState");

                        String userID = (String) document.get("userID");


                        Map<String, Object> reportData = document.getData();



                        strikeNum++;


                        for (Map.Entry<String, Object> e : reportData.entrySet()) {



                            if (reportData.containsKey(fireUser.getEmail())) {



                                Utils.toastMessage("You Have Already Reported This User", reportUser.this);


                            } else {


                                //if (strikeNum <= 5) {

                                    boolean reportState = true;
                                    reportData = document.getData();//new HashMap<>();

                                    userReport.put("name", name);
                                    userReport.put("details", details);
                                    userReport.put("reportState", reportState);


                                    reportData.put(fireUser.getEmail(), userReport);
                                    reportData.put("strikeNum", strikeNum);
                                    //if(!reportState) {

                                    Log.d("print mapa", reportData.toString());


                                    myCollection.collection("users").document(name).set(reportData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Utils.toastMessage("Report was sent", reportUser.this);
                                                    //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Utils.toastMessage("Error!!!" + e.toString(), reportUser.this);

                                                }
                                            });
                                    // Utils.toastMessage("You Have Already Reported This User", reportUser.this);

                                //}else {
                                    //we put the user in block list when strikeNum exceeds 5

                                    userReport.put("name", name);
                                    userReport.put("details", details);
                                    myCollection.collection("blockedUsers").document(userID).set(reportData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Utils.toastMessage("Report was sent", reportUser.this);
                                                    //Log.d("THIS IS THE TOKEN!!!!", user.getUserToken());


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Utils.toastMessage("Error!!!" + e.toString(), reportUser.this);

                                                }
                                            });
                                //}
                            }

                        }




                        //Log.d("print mapa1", reportData.toString());



                    }else{

                        Utils.toastMessage("User does not exist", reportUser.this);
                    }
                }

            });
        }
    }
}
