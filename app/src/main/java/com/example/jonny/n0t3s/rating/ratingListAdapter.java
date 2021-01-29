package com.example.jonny.n0t3s.rating;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jonny.n0t3s.Notification;
import com.example.jonny.n0t3s.R;

import java.util.ArrayList;
import java.util.List;

public class ratingListAdapter extends ArrayAdapter<Notification> implements Filterable{







    public List<Notification> applicantList;
    private List<Notification> filterRatingL;
    private Context adapterCont;

    RatingFilter myRating;
    TextView userName,
            userRating,
            ratingIcon;







    Notification myNotification;





    public ratingListAdapter(@NonNull Context context, List<Notification> myApplicants) {
        super(context,0, myApplicants);

        this.adapterCont = context;
        this.applicantList = myApplicants;
        this.filterRatingL = myApplicants;

        getFilter();

    }


    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ratingitemslist, parent, false
            );
        }

        final Notification currentApplicant = getItem(position);

        //instiate our views

        userName = (TextView)listItemView.findViewById(R.id.userEmail);
        userRating = (TextView)listItemView.findViewById(R.id.ratingNum);
        ratingIcon = (TextView)listItemView.findViewById(R.id.ratingIcon);


        userName.setText(currentApplicant.getOwnerEmail());
        userRating.setText(currentApplicant.getRatingValue().toString());


        return listItemView;


    }


    public List<Notification> getApplicantList(){
        return applicantList;
    }

    public Notification setNoti(int pos){
        myNotification = applicantList.get(pos);
        return myNotification;
    }

    @Override
    public int getCount() {
        return filterRatingL.size();
    }

    @Override
    public Notification getItem(int i) {
        return filterRatingL.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        if (myRating == null) {
        myRating = new RatingFilter();

        }

        return myRating;
    }



/**********ratingfilter class ********************/

    private class RatingFilter extends Filter {



            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null && constraint.length()>0) {
                    ArrayList<Notification> tempList = new ArrayList<Notification>();

                    // search content i friend list
                    for (Notification rating : applicantList) {
                        Log.d("this is search passed", constraint.toString().toLowerCase());
                        if (rating.getOwnerEmail().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            tempList.add(rating);

                            Log.d("this is the value", rating.toString().toLowerCase());

                        }


                    }



                    Log.d("you dont have anything", constraint.toString().toLowerCase());


                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                } else {
                    Log.d(" empty string boah", constraint.toString().toLowerCase());

                    filterResults.count = applicantList.size();
                    filterResults.values = applicantList;
                }

                return filterResults;
            }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filterRatingL= (List<Notification>) results.values;
            notifyDataSetChanged();
        }
    }






}






