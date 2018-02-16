package com.application.saksham.stocktracker.presenters;

import com.application.saksham.stocktracker.mvpviews.BaseView;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public interface BasePresenter {
    void setMvpView(BaseView baseView);

    void pause();

    void resume();

    void destroy();

    void subscribe();

    void unsubscribe();

    BaseView getView();

}
