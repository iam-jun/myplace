package com.tu.place.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tu.place.R;


/**
 * Created by SEV_USER on 4/18/2017.
 */

public class PlaceMgrActivity extends NavigationActivity implements View.OnClickListener {
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private ImageView imPlace;
    private ImageView imCamera;
    private ImageView imGallery;
    private Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_mgr);
        initViews();
    }

    private void initViews() {
        imPlace = (ImageView) findViewById(R.id.imPlace);
        imCamera = (ImageView) findViewById(R.id.imCamera);
        imGallery = (ImageView) findViewById(R.id.imGallery);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        imCamera.setOnClickListener(this);
        imGallery.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                break;
            case R.id.imCamera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }
                break;
            case R.id.imGallery:
                Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
                intentGallery.setType("image/*");
                startActivityForResult(Intent.createChooser(intentGallery, "Choose image"), REQUEST_GALLERY);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Glide.with(this).load(data.getData().toString()).placeholder(R.drawable.ic_map).error(R.drawable.ic_map).into(imPlace);
        }
    }

}
