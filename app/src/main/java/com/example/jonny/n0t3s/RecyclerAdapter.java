package com.example.jonny.n0t3s;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    public List<User> userNotes;
    private Context context;
    List<User> userInfo;
    FirebaseFirestore myCollection;



    DocumentReference userData;
    User user;
    public static int atAdapter;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public RecyclerAdapter(List<User>userNotes, Context cont, FirebaseFirestore myData){
        this.userNotes = userNotes;
        this.context = cont;
        this.myCollection = myData;

    }




    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView itemTitle, itemDesc, itemYears;

        public ViewHolder( View itemView ) {
            super(itemView);
            //instiate our views
            itemTitle = (TextView) itemView.findViewById(R.id.item_info);
            itemDesc = (TextView) itemView.findViewById(R.id.itemDesc);
            itemYears = (TextView) itemView.findViewById(R.id.itemYear);
            //atAdapter = getAdapterPosition();

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    atAdapter = getAdapterPosition();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                    alertDialogBuilder.setTitle("Do you want to delete this item?....");
                    alertDialogBuilder.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {

                                   // deleteItem(atAdapter);
                                    //writeData.remove(String.valueOf(atAdapter));


                                    //notifyItemRemoved(atAdapter);
                                    //notifyItemRangeChanged(atAdapter, writeData.size());
                                }



                            });

                    alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });*/

        }



    }






    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        //ViewHolder viewHolder = new ViewHolder(v);


        return new RecyclerAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final int itemListPos = i;
        final User user = userNotes.get(itemListPos);


        viewHolder.itemTitle.setText(user.getTitle());
        viewHolder.itemDesc.setText(user.getDetails());
        viewHolder.itemYears.setText(user.getYear());



    }






    @Override
    public int getItemCount() {
        return userNotes.size();
    }








}