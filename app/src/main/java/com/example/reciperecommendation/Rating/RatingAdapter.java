package com.example.reciperecommendation.Rating;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.reciperecommendation.R;

import java.util.List;

public class RatingAdapter extends ArrayAdapter<RatingLaborModelClass> {
    Activity mcontext ;
    List<RatingLaborModelClass> ratingLaborModelClassList ;
    public RatingAdapter(Activity context, List<RatingLaborModelClass> artistList) {
        super(context, R.layout.rating_list , artistList);
        this.mcontext = context;
        this.ratingLaborModelClassList = artistList ;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mcontext.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.rating_list , null , true) ;

        TextView Comment = itemView.findViewById(R.id.viewcomment);
        TextView NameOf = itemView.findViewById(R.id.name)  ;
        RatingBar rating = itemView.findViewById(R.id.viewrat);

        RatingLaborModelClass  artist= ratingLaborModelClassList.get(position);

        NameOf.setText(artist.getName());
        Comment.setText(artist.getComment());
        rating.setRating(Float.valueOf(artist.getRating()));
        return  itemView ;
    }
}
