package com.tu.place.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tu.place.R;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class RegisterActivity extends NavigationActivity implements View.OnClickListener{
    private static final int KILL_SELF = 1;
    private Button btnRegister;
    private Dialog dialog;
    private LinearLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDialog();
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        layout = (LinearLayout) findViewById(R.id.registerPanel);
        Animation animation = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        animation.setDuration(1000);
        layout.startAnimation(animation);
    }

    private void initViews() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    private void initDialog() {
        dialog = new Dialog(this,android.R.style.Theme_Material_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_otp);
        EditText edtOtp = (EditText) dialog.findViewById(R.id.edtOtp);
        Button btnOtp = (Button) dialog.findViewById(R.id.btnOtp);
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishSelf();
            }
        });
        dialog.setCancelable(false);
    }

    private void finishSelf() {
        int duration = 1000;
        Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this,android.R.anim.slide_out_right);
        animation.setDuration(duration);
        handler.sendEmptyMessageDelayed(KILL_SELF,duration-100);
        layout.startAnimation(animation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                dialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishSelf();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == KILL_SELF){
                finish();
            }
        }
    };
}
