package com.application.saksham.stocktracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.activities.utils.StringUtils;
import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.mvpviews.StockView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockTrackingFragment extends BaseFragment implements StockView {

    View mRootView;
    String currentStock;

    @BindView(R.id.textview_current_stock)
    TextView currentStockPriceTextView;

    @Override
    public String getTitle() {
        return StringUtils.getString(R.string.app_name);
    }

    public static BaseFragment newInstance() {
        return new StockTrackingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_stock_tracking, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText()
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public void onStockFetched(Stock stock) {

    }

    @Override
    public void onStockFetchFailed() {

    }
}
