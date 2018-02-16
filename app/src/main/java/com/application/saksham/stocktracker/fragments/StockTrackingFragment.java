package com.application.saksham.stocktracker.fragments;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.databinding.FragmentStockTrackingBinding;
import com.application.saksham.stocktracker.presenters.StockPresenter;
import com.application.saksham.stocktracker.utils.SharedPreferencesHelper;
import com.application.saksham.stocktracker.utils.StringUtils;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.mvpviews.StockView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockTrackingFragment extends BaseFragment implements StockView {

    View mRootView;
    String currentStock;

    private Unbinder mUnbinder;
    @BindView(R.id.textview_current_stock)
    TextView currentStockPriceTextView;
    FragmentStockTrackingBinding stockTrackingBinding;


    StockPresenter stockPresenter;


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
        initPresenter();
        currentStock = SharedPreferencesHelper.getLastViewedStock();
    }

    private void initPresenter() {
        stockPresenter = new StockPresenter();
        stockPresenter.setMvpView(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stockTrackingBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_stock_tracking, container, false);
        stockTrackingBinding.setStock  (new Stock());
        mUnbinder = ButterKnife.bind(this, stockTrackingBinding.getRoot());
        return stockTrackingBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockPresenter.fetchstock(currentStock,false);
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
        Toast.makeText(StockTrackerApp.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onStockFetched(Stock stock) {
        stockTrackingBinding.setStock(stock);
    }

    @Override
    public void onStockFetchFailed() {
        Toast.makeText(StockTrackerApp.getContext(),getString(R.string.stock_name_invald),Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_change)
    void onClickChange(){
        openStockSelector();
    }

    @OnClick(R.id.textview_current_stock)
    void openStockSelector() {

    }

}
