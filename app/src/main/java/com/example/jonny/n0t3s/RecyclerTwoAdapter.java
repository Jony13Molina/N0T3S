package com.example.jonny.n0t3s;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerTwoAdapter extends RecyclerView.Adapter<RecyclerTwoAdapter.ViewHolder> {
    public List<User> userNotes;
    private Context myContext;
    List<User> userInfo;
    FirebaseFirestore myCollection;
    FirebaseUser fireUser= FirebaseAuth.getInstance().getCurrentUser();
    String userPath;
    String pathId;
    int likeCount;
    private boolean clicked = true;

    RecyclerLikeButton myLike;
    RecyclerDeleteButton myDelete;
    RecyclerTwoAdapter(Context context) {
        myContext = context;
    }

    User user;
    public RecyclerTwoAdapter(List<User> userNotes, Context context, FirebaseFirestore myData) {
        this.userNotes = userNotes;
        this.myContext = context;
        this.myCollection = myData;

    }



    public interface RecyclerLikeButton {
        void onClick(View view, int position);
    }


    public interface RecyclerDeleteButton{
        void onMyClick(View v, int pos);
    }


    public void setRecyclerButton(RecyclerLikeButton listener) {
        this.myLike = listener;
    }


    public void setRecyclerDeleteListener(RecyclerDeleteButton listener){
        this.myDelete = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView itemTitle, itemDesc, itemYears, itemName, postBy, likeC;

        private ImageView deleteIcon, likeIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            //instiate our views
            itemTitle = (TextView) itemView.findViewById(R.id.noTitle);
            itemDesc = (TextView) itemView.findViewById(R.id.noteDesc);
            itemYears = (TextView) itemView.findViewById(R.id.noteDate);
            itemName = (TextView) itemView.findViewById(R.id.noteNames);
            postBy = (TextView) itemView.findViewById(R.id.noteBy);
            //instatiate our imageviews
            deleteIcon = (ImageView) itemView.findViewById(R.id.delItem);
            likeIcon = (ImageView) itemView.findViewById(R.id.likeItem);
            likeC = (TextView) itemView.findViewById(R.id.likeCounter);


        }


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout_two, viewGroup, false);
        //ViewHolder viewHolder = new ViewHolder(v);


        return new RecyclerTwoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerTwoAdapter.ViewHolder viewHolder, int i) {

        final int itemListPos = i;
        final User user = userNotes.get(itemListPos);
        int post = R.string.email_Address;



        viewHolder.itemTitle.setText(user.getTitle());
        viewHolder.itemDesc.setText(user.getDetails());
        viewHolder.itemYears.setText(user.getYear());
        viewHolder.itemName.setText(user.getEma());
        viewHolder.postBy.setText(post);
        viewHolder.likeC.setText(user.getLikeCounter());

        //deleteicon listener deletes the data
        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDelete != null){
                    myDelete.onMyClick(v, viewHolder.getAdapterPosition());
                }


            }
        });


        //set like item code
        viewHolder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLike != null){
                    myLike.onClick(v, viewHolder.getAdapterPosition());
                }



            }


        });


    }

    public User setUser( int pos){
        user = userNotes.get(pos);
        return user;
    }
    public List <User> getUserNotes(){
        return userNotes;
    }

    @Override
    public int getItemCount() {
        return userNotes.size();
    }




}




