package com.application.saksham.stocktracker.utils;

import android.content.SharedPreferences;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class SharedPreferencesHelper {

    private final static String GENERAL_PREFS = "general_prefs";
    private final static String KEY_GENERAL_PREFS_LAST_STOCK = "last_stock";

    public static String getLastViewedStock() {
        SharedPreferences prefs = StockTrackerApp.getContext().getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE);
        return prefs.getString(KEY_GENERAL_PREFS_LAST_STOCK, "GOOG");
    }

    public static void setLastViewedStock(String stockName) {
        SharedPreferences.Editor editor = StockTrackerApp.getContext().getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE).edit();
        editor.putString(KEY_GENERAL_PREFS_LAST_STOCK, stockName);
        editor.apply();
    }
}
