package com.example.jonny.n0t3s.tabView;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.Utils;
import com.example.jonny.n0t3s.tabView.applicants.applicantAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyTabAdapter extends Fragment implements applicantAdapter.recyclerDeny, applicantAdapter.recyclerAccept {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    applicantAdapter adapter;

    List<Notification>myAppicants;
    FirebaseAuth mainUser;
    FirebaseFirestore myData = FirebaseFirestore.getInstance();
    ListView myApplicants;
    Notification myNoti = new Notification();
    FirebaseUser myUser;

    int pos;
    int numOfTabs;
    SharedPreferences mSharedPref;
    public String myTime;
    //MainRepositoryImp myMainRe = new MainRepositoryImp(getContext());
    private static final String sharedPref = "time";





    private int[] COLOR_MAP = {
            R.color.softeRealBlue,R.color.softGold,R.color.softeRealBlue
    };
    public MyTabAdapter() {
        // Required empty public constructor
    }
    public static MyTabAdapter newInstance(Integer counter) {
        MyTabAdapter fragment = new MyTabAdapter();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);

            Utils.toastMessage(String.valueOf(counter),getContext());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        switch (getArguments().getInt(ARG_COUNT)){
            case 0:
                return inflater.inflate(R.layout.view_pager_text, container, false);

            case 1:
                return inflater.inflate(R.layout.applications_xmlfragment, container, false);
            case 2:
                return inflater.inflate(R.layout.messages_xmlfragment, container, false);


        }
        return null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));


        switch (getArguments().getInt(ARG_COUNT)){
            case 0:

               //TextView textViewCounter = view.findViewById(R.id.tabText);
                //textViewCounter.setText( String.valueOf(counter));


                myApplicants= (ListView)getView().findViewById(R.id.applicantsView);

//                myNoti = adapter.applicantList.get(pos);
                getData();

                break;
            case 1:
                //TextView textApplications = view.findViewById(R.id.applicationsID);
               // textApplications.setText( String.valueOf(counter));
                break;


            case 2:
               // TextView textMessages = view.findViewById(R.id.messagesID);
                //textMessages.setText( String.valueOf(counter));
                break;



        }




    }


    //get the data from database to populate our list
    public void getData(){
        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();

        //String mydata;
        myData.collection(myUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification>  userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myApplicants = myDoc.toObject(Notification.class);
                                myApplicants.setMessageNoti((String)myDoc.get("email"));
                                myApplicants.setSenderNoti((String) myDoc.get("from"));

                                adapter = new applicantAdapter(getContext(), userList);

                                userList.add(myApplicants);

                            }


                            if (myAppicants == null) {

                                return;
                            }
                            myApplicants.setAdapter(adapter);
                            adapter.setRecycleraccept(MyTabAdapter.this);
                            adapter.setRecyclerDeny(MyTabAdapter.this);



                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }

                });










    }

    //method when the user deny applicnt
    public void denyApplicant(final int position){

        myNoti = adapter.setNoti(position);

        mainUser = FirebaseAuth.getInstance();

        myUser = mainUser.getCurrentUser();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Do you want to turn down the applicant");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        myData.collection(myUser.getEmail()).document(
                                myNoti.getSenderEmail()+ myNoti.getTimeStamp()).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        completeDeny( position);
                                    }
                                });
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void completeDeny( int pos){
        //applicantList.remove(pos);
        adapter.getItem(pos);
        adapter.remove(adapter.getItem(pos));
        adapter.notifyDataSetChanged();
        Utils.toastMessage("Applicant was removed", getContext());
    }



    //onClick methods for the actions on the notes card
    //specifically accept and deny
    @Override
    public void onClick(View view, int position) {
        //setLike(position);


        //acceptApplicant(position);


    }

    @Override
    public void onMyClick(View v, int pos) {
        //itemDelete(pos);

        denyApplicant(pos);

    }








}
