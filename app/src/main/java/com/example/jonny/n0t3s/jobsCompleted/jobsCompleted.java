package com.example.jonny.n0t3s.jobsCompleted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.jonny.n0t3s.Month;
import com.example.jonny.n0t3s.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class jobsCompleted extends AppCompatActivity {

    String[] colorArray;
    List<Integer> colors;
    FirebaseFirestore myCollection =  FirebaseFirestore.getInstance();
    FirebaseAuth mainUser;
    FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_completed);







        monthlyGraphView();

    }


    public List<Integer> returnColor(){

        colorArray = getApplicationContext().getResources().getStringArray(R.array.colors);

        colors = new ArrayList<Integer>();
        for (int i = 0; i < colorArray.length; i++) {
            int newColor = Color.parseColor(colorArray[i]);

            colors.add(newColor);

        }


        return colors;
    }


    public void monthlyGraphView(){


        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();



        myCollection.collection("CompletedJobs"+myUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {




                //set barchart view and declare array list to store jobCount data

                BarChart barChart = (BarChart) findViewById(R.id.barchart);

                ArrayList<Month> myData = new ArrayList<>();






                //Bar Entry Array List to handle data population by index,
                //populated area comes from the path that stores job count as well montName
                ArrayList<BarEntry> monthData = new ArrayList<>();

                for(DocumentSnapshot myDoc: task.getResult()){
                    Month myMonthCount = myDoc.toObject(Month.class);
                    //myAgreements.setMessageNoti((String)myDoc.get("ownerEmail"));
                    assert myMonthCount != null;
                    myMonthCount.setJobCompCount((String) myDoc.get("jobCompCount"));


                    Log.e( myMonthCount.getJobCompCount(),"name ");
                    String cVal = myMonthCount.getJobCompCount();

                    float cValue = Integer.parseInt(cVal);
                    myData.add(myMonthCount);

                    monthData.add(new BarEntry(cValue, myData.indexOf(myMonthCount)));
                   //BarDataSet bardataset = new BarDataSet(monthData, "Months");

                }




                //setting our data by index into the bar
                BarDataSet bardataset = new BarDataSet(monthData, "Jobs Completed");


                //arraylist to store monthName as strings
                ArrayList<String> monthName = new ArrayList<String>();

                //ArrayList<Month> monthNames = new ArrayList<>();
                for(DocumentSnapshot myDoc: task.getResult()){
                    Month myMonthName = myDoc.toObject(Month.class);

                    //we get the monthName field from our data path
                    myMonthName.setMonthName((String) myDoc.get("monthName"));



                    Log.e( myMonthName.getMonthName(),"name ");
                    //String cVal = myMonthName.getMonth();


                    //monthNames.add(myMonthName);


                    //we add the monthName to arraylist to label the graph with its repective data
                    //pupulated bar by index
                    monthName.add(myMonthName.getMonthName());


                }



                // set the labels per our bar data by index
                //we set the Label of the Graph and
                //we also set the color for our graph based on an List populated with colors from xml file
                //we animate the Y axis population, for bar graph data to look like it grows when view is created
                //or refreshed
                BarData data = new BarData(monthName, bardataset);
                barChart.setData(data);
                barChart.setDescription("Monthly Completed Jobs");
                bardataset.setColors(returnColor());
                barChart.animateY(5500);

            }
        });
    }




}
