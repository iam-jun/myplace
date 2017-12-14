package com.tu.place.utils;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class AppUtils {
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
