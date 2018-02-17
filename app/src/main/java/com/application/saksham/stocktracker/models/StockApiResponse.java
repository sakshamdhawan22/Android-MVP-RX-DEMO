
package com.application.saksham.stocktracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class StockApiResponse {

    @SerializedName("Meta Data")
    @Expose
    public MetaData metaData;
    @SerializedName("Time Series (Daily)")
    @Expose
    public HashMap<String,StockData> timeSeries15min;
    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public HashMap<String, StockData> getTimeSeries15min() {
        return timeSeries15min;
    }

    public void setTimeSeries15min(HashMap<String, StockData> timeSeries15min) {
        this.timeSeries15min = timeSeries15min;
    }
}
