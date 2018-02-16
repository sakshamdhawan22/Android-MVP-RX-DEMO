package com.application.saksham.stocktracker.app;

import android.app.Application;
import android.content.Context;

import com.application.saksham.stocktracker.activities.utils.StringUtils;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockTrackerApp extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        StringUtils.initialize(this);
        StockTrackerApp.context = getApplicationContext();
    }

    public Context getContext(){
        return context;
    }
}
