package com.example.jonny.n0t3s;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class viewInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    FirebaseFirestore myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);



        recyclerView = findViewById(R.id.recycler_view);
        myData = FirebaseFirestore.getInstance();
        getData();
        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);

        //adapter = new RecyclerAdapter(this);
        //recyclerView.setAdapter(adapter);



    }

    private void getData(){
        myData.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<User> userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                User myUser = myDoc.toObject(User.class);
                                myUser.setUserID(myDoc.getId());
                                userList.add(myUser);

                            }
                            adapter = new RecyclerAdapter(userList, getApplicationContext(), myData);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }
                });
    }



}