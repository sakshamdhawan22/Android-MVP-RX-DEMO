package com.application.saksham.stocktracker.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.models.Stock;

import java.util.HashMap;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockCacheManager extends SQLiteOpenHelper {

    public static final String DB_NAME = "stock_cache";
    public static final int DB_VERSION = 1;
    private static StockCacheManager stockCacheManager;


    public static StockCacheManager getInstance() {
        if (stockCacheManager == null) {
            stockCacheManager = new StockCacheManager(StockTrackerApp.getContext());
        }
        return stockCacheManager;
    }

    private StockCacheManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String q : Create.DB_CREATE_QUERIES) {
            db.execSQL(q);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public static Observable<Void> addStock(Stock stock) {
        return Observable.concat(makeObservable(insertStockCallable(stock, getInstance().getWritableDatabase())),
                makeObservable(insertHistoricalDataCallable(stock, getInstance().getWritableDatabase())))
                .subscribeOn(Schedulers.computation());
    }

    public static Observable<Void> addHistoricalData(Stock stock) {
        return makeObservable(insertHistoricalDataCallable(stock, getInstance().getWritableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    public static Observable<Stock> getStock(String stockName) {
        return makeObservable(getStockCallable(stockName, getInstance().getWritableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Void> insertStockCallable(Stock stock, SQLiteDatabase db) {
        return () -> insertStock(stock, db);
    }

    private static Callable<Void> insertHistoricalDataCallable(Stock stock, SQLiteDatabase db) {
        return () -> insertHistoricalData(stock, db);
    }

    private static Callable<Stock> getStockCallable(String stockName, SQLiteDatabase db) {
        return () -> getStock(stockName, db);
    }


    private static synchronized Void insertStock(Stock stock, SQLiteDatabase db) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(StockColumns.STOCK_NAME, stock.getStockName().toLowerCase());
        contentValues.put(StockColumns.STOCK_CURRENT_PRICE, stock.getCurrentPrice());
        contentValues.put(StockColumns.STOCK_CHANGE_IN_PRICE, stock.getChangeInPrice());
        contentValues.put(StockColumns.STOCK_INTRADAY_LOW_PRICE, stock.getIntradayLowPrice());
        contentValues.put(StockColumns.STOCK_INTRADAY_HIGH_PRICE, stock.getIntradayHighPrice());
        contentValues.put(StockColumns.STOCK_CLOSED, stock.isClosed());
        contentValues.put(StockColumns.STOCK_OPEN_PRICE, stock.getOpeningPrice());
        contentValues.put(StockColumns.STOCK_LAST_UPDATED, stock.getLastUpdatedDate());
        db.beginTransaction();
        try {
            db.insertWithOnConflict(Tables.CACHE_STOCK, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
            return null;
        } finally {
            db.endTransaction();
        }
    }

    private static synchronized Void insertHistoricalData(Stock stock, SQLiteDatabase db) {
        db.beginTransaction();
        try {
            for (String key : stock.getHistoricalData().keySet()) {
                final ContentValues contentValues = new ContentValues();
                contentValues.put(HistoryDataColumns.HISTORICAL_DATA_STOCK_SYMBOL, stock.getStockName().toLowerCase());
                contentValues.put(HistoryDataColumns.HISTORICAL_DATA_DATE, key);
                contentValues.put(HistoryDataColumns.HISTORICAL_DATA_PRICE_ON_DATE, stock.getHistoricalData().get(key));
                db.insertWithOnConflict(Tables.CACHE_HISTORICAL_DATA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }


    public static Stock getStock(String stockName, SQLiteDatabase db) {
        String s = "SELECT * FROM " + Tables.CACHE_STOCK + " where " + StockColumns.STOCK_NAME + " = '" + stockName.toLowerCase() + "'";
        Cursor c = db.rawQuery(s, null);
        try {
            if (!c.moveToFirst()) {
                Timber.d("stock " + stockName + "not found in cache");
                return null;
            }
            Stock stock = getStockFromCursor(c);
            c.close();
            Timber.d("stock " + stockName + " found in cache");

            addHistoricalDataToSock(stockName, db, stock);

            return stock;
        } finally {
            c.close();
        }
    }

    private static void addHistoricalDataToSock(String stockName, SQLiteDatabase db, Stock stock) {
        HashMap<String, Double> historicalData = new HashMap<>();
        String s = "SELECT * FROM " + Tables.CACHE_HISTORICAL_DATA + " where " + HistoryDataColumns.HISTORICAL_DATA_STOCK_SYMBOL + " = '" + stockName.toLowerCase() + "'";
        Cursor c = db.rawQuery(s, null);
        try {
            if (!c.moveToFirst()) {
                return;
            }
            while (c.moveToNext()) {
                historicalData.put(c.getString(c.getColumnIndex(HistoryDataColumns.HISTORICAL_DATA_DATE)),
                        c.getDouble(c.getColumnIndex(HistoryDataColumns.HISTORICAL_DATA_PRICE_ON_DATE)));
            }
            c.close();
            stock.setHistoricalData(historicalData);
            return;
        } finally {
            c.close();
        }
    }

    private static Stock getStockFromCursor(Cursor c) {
        Stock stock = new Stock();
        String stockName = c.getString(c.getColumnIndex(StockColumns.STOCK_NAME));
        double stockPrice = c.getDouble(c.getColumnIndex(StockColumns.STOCK_CURRENT_PRICE));
        double intradayHighPrice = c.getDouble(c.getColumnIndex(StockColumns.STOCK_INTRADAY_HIGH_PRICE));
        double intradayLowPrice = c.getDouble(c.getColumnIndex(StockColumns.STOCK_INTRADAY_LOW_PRICE));
        double changeInPrice = c.getDouble(c.getColumnIndex(StockColumns.STOCK_CHANGE_IN_PRICE));
        boolean closed = c.getInt(c.getColumnIndex(StockColumns.STOCK_CLOSED)) == 1;
        double openPrice = c.getDouble(c.getColumnIndex(StockColumns.STOCK_OPEN_PRICE));
        String lastUpdated = c.getString(c.getColumnIndex(StockColumns.STOCK_LAST_UPDATED));
        stock.setStockName(stockName);
        stock.setCurrentPrice(stockPrice);
        stock.setIntradayHighPrice(intradayHighPrice);
        stock.setIntradayLowPrice(intradayLowPrice);
        stock.setChangeInPrice(changeInPrice);
        stock.setClosed(closed);
        stock.setValidStock(true); // if it wasn't valid, it would not have made it to the db
        stock.setOpeningPrice(openPrice);
        stock.setLastUpdatedDate(lastUpdated);
        return stock;
    }

    public static class Tables {
        public static final String CACHE_STOCK = "stock";
        public static final String CACHE_HISTORICAL_DATA = "historical_data";
    }

    public static class StockColumns {
        public static final String STOCK_NAME = "stock_name";
        public static final String STOCK_CURRENT_PRICE = "current_price";
        public static final String STOCK_CHANGE_IN_PRICE = "change_in_price";
        public static final String STOCK_CLOSED = "closed";
        public static final String STOCK_INTRADAY_LOW_PRICE = "intraday_low_price";
        public static final String STOCK_INTRADAY_HIGH_PRICE = "intraday_high_price";
        public static final String STOCK_OPEN_PRICE = "open_price";
        public static final String STOCK_LAST_UPDATED = "last_updated_time";
    }

    public static class HistoryDataColumns {
        public static final String HISTORICAL_DATA_STOCK_SYMBOL = "stock_symbol";
        public static final String HISTORICAL_DATA_DATE = "date";
        public static final String HISTORICAL_DATA_PRICE_ON_DATE = "price_on_date";
    }

    public static class Create {
        private static final String CREATE_CACHE_TABLE = "CREATE TABLE " + Tables.CACHE_STOCK + " (" +
                StockColumns.STOCK_NAME + " TEXT PRIMARY KEY," +
                StockColumns.STOCK_LAST_UPDATED + " TEXT , " +
                StockColumns.STOCK_OPEN_PRICE + " REAL, " +
                StockColumns.STOCK_CURRENT_PRICE + " REAL, " +
                StockColumns.STOCK_CHANGE_IN_PRICE + " REAL, " +
                StockColumns.STOCK_CLOSED + " INT, " +
                StockColumns.STOCK_INTRADAY_LOW_PRICE + " REAL, " +
                StockColumns.STOCK_INTRADAY_HIGH_PRICE + " REAL " +
                ");";


        private static final String CREATE_HISTORY_DATA_TABLE = "CREATE TABLE " + Tables.CACHE_HISTORICAL_DATA + " (" +
                HistoryDataColumns.HISTORICAL_DATA_STOCK_SYMBOL + " TEXT," +
                HistoryDataColumns.HISTORICAL_DATA_DATE + " TEXT, " +
                HistoryDataColumns.HISTORICAL_DATA_PRICE_ON_DATE + " REAL, " +
                "PRIMARY KEY (" + HistoryDataColumns.HISTORICAL_DATA_STOCK_SYMBOL + " , " +
                HistoryDataColumns.HISTORICAL_DATA_DATE + " )" +
                ");";

        private static final String[] DB_CREATE_QUERIES = new String[]{
                CREATE_CACHE_TABLE,
                CREATE_HISTORY_DATA_TABLE
        };
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                subscriber -> {
                    try {
                        subscriber.onNext(func.call());
                        subscriber.onCompleted();
                    } catch (Exception ex) {
                        subscriber.onError(ex);
                    }
                });
    }
}

