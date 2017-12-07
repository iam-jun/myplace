package com.tu.place.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tu.place.R;
import com.tu.place.model.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class AdapterFriend extends BaseAdapter{
    private ArrayList<Friend> arrFriend;

    public AdapterFriend(Context context, ArrayList<Friend> arrFriend) {
        super(context, arrFriend);
        this.arrFriend = arrFriend;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        v = super.getView(position, v, parent);
        ImageView imAvatar = (ImageView) v.findViewById(R.id.imFriendAvatar);
        TextView tvName = (TextView) v.findViewById(R.id.tvFriendName);
        Friend friend = arrFriend.get(position);
        tvName.setText(friend.getName());
        Glide.with(v.getContext()).load(friend.getAvatar()).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imAvatar);
        return v;
    }

    @Override
    protected int animationId() {
        return R.anim.translate;
    }

    @Override
    protected int getIdItem() {
        return R.layout.item_friend;
    }
}
