package com.application.saksham.stocktracker.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.fragments.BaseFragment;
import com.application.saksham.stocktracker.services.FirebaseHelper;
import com.application.saksham.stocktracker.services.StockSyncService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class BaseActivity extends AppCompatActivity {

    public void addFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();

        Fragment oldFragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
        if (oldFragment != null)
            transaction.remove(oldFragment);

        transaction.
                add (R.id.container, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    // do not remove any fragment unless absolutely necessary (e.g login fragment)
    public void removeFragment(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public Fragment findRetainedFragment(String tag){
        FragmentManager fm = getSupportFragmentManager();
        return  fm.findFragmentByTag(tag);
    }

    @Override
    public void onBackPressed() {
        if ((getFragment()).onBackPressed()) // fragment will return true if it has handled the event
            return;

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            this.finish();
        }
    }

    private BaseFragment getFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    protected void onPause() {
        super.onPause();
       FirebaseHelper.scheduleStockSyncService(this,60*60,60*60*2);
    }
}

