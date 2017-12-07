package com.tu.place.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tu.place.R;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class StartActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        initViews();
    }

    private void initViews() {
        AnimationDrawable drawable = (AnimationDrawable) ((ImageView)findViewById(R.id.imProcess)).getDrawable();
        drawable.start();
        handler.sendEmptyMessageDelayed(0,3000);

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
