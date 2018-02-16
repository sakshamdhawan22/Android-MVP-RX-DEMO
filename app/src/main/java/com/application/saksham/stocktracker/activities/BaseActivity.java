package com.application.saksham.stocktracker.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.fragments.BaseFragment;

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
}

