package com.tu.place.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.tu.place.R;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.model.User;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int KILL_SELF = 1;
    private Button btnRegister;
    private EditText edtName, edtPhone, edtUserName, edtPassword, edtRePassword;
    private RadioGroup radioGender;
    private Dialog dialog;
    private LinearLayout layout;
    FirebaseManager firebaseManager;
    Dialog loadingDialog;
    private String TAG = "Register_Activity";
    private boolean isDupplicateAccount =false;
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
        edtName = (EditText) findViewById(R.id.edtRegisterName);
        edtPhone = (EditText) findViewById(R.id.edtRegisterPhone);
        edtUserName = (EditText) findViewById(R.id.edtRegisterUserName);
        edtPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRegisterRePassword);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        loadingDialog = AppDialogManager.createLoadingDialog(this);
        btnRegister.setOnClickListener(this);

        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkDupplicateAccount(edtUserName.getText().toString());

            }
        });
        firebaseManager = new FirebaseManager();
    }

    private void registerUser(){
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String usn = edtUserName.getText().toString();
        String pwd = edtPassword.getText().toString();
        String repwd = edtRePassword.getText().toString();
        Boolean gender = radioGender.indexOfChild(findViewById(radioGender.getCheckedRadioButtonId())) == 0;
        if(AppUtils.isNetworkAvailable(this)) {
            if(!isDupplicateAccount) {
                if (AppUtils.isEmptyString(name)) {
                    edtName.setError("Không được bỏ trống tên");
                } else if (AppUtils.isEmptyString(phone)) {
                    edtPhone.setError("Không được bỏ trống số diện thoại");
                } else if (AppUtils.isEmptyString(usn)) {
                    edtUserName.setError("Không được bỏ trống tên tài khoản");
                } else if (AppUtils.isEmptyString(pwd)) {
                    edtPassword.setError("Không được bỏ trống mật khẩu");
                } else if (!pwd.equals(repwd)) {
                    edtRePassword.setError("Mật khẩu nhập lại không khớp");
                } else {
                    loadingDialog.show();
                    User user = new User(name, phone, pwd, gender, 1);
                    firebaseManager.writeRef(AppContants.FIREBASE_USER_TABLE, usn, user, new FirebaseManager.Callback() {
                        @Override
                        public void success(DataSnapshot dataSnapshot) {
                            loadingDialog.dismiss();
                            Snackbar.make(layout, "Đăng ký thành công", Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void failed() {
                            loadingDialog.dismiss();
                            Snackbar.make(layout, "Đăng ký thất bại", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }else  edtUserName.setError("Tài khoản đã tồn tại");
        }else Snackbar.make(layout, "Vui lòng kiểm tra lại kết nối", Snackbar.LENGTH_LONG).show();

    }

    private void checkDupplicateAccount(final String usn){
        if(AppUtils.isNetworkAvailable(this)) {
            FirebaseManager firebaseManager = new FirebaseManager();
            firebaseManager.getRef(AppContants.FIREBASE_USER_TABLE, new FirebaseManager.Callback() {
                @Override
                public void success(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            if(child.getKey().equals(usn)){
                                isDupplicateAccount = true;
                            }
                        }
                        if(isDupplicateAccount) edtUserName.setError("Tài khoản đã tồn tại");

                    }
                }

                @Override
                public void failed() {
                    Snackbar.make(layout, "Đã có lỗi xảy ra, vui lòng thử lại", Snackbar.LENGTH_LONG).show();
                }
            });

        }else Snackbar.make(layout, "Vui lòng kiểm tra lại kết nối", Snackbar.LENGTH_LONG).show();
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
                //dialog.show();
                registerUser();
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
