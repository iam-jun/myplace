package com.tu.place.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ui_place,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        imPlace = (ImageView) getActivity().findViewById(R.id.imPlace);
        tvContent = (TextView) getActivity().findViewById(R.id.tvContent);
        tvAddess = (TextView) getActivity().findViewById(R.id.tvAddress);
        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvDistance = (TextView) getActivity().findViewById(R.id.tvDistance);
        //imDirection = (ImageView) getActivity().findViewById(R.id.imDirection);
        //imDirection.setOnClickListener(this);
    }


    public void setData(Place place) {
        Glide.with(getActivity()).load(place.getImg()).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imPlace);
        tvTitle.setText(place.getTitle());
        tvAddess.setText(place.getAddress());
        tvContent.setText(place.getContent());
        DecimalFormat f = new DecimalFormat("##.00");
        tvDistance.setText(f.format(place.getDistance())+" km");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.imDirection:
                //break;
        }
    }
}
