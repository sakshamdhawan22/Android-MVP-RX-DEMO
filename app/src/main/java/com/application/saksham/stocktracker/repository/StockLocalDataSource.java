package com.application.saksham.stocktracker.repository;

import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.storage.StockCacheManager;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockLocalDataSource implements StockDataSource {

    private static StockLocalDataSource stockLocalDataSource;

    public static StockLocalDataSource getInstance() {
        if (stockLocalDataSource == null) {
            stockLocalDataSource = new StockLocalDataSource();
        }
        return stockLocalDataSource;
    }

    private StockLocalDataSource(){}

    @Override
    public Observable<Stock> getStock(String stockName) {
        return StockCacheManager.getStock(stockName);
    }

    @Override
    public Observable<Boolean> isValidStockName(String stockName) {
        return null; // unsupported as of now
    }

    @Override
    public Observable<Void> writeStockData(Stock stock) {
        return StockCacheManager.addStock(stock);
    }
}
