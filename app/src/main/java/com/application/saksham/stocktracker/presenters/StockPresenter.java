package com.application.saksham.stocktracker.presenters;

import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.mvpviews.BaseView;
import com.application.saksham.stocktracker.mvpviews.StockView;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockPresenter implements BasePresenter {

    StockView stockView;

    @Override
    public void setMvpView(BaseView baseView) {
        this.stockView = (StockView) baseView;
    }

    public void fetchstock(String stockName, boolean inBackground){
        if(!inBackground)
            stockView.showLoading();

        Stock stock = new Stock();
        stock.setStockName("ABCD");
        stock.setChangeInPrice(15);
        stock.setClosed(true);
        stock.setCurrentPrice(1200);
        stock.setIntradayHighPrice(1300);
        stock.setChangeInPrice(1100);
        stock.setOpeningPrice(1150);
        stockView.onStockFetched(stock);
        stockView.hideLoading();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public BaseView getView() {
        return null;
    }
}
