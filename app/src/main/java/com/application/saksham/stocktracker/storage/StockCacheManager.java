package com.application.saksham.stocktracker.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockCacheManager {
}



public class JCacheManager extends SQLiteOpenHelper implements CacheManager<JumboJsonData> {

    public static final String DB_NAME = "jumbo";
    public static final int DB_VERSION = 1;

    private static JCacheManager jCacheManager;
    private ThreadHandler threadHandler;

    public static JCacheManager getInstance() {
        if(jCacheManager == null) {
            jCacheManager = new JCacheManager(Jumbo.getApplicationContext());
        }
        return jCacheManager;
    }

    private JCacheManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        threadHandler = new ThreadHandler("DB insert thread");
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

    @Override
    public void insertCache(final JumboJsonData jumboJsonData, final CacheCallback cacheCallback) {
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.ID, Helper.generatePrimaryId());
        contentValues.put(Columns.CACHE_DATA, jumboJsonData.getJsonObject().toString());
        contentValues.put(Columns.DEVICE_ID_PRESENT, jumboJsonData.containsDeviceID()?1:0);

        /**
         * Queue for insertion
         */
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                db.beginTransaction();
                try {
                    db.insert(Tables.CACHE_TABLE, null, contentValues);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if(cacheCallback != null) cacheCallback.cacheInserted();
            }
        });

    }


    @Override
    public ArrayList<EventCache<JumboJsonData>> getCache(int limit) {
        SQLiteDatabase db = getReadableDatabase();
        String s = "SELECT * FROM " + Tables.CACHE_TABLE + " LIMIT " + limit;
        Cursor c = db.rawQuery(s, null);
        ArrayList<EventCache<JumboJsonData>> caches = new ArrayList<>();
        try {

            if (!c.moveToFirst()) return caches;
            do {
                try {
                    String id = c.getString(c.getColumnIndex(Columns.ID));
                    JSONObject json = new JSONObject(c.getString(c.getColumnIndex(Columns.CACHE_DATA)));
                    boolean containsDeviceId = c.getInt(c.getColumnIndex(Columns.DEVICE_ID_PRESENT))==1?true:false;
                    caches.add(new EventCache<>(id, new JumboJsonData(json,containsDeviceId) ));
                } catch (JSONException e) {
                    /**
                     should never occur, since we are entering JSONObject only
                     */
                    ZCrashLogger.logAndPrintException(e);
                }
            } while (c.moveToNext());
        } finally {
            c.close();
        }
        return caches;
    }

    @Override
    public int getCacheSize() {
        SQLiteDatabase db = getReadableDatabase();
        String s = "SELECT count (*) as count FROM " + Tables.CACHE_TABLE;
        Cursor c = db.rawQuery(s, null);
        if(!c.moveToFirst()) return 0;
        return  c.getInt(c.getColumnIndex("count"));
    }

    @Override
    public void setCacheSynced(ArrayList<EventCache<JumboJsonData>> eventCaches, CacheCallback cacheCallback) {
        if (eventCaches == null) return;
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Tables.CACHE_TABLE, Columns.ID + " IN (" + Helper.createComaSepIdSet(eventCaches) + ")", null);
        if(cacheCallback != null) cacheCallback.onCacheSyncStateSet();
    }

    @Override
    public CachePolicy getCachePolicy() {
        return CachePolicy.FIFO;
    }


    public static class Tables {
        public static final String CACHE_TABLE = "cache";
    }

    public static class Columns {
        public static final String ID = "id";
        public static final String CACHE_DATA = "cache_data";
        public static final String DEVICE_ID_PRESENT = "device_id_present";
    }

    public static class Create {
        private static final String CREATE_CACHE_TABLE = "CREATE TABLE " + Tables.CACHE_TABLE + " (" +
                Columns.ID + " TEXT PRIMARY KEY," +
                Columns.CACHE_DATA + " TEXT, " +
                Columns.DEVICE_ID_PRESENT + " INTEGER " +
                ");";

        private static final String[] DB_CREATE_QUERIES = new String[]{
                CREATE_CACHE_TABLE
        };
    }

    public static class Helper {
        // TODO CHECK what sorcery is this !
        public static <T> String createComaSepIdSet(ArrayList<EventCache<T>> eventCaches) {
            if (ListUtils.isNullOrEmpty(eventCaches)) return "";
            String s = "";
            for (int i = 0; i < eventCaches.size() - 1; i++) {
                EventCache jCache = eventCaches.get(i);
                if (jCache == null) continue;
                s += getAsString(jCache.getId())+ ",";
            }
            s += getAsString(eventCaches.get(eventCaches.size() - 1).getId());
            return s;
        }

        public static String generatePrimaryId() {
            Random random = new Random();
            String s = "";
            s += String.valueOf(random.nextLong()) + "_";
            s += System.currentTimeMillis();
            return s;
        }

        public static String getAsString(String s) {
            return  "\'" + s + "\'" ;
        }
    }


}

