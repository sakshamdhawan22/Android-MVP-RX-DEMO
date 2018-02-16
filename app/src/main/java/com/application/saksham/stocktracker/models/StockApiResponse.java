
package com.application.saksham.stocktracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class StockApiResponse {

    @SerializedName("Meta Data")
    @Expose
    public MetaData metaData;
    @SerializedName("Time Series (15min)")
    @Expose
    public HashMap<String,String> timeSeries15min;
}
