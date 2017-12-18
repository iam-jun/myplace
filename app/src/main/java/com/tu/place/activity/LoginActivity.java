package com.tu.place.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.tu.place.R;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.model.User;
import com.tu.place.permission.AppPermission;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private Button btnLogin, btnRegister;
    private EditText edtLoginUserName, edtLoginPassword;
    private String TAG = "LoginActivity";
    Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        if (!AppPermission.acceptedPemission(this)) {
            AppPermission.requestPermission(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!AppPermission.acceptedPemission(this)) {
            finish();
        }
    }

    private void initViews() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnLoginRegister);
        edtLoginUserName = (EditText) findViewById(R.id.edtLoginUserName);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        loadingDialog = AppDialogManager.createLoadingDialog(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(AppContants.PRE_IS_LOGGED_IN, false)){
            edtLoginUserName.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(AppContants.PRE_USERNAME, ""));
            edtLoginPassword.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(AppContants.PRE_PASSWORD, ""));
            btnLogin.performClick();
        }
    }

    private void loginToApp(){
        final String usn = edtLoginUserName.getText().toString();
        final String pwd = edtLoginPassword.getText().toString();
        if(usn.trim().equals("")){
            edtLoginUserName.setError("Tên tài khoản không được rỗng!");
        }else if(pwd.trim().equals("")){
            edtLoginPassword.setError("Mật khẩu không được rỗng!");
        }else{
            if(AppUtils.isNetworkAvailable(this)) {
                loadingDialog.show();
                FirebaseManager firebaseManager = new FirebaseManager();
                firebaseManager.getRef(AppContants.FIREBASE_USER_TABLE + "/" + usn, new FirebaseManager.Callback() {
                    @Override
                    public void success(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Log.d(TAG, dataSnapshot.toString());
                            User user = dataSnapshot.getValue(User.class);
                            if (usn.equals(dataSnapshot.getKey()) && pwd.equals(user.password)) {
                                loadingDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                user.username = usn;
                                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putBoolean(AppContants.PRE_IS_LOGGED_IN, true).commit();
                                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(AppContants.PRE_USERNAME, usn).commit();
                                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(AppContants.PRE_PASSWORD, pwd).commit();
                                EventBus.getDefault().postSticky(user);
                                startActivity(intent);
                                finish();
                            } else {
                                loadingDialog.dismiss();
                                Snackbar.make(btnLogin, "Sai tài khoản hoặc mật khẩu", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            loadingDialog.dismiss();
                            Snackbar.make(btnLogin, "Tài khoản không tồn tại", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failed() {
                        Snackbar.make(btnLogin, "Đã có lỗi xảy ra, vui lòng thử lại", Snackbar.LENGTH_LONG).show();
                    }
                });
            }else Snackbar.make(btnLogin, "Vui lòng kiểm tra lại kết nối", Snackbar.LENGTH_LONG).show();
        }

    }



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnLogin:
                loginToApp();
                break;
            case R.id.btnLoginRegister:
                intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}

