package com.application.saksham.stocktracker.presenters;

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
