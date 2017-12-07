package com.tu.place.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tu.place.R;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public abstract class BaseAdapter extends android.widget.BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<?> arrData;

    public BaseAdapter(Context context, ArrayList<?> arrData) {
        this.inflater = LayoutInflater.from(context);
        this.arrData = arrData;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null){
            v = inflater.inflate(getIdItem(),parent,false);
            Animation animation = AnimationUtils.loadAnimation(v.getContext(),animationId());
            long second = animation.getDuration();
            animation.setDuration(position * 100 + second);
            v.startAnimation(animation);
        }
        return v;
    }

    protected abstract int animationId();

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return arrData.get(i);
    }

    protected abstract int getIdItem();
}
