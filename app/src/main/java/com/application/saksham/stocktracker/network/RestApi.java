package com.application.saksham.stocktracker.network;

import com.application.saksham.stocktracker.models.StockApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Saksham Dhawan on 2/17/18.
 */

public interface RestApi {

    @GET("query")
    Observable<StockApiResponse> getStockData(@Query("function") FUNCTION function, @Query("symbol") String stockName,
                                              @Query("interval") String interval, @Query("outputsize") OUTPUT_SIZE outputSize,
                                              @Query("apikey") String apiKey);

    enum FUNCTION {
        TIME_SERIES_INTRADAY, TIME_SERIES_DAILY
    }

    enum INTERVAL {
        MIN_1("1min"),
        MIN_15("15min");
        private String value;

        INTERVAL(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    enum OUTPUT_SIZE {
        FULL("full"),
        COMPACT("compact");
        private String value;

        OUTPUT_SIZE(String value) {
            this.value = value;
        }
    }
}