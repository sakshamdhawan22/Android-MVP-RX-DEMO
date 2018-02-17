package com.application.saksham.stocktracker.repository;

import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.models.StockApiResponse;
import com.application.saksham.stocktracker.network.RestApi;
import com.application.saksham.stocktracker.network.RetrofitService;
import com.application.saksham.stocktracker.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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

    private StockRemoteDataSource(){}

    @Override
    public Observable<Stock> getStock(String stockName) {
        return RetrofitService.getInstance().getStockData(RestApi.FUNCTION.TIME_SERIES_DAILY, stockName,
                RestApi.INTERVAL.MIN_15.getValue(), RestApi.OUTPUT_SIZE.COMPACT, API_KEY)
                .map(stockApiResponse -> getStockFromStockApiResponse(stockApiResponse))
                .subscribeOn(Schedulers.io());
    }

    private Stock getStockFromStockApiResponse(StockApiResponse stockApiResponse) {
        Stock stock = new Stock();
        DecimalFormat df = new DecimalFormat("#.##");

        stock.setCurrentPrice(Double.valueOf(df.format(stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getClose())));
        stock.setClosed(!isMarkedOpen());
        stock.setOpeningPrice(Double.valueOf(df.format((stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getOpen()))));
        stock.setChangeInPrice(Double.valueOf(df.format(getChangeInPrice(stock.getCurrentPrice(), stockApiResponse))));
        stock.setIntradayLowPrice(Double.valueOf(df.format((stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getLow()))));
        stock.setIntradayHighPrice(Double.valueOf(df.format(stockApiResponse.getTimeSeries15min().get(stockApiResponse.getMetaData()._3LastRefreshed).getHigh())));
        stock.setStockName(stockApiResponse.getMetaData()._2Symbol);
        return stock;
    }

    private double getChangeInPrice(double currentPrice, StockApiResponse stockApiResponse) {
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

    private boolean isMarkedOpen() {
        int currentHour = DateUtils.getCurrentTimeInUs().get(Calendar.HOUR_OF_DAY);
        return currentHour >= 9 && currentHour <= 16;
    }
}
