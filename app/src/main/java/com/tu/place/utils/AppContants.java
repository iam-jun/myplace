package com.tu.place.utils;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class AppContants {

    public static String TAG = "MyProject";
    public static String FIREBASE_ROOT = "MyProject";
    public static String GG_PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static String GOOGLE_BROWSER_API_KEY = "AIzaSyDhLLXweHCuv8m4ZRqocAKizxRywaGBjXI";
    public static String GG_PLACE_STATUS = "status";
    public static String GG_PLACE_STATUS_OK = "OK";
    public static String GG_PLACE_PHOTO_REF_KEY = "photoreference_key";
    public static String GG_PLACE_PHOTO_REF_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference="+ GG_PLACE_PHOTO_REF_KEY +"&sensor=false&maxheight=500&maxwidth=500&key="+GOOGLE_BROWSER_API_KEY;
    public static String FIREBASE_USER_TABLE = FIREBASE_ROOT + "/" + "User";
    public static String FIREBASE_PLACE_TABLE = FIREBASE_ROOT + "/" + "Place";
    public static String FIREBASE_COMMENT_TABLE = FIREBASE_ROOT + "/" + "Comment";
    public static String FIREBASE_PLACE_ID_COL = "placeId";
    public static String FIREBASE_LONGITU_COL = "longitu";
    public static String STORAGE_AVATAR = "Avatar";
    public static String PRE_IS_LOGGED_IN = "isLoggedIn";
    public static String PRE_USERNAME = "username";
    public static String PRE_PASSWORD = "password";

    public static String getURLImg(String key){
        return GG_PLACE_PHOTO_REF_URL.replace(GG_PLACE_PHOTO_REF_KEY, key);
    }
}
