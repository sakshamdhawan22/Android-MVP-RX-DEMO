package com.application.saksham.stocktracker.mvpviews;

import android.support.v4.app.Fragment;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public interface BaseView {
    void showLoading();
    void hideLoading();
    void showRetry();
    void hideRetry();
    void showError(String message);
    Fragment getFragment();
}
