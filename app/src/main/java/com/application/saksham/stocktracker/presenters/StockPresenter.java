package com.application.saksham.stocktracker.presenters;

import com.application.saksham.stocktracker.repository.StockDataRepository;
import com.application.saksham.stocktracker.repository.StockDataSource;
import com.application.saksham.stocktracker.mvpviews.BaseView;
import com.application.saksham.stocktracker.mvpviews.StockView;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockPresenter implements BasePresenter {

    StockView stockView;
    StockDataRepository stockDataRepository;
    CompositeSubscription compositeSubscription;

    public StockPresenter(StockDataRepository stockDataRepository) {
        this.stockDataRepository = stockDataRepository;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void setMvpView(BaseView baseView) {
        this.stockView = (StockView) baseView;
    }

    public void fetchstock(String stockName, boolean inBackground){
        if(!inBackground)
            stockView.showLoading();

        compositeSubscription.add(stockDataRepository.getStock(stockName)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(stock -> {
                    stockView.onStockFetched(stock);
                },throwable -> {
                    throwable.printStackTrace();
                }));

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
        compositeSubscription.clear();
    }

    @Override
    public BaseView getView() {
        return stockView;
    }
}
