package com.application.saksham.stocktracker.repository;

import com.application.saksham.stocktracker.models.Stock;

import rx.Observable;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockDataRepository implements StockDataSource {

    private static StockDataRepository StockDataRepository;
    private StockDataSource mLocalStockDataSource, mRemoteStockDataSource;

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
    public Observable<Stock> getStock(String stockName, boolean forceRefresh) {
        if (forceRefresh)
            return mRemoteStockDataSource.getStock(stockName, forceRefresh)
                    .doOnNext(stock -> cacheStock(stock));
        return Observable.concat(mLocalStockDataSource.getStock(stockName, forceRefresh),
                mRemoteStockDataSource.getStock(stockName, forceRefresh))
                .doOnNext(stock -> cacheStock(stock));

    }

    private void cacheStock(Stock stock) {
        if (stock != null)
            writeStockData(stock)
                    .subscribe(aVoid -> {
                    }, throwable -> throwable.printStackTrace());
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
