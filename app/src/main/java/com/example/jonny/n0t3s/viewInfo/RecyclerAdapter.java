package com.example.jonny.n0t3s.viewInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonny.n0t3s.R;
import com.example.jonny.n0t3s.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static com.example.jonny.n0t3s.R.id.deleteItem;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    public List<User> userNotes;
    User user;
    private Context myContext;
    FirebaseFirestore myCollection;
    FirebaseUser fireUser;
    String userPath;
    String likeCount;


    RecyclerViewClickListener mListener;
    RecyclerDeleteListener myListener;




    public RecyclerAdapter(Context context) {
        myContext = context;
    }

    public RecyclerAdapter(List<User>userNotes, Context context){
        this.userNotes = userNotes;
        this.myContext = context;

    }



    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }


    public interface RecyclerDeleteListener{
        void onMyClick(View v, int pos);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView itemTitle, itemDesc, itemYears, itemName, postBy;

        private ImageView deleteIcon, shareIcon, check;

        RecyclerViewClickListener mListener;


        public ViewHolder(View itemView) {
            super(itemView);
            //instiate our views
            itemTitle = (TextView) itemView.findViewById(R.id.item_info);
            itemDesc = (TextView) itemView.findViewById(R.id.itemDesc);
            itemYears = (TextView) itemView.findViewById(R.id.itemYear);
            itemName = (TextView) itemView.findViewById(R.id.itemNames);
            postBy = (TextView) itemView.findViewById(R.id.postBy);
            //instatiate our imageviews
            deleteIcon = (ImageView) itemView.findViewById(deleteItem);
            shareIcon = (ImageView) itemView.findViewById(R.id.shareItem);




        }



    }


    public void setCustomButtonListner(RecyclerViewClickListener listener) {
        this.mListener = listener;
    }


    public void setRecyclerDeleteListener(RecyclerDeleteListener listener){
        this.myListener = listener;
    }



    public void setuserNotes( List <User> myUser){
        this.userNotes = myUser;
    }

    public List <User> getUserNotes(){
        return userNotes;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        //ViewHolder viewHolder = new ViewHolder(v);


        return new RecyclerAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        final int itemListPos = i;
       // final User user = userNotes.get(itemListPos);
        setUser( itemListPos);
        int post = R.string.email_Address;


        viewHolder.itemTitle.setText(user.getTitle());
        viewHolder.itemDesc.setText(user.getDetails());
        viewHolder.itemYears.setText(user.getYear());
        viewHolder.itemName.setText(user.getEma());
        viewHolder.postBy.setText(post);



        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myListener != null){
                    myListener.onMyClick(v, viewHolder.getAdapterPosition());
                }

            }
        });


        viewHolder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onClick(v, viewHolder.getAdapterPosition());
                }

            }
        });





    }

    public User setUser( int pos){
        user = userNotes.get(pos);
        return user;
    }








    @Override
    public int getItemCount() {
        return userNotes.size();
    }






}