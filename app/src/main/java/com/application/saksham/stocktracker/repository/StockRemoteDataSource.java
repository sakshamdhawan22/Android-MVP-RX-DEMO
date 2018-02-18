package com.application.saksham.stocktracker.repository;

import android.support.annotation.VisibleForTesting;

import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.models.StockApiResponse;
import com.application.saksham.stocktracker.network.RestApi;
import com.application.saksham.stocktracker.network.RetrofitService;
import com.application.saksham.stocktracker.utils.DateUtils;
import com.github.mikephil.charting.charts.LineChart;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockRemoteDataSource implements StockDataSource {

    public static final String API_KEY = "IFCESQ5S8658A7XW";


    private static StockRemoteDataSource stockRemoteDataSource;

    public static StockRemoteDataSource getInstance() {
        if (stockRemoteDataSource == null) {
            stockRemoteDataSource = new StockRemoteDataSource();
        }
        return stockRemoteDataSource;
    }

    private StockRemoteDataSource() {
    }

    @Override
    public Observable<Stock> getStock(String stockName, boolean forceRefresh) {
        return RetrofitService.getInstance().getStockData(RestApi.FUNCTION.TIME_SERIES_DAILY, stockName,
                null, null, API_KEY)
                .subscribeOn(Schedulers.io())
                .map(stockApiResponse -> getStockFromStockApiResponse(stockApiResponse));
    }

    public static Stock getStockFromStockApiResponse(StockApiResponse stockApiResponse) {
        Stock stock = new Stock();
        if (stockApiResponse.getErrorMessage() != null) {
            stock.setValidStock(false);
            return stock;
        }
        stock.setValidStock(true);

        DecimalFormat df = new DecimalFormat("#.##");

        stock.setCurrentPrice(Double.valueOf(df.format(stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getClose())));
        stock.setClosed(!DateUtils.isMarkedOpen());
        stock.setOpeningPrice(Double.valueOf(df.format((stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getOpen()))));
        stock.setChangeInPrice(Double.valueOf(df.format(getChangeInPrice(stock.getCurrentPrice(), stockApiResponse))));
        stock.setIntradayLowPrice(Double.valueOf(df.format((stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getLow()))));
        stock.setIntradayHighPrice(Double.valueOf(df.format(stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getHigh())));
        stock.setStockName(stockApiResponse.getMetaData()._2Symbol);
        stock.setLastUpdatedDate(stockApiResponse.getMetaData()._3LastRefreshed);
        stock.setTimeStamp(System.currentTimeMillis());

        HashMap<String,Double> stockDatePriceMap = new HashMap<>();
        for(String key: stockApiResponse.getTimeSeries15min().keySet())
            stockDatePriceMap.put(key,stockApiResponse.getTimeSeries15min().get(key).getClose());
        stock.setHistoricalData(stockDatePriceMap);

        return stock;
    }

    private static double getChangeInPrice(double currentPrice, StockApiResponse stockApiResponse) {
        Date todayDate = DateUtils.convertStringToDate(stockApiResponse.getMetaData()._3LastRefreshed);
        todayDate.setTime(todayDate.getTime() - 2); // one day before
        if (!stockApiResponse.getTimeSeries15min().containsKey(DateUtils.convertDateToString(todayDate)))
            return 0d;
        return currentPrice - stockApiResponse.getTimeSeries15min().get(DateUtils.convertDateToString(todayDate)).getClose();
    }

    @Override
    public Observable<Boolean> isValidStockName(String stockName) {
        return null; // unsupported as of now
    }

    @Override
    public Observable<Void> writeStockData(Stock stock) {
        throw new RuntimeException("writing on remote is not available");
    }
}
