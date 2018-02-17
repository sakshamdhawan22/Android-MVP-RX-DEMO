package com.application.saksham.stocktracker.repository;

import com.application.saksham.stocktracker.models.Stock;

import rx.Observable;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockDataRepository implements StockDataSource {

    private static StockDataRepository StockDataRepository;
    StockDataSource mLocalStockDataSource, mRemoteStockDataSource;

    private StockDataRepository(StockDataSource localStockDataSource, StockDataSource remoteStockDataSource) {
        this.mLocalStockDataSource = localStockDataSource;
        this.mRemoteStockDataSource = remoteStockDataSource;
    }

    public static StockDataRepository getInstance(StockDataSource localStockDataSource, StockDataSource remoteStockDataSource) {
        if (StockDataRepository == null) {
            StockDataRepository = new StockDataRepository(localStockDataSource, remoteStockDataSource);
        }
        return StockDataRepository;
    }

    @Override
    public Observable<Stock> getStock(String stockName) {
        return mLocalStockDataSource.getStock(stockName)
                .first()
                .concatWith(mRemoteStockDataSource.getStock(stockName))
                .doOnNext(stock -> mLocalStockDataSource.writeStockData(stock));
    }

    @Override
    public Observable<Boolean> isValidStockName(String stockName) {
        return mRemoteStockDataSource.isValidStockName(stockName);
    }

    @Override
    public Observable<Void> writeStockData(Stock stock) {
        return mLocalStockDataSource.writeStockData(stock);
    }
}
