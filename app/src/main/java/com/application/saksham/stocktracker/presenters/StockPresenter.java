package com.application.saksham.stocktracker.presenters;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.mvpviews.BaseView;
import com.application.saksham.stocktracker.mvpviews.StockView;
import com.application.saksham.stocktracker.repository.StockDataRepository;
import com.application.saksham.stocktracker.utils.NetworkUtils;
import com.application.saksham.stocktracker.utils.StringUtils;

import java.io.IOException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockPresenter implements BasePresenter {

    StockView stockView;
    StockDataRepository stockDataRepository;
    CompositeSubscription compositeSubscription;
    Subscription fetchSubsciption;

    public StockPresenter(StockDataRepository stockDataRepository) {
        this.stockDataRepository = stockDataRepository;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void setMvpView(BaseView baseView) {
        this.stockView = (StockView) baseView;
    }

    public void fetchstock(String stockName, boolean inBackground) {
        if (fetchSubsciption != null) {
            compositeSubscription.remove(fetchSubsciption);
        }
        if (!inBackground)
            stockView.showLoading();

        fetchSubsciption = stockDataRepository.getStock(stockName)
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(stock -> {
                    stockView.onStockFetched(stock);
                }, e -> {
                    if (e instanceof IOException && !NetworkUtils.isInternetOn(StockTrackerApp.getContext())) {
                        stockView.showError(StringUtils.getString(R.string.no_internet));
                    } else
                        stockView.showError(StringUtils.getString(R.string.something_went_wrong));
                });
        compositeSubscription.add(fetchSubsciption);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {
        unsubscribe();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        Timber.d("unsubscribing");
        compositeSubscription.clear();
    }

    @Override
    public BaseView getView() {
        return stockView;
    }
}
