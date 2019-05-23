package com.example.jonny.n0t3s;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.example.jonny.n0t3s.R.id.deleteItem;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    public List<User> userNotes;
    private Context myContext;
    List<User> userInfo;
    FirebaseFirestore myCollection;
    FirebaseUser fireUser;
    String userPath;






    public RecyclerAdapter(Context context) {
        myContext = context;
    }

    public RecyclerAdapter(List<User>userNotes, Context context, FirebaseFirestore myData){
        this.userNotes = userNotes;
        this.myContext = context;
        this.myCollection = myData;

    }




    class ViewHolder extends RecyclerView.ViewHolder  {


        private TextView itemTitle, itemDesc, itemYears,itemName, postBy;

        private ImageView deleteIcon, shareIcon;



        public ViewHolder( View itemView) {
            super(itemView);
            //instiate our views
            itemTitle = (TextView) itemView.findViewById(R.id.item_info);
            itemDesc  = (TextView) itemView.findViewById(R.id.itemDesc);
            itemYears = (TextView) itemView.findViewById(R.id.itemYear);
            itemName  = (TextView) itemView.findViewById(R.id.itemNames);
            postBy    = (TextView) itemView.findViewById(R.id.postBy);
            //instatiate our imageviews
            deleteIcon = (ImageView) itemView.findViewById(deleteItem);
            shareIcon =  (ImageView) itemView.findViewById(R.id.shareItem);





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
        int post = R.string.email_Address;



        viewHolder.itemTitle.setText(user.getTitle());
        viewHolder.itemDesc.setText(user.getDetails());
        viewHolder.itemYears.setText(user.getYear());
        viewHolder.itemName.setText(user.getEma());
        viewHolder.postBy.setText(post);

        //deleteicon listener deletes the data
        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
                alertDialogBuilder.setTitle("Are you sure you want to delete this note?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fireUser = FirebaseAuth.getInstance().getCurrentUser();
                                user.setUserID(fireUser.getUid());
                                userPath = user.getUserID()+user.gettimeStampMe();
                                itemDelete(user.getUserID(),userPath,itemListPos);

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
        });





    }







    @Override
    public int getItemCount() {
        return userNotes.size();
    }



    private void itemDelete(String id, String path, final int pos){
        myCollection = FirebaseFirestore.getInstance();

        myCollection.collection(id).document(path).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userNotes.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, userNotes.size());
                        Toast.makeText(myContext, "Note has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}