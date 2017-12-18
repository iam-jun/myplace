package com.tu.place.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.tu.place.R;
import com.tu.place.firebase.FirebaseManager;
import com.tu.place.firebase.StorageManager;
import com.tu.place.model.User;
import com.tu.place.utils.AppContants;
import com.tu.place.utils.AppDialogManager;
import com.tu.place.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class ChangeInfoActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button btnUpdate, btnChangePwd;
    private EditText edtName, edtPhone, edtPassword, edtRePassword, edtDob;
    private RadioGroup radioGender;
    private ImageView imgAvatar, imgCamera,imgGallery;
    private Dialog dialog;
    private FirebaseManager firebaseManager;
    private Dialog loadingDialog;
    private String TAG = "ChangeInfo_Activity";
    private boolean isChangePwd = false;
    private boolean isUpload = false;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews() {
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnChangePwd = (Button) findViewById(R.id.btnChangePwd);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtDob = (EditText) findViewById(R.id.edtDOB);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtRePassword = (EditText) findViewById(R.id.edtRePassword);
        radioGender = (RadioGroup) findViewById(R.id.rai);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        imgCamera = (ImageView) findViewById(R.id.imCamera);
        imgGallery = (ImageView) findViewById(R.id.imGallery);
        loadingDialog = AppDialogManager.createLoadingDialog(this);
        btnUpdate.setOnClickListener(this);
        btnChangePwd.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        edtDob.setOnClickListener(this);

        edtName.setText(MainActivity.user.name);
        edtPhone.setText(MainActivity.user.phone);
        if(MainActivity.user.gender) radioGender.check(R.id.rbMale);
        else radioGender.check(R.id.rbFemale);
        if(!AppUtils.isEmptyString(MainActivity.user.url))
            Picasso.with(this).load(MainActivity.user.url).into(imgAvatar);

        if(!AppUtils.isEmptyString(MainActivity.user.dob))
            edtDob.setText(MainActivity.user.dob);

        datePickerDialog = new DatePickerDialog(
                ChangeInfoActivity.this, this, 1990, 1, 1);
        firebaseManager = new FirebaseManager();
        //firebaseManager.getRef(AppContants.FIREBASE_USER_TABLE);
    }

    private void updateUser(){
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String dob = edtDob.getText().toString();
        String pwd = MainActivity.user.password;
        Boolean gender = radioGender.indexOfChild(findViewById(radioGender.getCheckedRadioButtonId())) == 0;
        if(AppUtils.isNetworkAvailable(this)) {
                if (AppUtils.isEmptyString(name)) {
                    edtName.setError("Không được bỏ trống tên");
                } else if (AppUtils.isEmptyString(phone)) {
                    edtPhone.setError("Không được bỏ trống số diện thoại");
                }else if (AppUtils.isEmptyString(dob)) {
                    edtDob.setError("Không được bỏ trống ngày sinh");
                }else{
                    if(isChangePwd){
                        String tmp_pwd = edtPassword.getText().toString();
                        String repwd = edtRePassword.getText().toString();
                        if (AppUtils.isEmptyString(tmp_pwd)) {
                            edtPassword.setError("Không được bỏ trống mật khẩu");
                        } else if (!pwd.equals(repwd)) {
                            edtRePassword.setError("Mật khẩu nhập lại không khớp");
                        }else pwd = tmp_pwd;
                    }
                    final User user;
                    if(isUpload){
                        user = new User(name, phone, pwd, dob, gender, 1, null);
                        uploadAvatar(user);
                    } else {
                        user = new User(name, phone, pwd, dob, gender, 1, MainActivity.user.url);
                        writeUser(user);
                    }

                }
        }else Snackbar.make(btnUpdate, "Vui lòng kiểm tra lại kết nối", Snackbar.LENGTH_LONG).show();

    }

    private void uploadAvatar(final User user){
        loadingDialog.show();
        StorageManager storageManager = new StorageManager();
        storageManager.uploadFile(AppContants.STORAGE_AVATAR+"/"+MainActivity.user.username + ".jpg", AppUtils.bitmapToByteArray(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap()), new StorageManager.Callback() {
            @Override
            public void success(String url) {
                user.url = url;
                Log.d(TAG, "url "+user.url);
                Toast.makeText(ChangeInfoActivity.this, "Upload hình ảnh thành công", Toast.LENGTH_LONG).show();
                writeUser(user);
            }

            @Override
            public void failed() {
                Toast.makeText(ChangeInfoActivity.this, "Upload hình ảnh thất bại", Toast.LENGTH_LONG).show();
                updateUser();
            }
        });
    }

    private void writeUser(final User user){
        firebaseManager.writeRef(AppContants.FIREBASE_USER_TABLE, MainActivity.user.username, user, new FirebaseManager.Callback() {
            @Override
            public void success(DataSnapshot dataSnapshot) {
                user.username = MainActivity.user.username;
                EventBus.getDefault().postSticky(user);
                loadingDialog.dismiss();
                Toast.makeText(ChangeInfoActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failed() {
                loadingDialog.dismiss();
                Snackbar.make(btnUpdate, "Cập nhật thất bại", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code
    }

    private void picImageFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    private void changePassword(){
        isChangePwd = true;
        edtPassword.setEnabled(true);
        edtPassword.setText("");
        edtRePassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdate:
                updateUser();
                break;
            case R.id.imCamera:
                takePictureFromCamera();
                break;
            case R.id.imGallery:
                picImageFromGallery();
                break;
            case R.id.edtDOB:
                datePickerDialog.show();
                break;
            case R.id.btnChangePwd:
                changePassword();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgAvatar.setImageURI(selectedImage);
                    isUpload = true;
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imgAvatar.setImageURI(selectedImage);
                    isUpload = true;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        edtDob.setText(i2+"/"+(i1+1)+"/"+i);
    }
}
