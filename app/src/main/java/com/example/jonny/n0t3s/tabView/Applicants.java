package com.example.jonny.n0t3s.tabView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Applicants extends Fragment {

    applicantAdapter adapter;

    FirebaseFirestore myData;

    ListView myApplicants;
        public Applicants() {
            // Required empty public constructor
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            myApplicants= (ListView)getView().findViewById(R.id.applicantsView);

            getData();
            return inflater.inflate(R.layout.view_pager_text, container, false);


        }

    //get the data from database to populate our list
    private void getData(){
        myData.collection("Notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Notification> userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                Notification myUser = myDoc.toObject(Notification.class);
                                myUser.setSenderNoti(myDoc.getId());
                                userList.add(myUser);

                            }

                            adapter = new applicantAdapter(getContext(), userList);
                            myApplicants.setAdapter(adapter);
                            //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                           // adapter.setRecyclerButton(MainActivity.this);
                            //adapter.setRecyclerDeleteListener(MainActivity.this);
                            //recyclerView.setLayoutManager(mLayoutManager);
                            //recyclerView.setItemAnimator(new DefaultItemAnimator());
                            //recyclerView.setAdapter(adapter);
                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }
                });

        }
    }

