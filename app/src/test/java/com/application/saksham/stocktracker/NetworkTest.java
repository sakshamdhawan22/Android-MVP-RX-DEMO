package com.application.saksham.stocktracker;

import com.application.saksham.stocktracker.network.RestApi;
import com.application.saksham.stocktracker.network.RetrofitService;
import com.application.saksham.stocktracker.repository.StockLocalDataSource;
import com.application.saksham.stocktracker.repository.StockRemoteDataSource;

import org.junit.Test;

/**
 * Created by Saksham Dhawan on 2/19/18.
 */

public class NetworkTest {

    @Test
    public void isApiWorking() {
        RetrofitService.getInstance().getStockData(RestApi.FUNCTION.TIME_SERIES_DAILY, "GOOG", "1min", RestApi.OUTPUT_SIZE.COMPACT, "IFCESQ5S8658A7XW")
                .map(stockApiResponse -> StockRemoteDataSource.getStockFromStockApiResponse(stockApiResponse))
                .toBlocking()
                .subscribe(stock -> {
                    assert (stock.isValidStock());
                });
    }


}
