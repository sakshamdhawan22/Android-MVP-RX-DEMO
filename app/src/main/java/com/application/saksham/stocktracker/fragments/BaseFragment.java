package com.application.saksham.stocktracker.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public abstract class BaseFragment extends Fragment {

    public abstract String getTitle();

    public boolean onBackPressed(){
        return false;
    }

}

