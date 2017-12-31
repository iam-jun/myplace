package com.tu.place.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tu.place.R;
import com.tu.place.model.Place;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class AppDialogManager {

    public static Dialog createLoadingDialog(Context context){
        ProgressDialog dialog = new ProgressDialog(context); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog onShowPlaceDialog(Context context, Place place){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setContentView(R.layout.ui_place);

        ImageView imPlace = (ImageView) dialog.findViewById(R.id.imPlace);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        TextView tvAddess = (TextView) dialog.findViewById(R.id.tvAddress);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView tvDistance = (TextView) dialog.findViewById(R.id.tvDistance);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating);

        tvTitle.setText(place.getTitle());
        tvAddess.setText(place.getAddress());
        tvContent.setText(place.getContent());
        ratingBar.setNumStars(5);
        ratingBar.setRating(Float.parseFloat(String.valueOf(place.getScore())));

        ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.ibtnDirection);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog onShowAddPlaceDialog(Context context, LatLng lng){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setContentView(R.layout.ui_add_place);

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
        Spinner spn = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, context.getResources().getStringArray(R.array.spn_place_type));
        spn.setAdapter(adapter);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog onShowImageViewerDialog(Context context, Bitmap bitmap){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setContentView(R.layout.ui_image_viewer);

        ImageView img = (ImageView) dialog.findViewById(R.id.imageView);
        final ImageView btnCancel = (ImageView) dialog.findViewById(R.id.imageView2);
        img.setImageBitmap(bitmap);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog onShowAddRatingDialog(Context context, float rating, final DialogClickListener listener){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setContentView(R.layout.ui_add_rating);

        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        ratingBar.setRating(rating);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(ratingBar.getRating());
                listener.onAccept(view);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public static Dialog onShowAddCommentDialog(Context context, final DialogClickListener listener, final  PickPhotoListener pickPhotoListener){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.setContentView(R.layout.ui_add_comment);

        final LinearLayout contentImg = (LinearLayout) dialog.findViewById(R.id.content_img);
        ImageView imgPickPhoto = (ImageView) dialog.findViewById(R.id.img_pick_photo);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        final EditText edt = (EditText)dialog.findViewById(R.id.edtComment);
//        Drawable progress = ratingBar.getProgressDrawable();
//        DrawableCompat.setTint(progress, Color.parseColor("#d9c300"));

        imgPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhotoListener.onPickPhoto(contentImg, view);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AppUtils.isEmptyString(edt.getText().toString())) {
                    view.setTag(edt.getText().toString());
                    listener.onAccept(view);
                    dialog.dismiss();
                }else {
                    edt.setError("Chưa nhập comment");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }

    public interface DialogClickListener{

        void onAccept(View view);

        void onCancel(View view);

    }

    public interface PickPhotoListener{
        void onPickPhoto(LinearLayout parent, View view);
    }
}
