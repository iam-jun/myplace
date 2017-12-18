package com.tu.place.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        dialog.setCancelable(false);
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
}
