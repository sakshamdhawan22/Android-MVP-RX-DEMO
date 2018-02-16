package com.application.saksham.stocktracker.activities.utils;

import android.content.Context;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StringUtils {

    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context;
    }

    public static String getString(int id) {
        return mContext.getResources().getString(id);
    }
}

