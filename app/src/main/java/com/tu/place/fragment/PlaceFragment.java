package com.tu.place.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tu.place.R;
import com.tu.place.model.Place;

import java.text.DecimalFormat;

/**
 * Created by SEV_USER on 4/18/2017.
 */

public class PlaceFragment extends Fragment implements View.OnClickListener {
    private ImageView imPlace;
    private TextView tvContent;
    private TextView tvAddess;
    private TextView tvTitle;
    private TextView tvDistance;
    private ImageView imDirection;

    private Place place;

    public PlaceFragment() {
    }

    @SuppressLint("ValidFragment")
    public PlaceFragment(Place place) {
        this.place = place;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.ui_place,container,false);
        imPlace = (ImageView) rootView.findViewById(R.id.imPlace);
        tvContent = (TextView) rootView.findViewById(R.id.tvContent);
        tvAddess = (TextView) rootView.findViewById(R.id.tvAddress);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvDistance = (TextView) rootView.findViewById(R.id.tvDistance);
        setData(place);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //imDirection = (ImageView) getActivity().findViewById(R.id.imDirection);
        //imDirection.setOnClickListener(this);
    }


    public void setData(Place place) {
//        Picasso.with(getActivity()).load(place.getImg()).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imPlace);
        tvTitle.setText(place.getTitle());
        tvAddess.setText(place.getAddress());
        tvContent.setText(place.getContent());
        DecimalFormat f = new DecimalFormat("##.00");
//        tvDistance.setText(f.format(place.getDistance())+" km");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.imDirection:
                //break;
        }
    }
}
