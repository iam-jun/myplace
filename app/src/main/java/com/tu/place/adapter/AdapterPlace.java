package com.tu.place.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tu.place.R;
import com.tu.place.model.Friend;
import com.tu.place.model.Place;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class AdapterPlace extends BaseAdapter{
    private ArrayList<Place> arrPalce;

    public AdapterPlace(Context context, ArrayList<Place> arrPalce) {
        super(context, arrPalce);
        this.arrPalce = arrPalce;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = super.getView(position, v, parent);
        ImageView imPlace = (ImageView) v.findViewById(R.id.imPlace);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) v.findViewById(R.id.tvContent);
        TextView tvAdress = (TextView) v.findViewById(R.id.tvAddress);
        TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
        Place place = arrPalce.get(position);
        tvTitle.setText(place.getTitle());
        tvContent.setText(place.getContent());
        tvAdress.setText(place.getAddress());
        tvAdress.setText(place.getDistance() + " km");
        Glide.with(v.getContext()).load(place.getImg()).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imPlace);
        return v;
    }

    @Override
    protected int animationId() {
        return R.anim.translate;
    }

    @Override
    protected int getIdItem() {
        return R.layout.item_place;
    }
}
