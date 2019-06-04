package com.example.jonny.n0t3s;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    FirebaseFirestore myData;
    FirebaseAuth mainUser;
    FirebaseUser fireUser;
    ListenerRegistration firestoreUpdate;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerList);
        myData = FirebaseFirestore.getInstance();
        //mainUser = FirebaseAuth.getInstance();
        fireUser = FirebaseAuth.getInstance().getCurrentUser();

        getData();

        firestoreUpdate = myData.collection("Notes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }
                        List<User> userList = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            User myUserNotes = doc.toObject(User.class);
                            myUserNotes.setUserID(doc.getId());
                            userList.add(myUserNotes);
                        }

                        adapter = new RecyclerTwoAdapter(userList, MainActivity.this, myData);
                        recyclerView.setAdapter(adapter);
                    }
                });


        setNavigation();
    }
    @Override
    protected void onStart(){
        super.onStart();
        getData();
    }

    //get all data that was newly added or shared ti the public list


    @Override
    protected void onResume() {
        super.onResume();
        getData();


    }
    //stop the listener when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreUpdate.remove();
    }

    private void getData(){
        myData.collection("Notes")
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

                            adapter = new RecyclerTwoAdapter(userList, MainActivity.this, myData);
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


    public void setNavigation()
    {
        BottomNavigationView  bottomNav = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent next_activity = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_info:
                        //transition to input notes
                        next_activity = new Intent(MainActivity.this, addInfo.class);
                        startActivity(next_activity);
                        break;

                    case R.id.navigation_viewInfo:
                        //transition to Viewing info class
                        Intent intent = new Intent(MainActivity.this, viewInfo.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_exit:
                        //close app
                        finish();
                        System.exit(0);
                        break;


                }
                return true;
            }
        });
    }

}
