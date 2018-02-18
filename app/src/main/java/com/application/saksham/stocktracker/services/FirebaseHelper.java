package com.application.saksham.stocktracker.services;

import android.content.Context;
import android.os.Bundle;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Saksham Dhawan on 2/18/18.
 */

public class FirebaseHelper {

    public static void scheduleStockSyncService(boolean recurring, Context context, int windowStart, int windowEnd) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("recurring", recurring);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder()
                .setExtras(bundle)
                .setService(StockSyncService.class)
                .setTag(StockSyncService.JOB_TAG)
                .setRecurring(recurring)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        dispatcher.mustSchedule(job);
    }
}
