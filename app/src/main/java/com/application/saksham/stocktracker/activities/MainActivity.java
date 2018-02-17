package com.application.saksham.stocktracker.activities;

import android.os.Bundle;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.fragments.StockTrackingFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (findRetainedFragment(StockTrackingFragment.class.getSimpleName())==null) {
            addFragment(StockTrackingFragment.newInstance());
        }
    }
}
