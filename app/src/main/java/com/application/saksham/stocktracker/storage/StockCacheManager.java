package com.application.saksham.stocktracker.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.models.Stock;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;

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
        return makeObservable(insertStockCallable(stock, getInstance().getWritableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    public static Observable<Stock> getStock(String stockName) {
        return makeObservable(getStockCallable(stockName, getInstance().getWritableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    private static Callable<Void> insertStockCallable(Stock stock, SQLiteDatabase db) {
        return () -> insertStock(stock, db);
    }

    private static Callable<Stock> getStockCallable(String stockName, SQLiteDatabase db) {
        return () -> getStock(stockName, db);
    }



    private static Void insertStock(Stock stock, SQLiteDatabase db) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.STOCK_NAME, stock.getStockName());
        contentValues.put(Columns.STOCK_CURRENT_PRICE, stock.getCurrentPrice());
        contentValues.put(Columns.STOCK_CHANGE_IN_PRICE, stock.getChangeInPrice());
        contentValues.put(Columns.STOCK_INTRADAY_LOW_PRICE, stock.getIntradayLowPrice());
        contentValues.put(Columns.STOCK_INTRADAY_HIGH_PRICE, stock.getIntradayHighPrice());
        contentValues.put(Columns.STOCK_CLOSED, stock.isClosed());
        db.beginTransaction();
        try {
            db.insert(Tables.CACHE_STOCK, null, contentValues);
            db.setTransactionSuccessful();
            return null;
        } finally {
            db.endTransaction();
        }
    }


    public static Stock getStock(String stockName, SQLiteDatabase db) {
        String s = "SELECT * FROM " + Tables.CACHE_STOCK + " where " + Columns.STOCK_NAME + " = '" + stockName + "'";
        Cursor c = db.rawQuery(s, null);
        try {
            if (!c.moveToFirst())
                return null;
            Stock stock = getStockFromCursor(c);
            c.close();
            return stock;
        } finally {
            c.close();
        }
    }

    private static Stock getStockFromCursor(Cursor c) {
        Stock stock = new Stock();
        String stockName = c.getString(c.getColumnIndex(Columns.STOCK_NAME));
        double stockPrice = c.getDouble(c.getColumnIndex(Columns.STOCK_CURRENT_PRICE));
        double intradayHighPrice = c.getDouble(c.getColumnIndex(Columns.STOCK_INTRADAY_HIGH_PRICE));
        double intradayLowPrice = c.getDouble(c.getColumnIndex(Columns.STOCK_INTRADAY_LOW_PRICE));
        double changeInPrice = c.getDouble(c.getColumnIndex(Columns.STOCK_CHANGE_IN_PRICE));
        boolean closed = c.getInt(c.getColumnIndex(Columns.STOCK_CLOSED)) == 1 ? true : false;
        stock.setStockName(stockName);
        stock.setCurrentPrice(stockPrice);
        stock.setIntradayHighPrice(intradayHighPrice);
        stock.setIntradayLowPrice(intradayLowPrice);
        stock.setChangeInPrice(changeInPrice);
        stock.setClosed(closed);
        return stock;
    }

    public static class Tables {
        public static final String CACHE_STOCK = "stock";
    }

    public static class Columns {
        public static final String STOCK_NAME = "stock_name";
        public static final String STOCK_CURRENT_PRICE = "current_price";
        public static final String STOCK_CHANGE_IN_PRICE = "change_in_price";
        public static final String STOCK_CLOSED = "closed";
        public static final String STOCK_INTRADAY_LOW_PRICE = "intraday_low_price";
        public static final String STOCK_INTRADAY_HIGH_PRICE = "closed";

    }

    public static class Create {
        private static final String CREATE_CACHE_TABLE = "CREATE TABLE " + Tables.CACHE_STOCK + " (" +
                Columns.STOCK_NAME + " TEXT PRIMARY KEY," +
                Columns.STOCK_CURRENT_PRICE + " REAL, " +
                Columns.STOCK_CHANGE_IN_PRICE + " REAL, " +
                Columns.STOCK_CLOSED + " INT " +
                Columns.STOCK_INTRADAY_LOW_PRICE + " REAL " +
                Columns.STOCK_INTRADAY_HIGH_PRICE + " REAL " +
                ");";

        private static final String[] DB_CREATE_QUERIES = new String[]{
                CREATE_CACHE_TABLE
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

