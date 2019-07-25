package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.example.jonny.n0t3s.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class viewInfo extends AppCompatActivity implements viewInfoView,
        RecyclerAdapter.RecyclerViewClickListener, RecyclerAdapter.RecyclerDeleteListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    FirebaseFirestore myData;
    FirebaseUser fireUser;
    viewPresentImp myPresenter;
    FirebaseFirestore myCollection;


    public List<User> userList;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);



        recyclerView = findViewById(R.id.recycler_view);
        myData = FirebaseFirestore.getInstance();
        //mainUser = FirebaseAuth.getInstance();
        fireUser = FirebaseAuth.getInstance().getCurrentUser();

        myPresenter = new viewPresentImp(this);
        myPresenter.getNoteData();



        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);

        //adapter = new RecyclerAdapter(this);
        //recyclerView.setAdapter(adapter);



    }


    @Override
    public void initDel(final int pos) {


        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure you want to delete this note?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myPresenter.startDelete(adapter.getUserNotes(), adapter.setUser(pos),adapter, pos);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
            }
        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


    @Override
    public void shareNotes(final int pos) {



        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Do you want to share this note?");
        alertDialogBuilder.setPositiveButton("Share",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        myPresenter.shareNote(adapter.setUser(pos));

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
            }
        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    @Override
    public void getDataNotes( ) {


        myCollection = myPresenter.getNoteData();
        myCollection.collection(fireUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            userList = new ArrayList<>();

                            for(DocumentSnapshot myDoc: task.getResult()){
                                User myUser = myDoc.toObject(User.class);
                                myUser.setUserID(myDoc.getId());
                                userList.add(myUser);

                                // adapter.setuserNotes(userList);
                            }

                            // adapter.setuserNotes(userList);
                            adapter = new RecyclerAdapter(userList, viewInfo.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            adapter.setCustomButtonListner(viewInfo.this);
                            adapter.setRecyclerDeleteListener(viewInfo.this);
                            recyclerView.setAdapter(adapter);
                        }else {
                            Log.d("TAG", "Error Getting Docs", task.getException());
                        }

                    }
                });

    }

    public void itemDelete(List<User>userNotes,int pos, RecyclerAdapter adapter, Context cont){

        userNotes.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, userNotes.size());
        Utils.toastMessage("Note was deleted", cont);
    }


    @Override
    public void onClick(View view, int position) {
        shareNotes(position);
    }

    @Override
    public void onMyClick(View v, int pos) {
        initDel(pos);
    }
}