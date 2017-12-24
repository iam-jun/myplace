package com.tu.place.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tu.place.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class AppUtils {

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    public static boolean checkContains(String container, String value){
        String[] arr = value.split(" ");
        for(int i=0; i<arr.length; i++){
            if(container.toLowerCase().contains(arr[i].toLowerCase()))
                return true;
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public static int getPlaceIcon(Context context, String content,  double score){
        String name = "";
//        if(score%2==0) score/=2;
//        else score = (score+1)/2;
        int _score = (int) score;
        switch (content){
            case "Food":
                name = "food_"+_score;
                break;
            case "Hotel":
                name = "hotel_"+_score;
                break;
            case "Drink":
                name = "coffee_"+_score;
                break;
            case "Health":
                name = "medical_"+_score;
                break;
            case "ATM":
                name = "atm_"+_score;
                break;
            case "Bank":
                name = "bank_"+_score;
                break;
            case "Shop":
                name = "shop_"+_score;
                break;
            case "Super Market":
                name = "market_"+_score;
                break;
            case "BusStop":
                name = "bus_stop_"+_score;
                break;
            case "Entertaiment":
                name = "gas_"+_score;
                break;
            default:
                name = "empty_pin";
                break;
        }
        int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        if (id == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + name
                    );
        }
        else
        {
            return id;
        }
    }

    public static String getRandomIdCmt(String usn, String placeId){
        return usn+placeId+ new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
    }

    public static String miliToDateString(long mili){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mili);
        return formatter.format(calendar.getTime());
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*20/100, bitmap.getHeight()*20/100, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static void replaceFragmentWithAnimation(FragmentManager fm, Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.setCustomAnimations(R.anim.show, R.anim.exit);
        transaction.show(fragment);
//        transaction.commit();
        transaction.addToBackStack(backStateName);
        transaction.commitAllowingStateLoss();
    }

    public static void hideFragmentWithAnimation(FragmentManager fm, Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.setCustomAnimations(R.anim.show, R.anim.exit);
        transaction.hide(fragment);
        //transaction.commit();
        transaction.addToBackStack(backStateName);
        transaction.commitAllowingStateLoss();
    }
}
