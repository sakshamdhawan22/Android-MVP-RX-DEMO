package com.application.saksham.stocktracker.repository;

import com.application.saksham.stocktracker.models.Stock;

import rx.Observable;


/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public interface StockDataSource {

    Observable<Stock> getStock(String stockName);
    Observable<Boolean> isValidStockName(String stockName);
    Observable<Void> writeStockData(Stock stock);
}
