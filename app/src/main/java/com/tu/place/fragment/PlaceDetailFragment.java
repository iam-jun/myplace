package com.tu.place.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.model.Place;


public class PlaceDetailFragment extends Fragment {

    private Place place;

    public PlaceDetailFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PlaceDetailFragment(Place place) {
        this.place = place;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    ImageView imPlace;
    TextView tvContent, tvAddess, tvTitle, tvDistance;
    RatingBar ratingBar;
    ImageButton btnDirection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_detail, container, false);
        imPlace = (ImageView) rootView.findViewById(R.id.imPlace);
        tvContent = (TextView) rootView.findViewById(R.id.tvContent);
        tvAddess = (TextView) rootView.findViewById(R.id.tvAddress);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvDistance = (TextView) rootView.findViewById(R.id.tvDistance);
        ratingBar = (RatingBar) rootView.findViewById(R.id.rating);
        btnDirection = (ImageButton) rootView.findViewById(R.id.ibtnDirection);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getFragmentManager().popBackStack();
                ((MainActivity)getActivity()).showDirection(place);
            }
        });
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);
        setData();
        return rootView;
    }

    private void setData(){
        tvTitle.setText(place.getTitle());
        tvAddess.setText(place.getAddress());
        tvContent.setText(place.getContent());
        ratingBar.setRating(Float.parseFloat(String.valueOf(place.getScore())));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).lnControl.setVisibility(View.GONE);
    }
}
