package com.application.saksham.stocktracker.app;

import android.app.Application;
import android.content.Context;

import com.application.saksham.stocktracker.utils.StringUtils;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

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
        initialiseStetho();
        Timber.plant(new Timber.DebugTree());
    }

    private void initialiseStetho() {
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(context)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

    public static Context getContext(){
        return context;
    }
}
