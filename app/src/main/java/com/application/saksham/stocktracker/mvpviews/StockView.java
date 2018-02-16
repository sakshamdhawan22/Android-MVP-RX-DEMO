package com.application.saksham.stocktracker.mvpviews;

import com.application.saksham.stocktracker.models.Stock;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public interface StockView extends BaseView {

    void onStockFetched(Stock stock);
    void onStockFetchFailed();

}
