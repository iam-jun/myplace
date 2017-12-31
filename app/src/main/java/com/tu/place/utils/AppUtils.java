package com.tu.place.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;

import com.tu.place.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
        return usn+placeId+ new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());
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

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static String uriToBase64(Uri uri, Context context){
        Bitmap bm = BitmapFactory.decodeFile(getRealPathFromURI(uri, context));
        bm = Bitmap.createScaledBitmap(bm, bm.getWidth()*20/100, bm.getHeight()*20/100, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    public static byte[] compress(String string) throws IOException {
        ByteArrayOutputStream baostream = new ByteArrayOutputStream();
        OutputStream outStream = new GZIPOutputStream(baostream);
        outStream.write(string.getBytes("UTF-8"));
        outStream.close();
        byte[] compressedBytes = baostream.toByteArray();
        return compressedBytes;
    }

    public static String decompress(byte[] compressed) throws IOException {
        InputStream inStream = new GZIPInputStream(
                new ByteArrayInputStream(compressed));
        ByteArrayOutputStream baoStream2 = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = inStream.read(buffer)) > 0) {
            baoStream2.write(buffer, 0, len);
        }
        String uncompressedStr = baoStream2.toString("UTF-8");
        return uncompressedStr.toString();
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
