package com.tu.place.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.tu.place.R;
import com.tu.place.adapter.AdapterFriend;
import com.tu.place.model.Friend;

import java.util.ArrayList;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class FriendActivity extends NavigationActivity {
    private ListView lvFriend;
    private AdapterFriend adapterFriend;
    private ArrayList<Friend> arrFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initData();
        initViews();
    }

    private void initViews() {
        lvFriend = (ListView) findViewById(R.id.lvFriends);
        adapterFriend = new AdapterFriend(this,arrFriend);
        lvFriend.setAdapter(adapterFriend);
    }

    private void initData() {
        arrFriend = new ArrayList<>();
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","David"));
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","John"));
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","Ronaldo"));
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","Messi"));
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","David"));
        arrFriend.add(new Friend("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSaJr_PB8SeCQdPUdRPrIWL0tXMB2iaylzk6lxvA_4bEbQe_K10FA","John"));
    }
}
