
package com.application.saksham.stocktracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("1. Information")
    @Expose
    public String _1Information;
    @SerializedName("2. Symbol")
    @Expose
    public String _2Symbol;
    @SerializedName("3. Last Refreshed")
    @Expose
    public String _3LastRefreshed;
    @SerializedName("4. Interval")
    @Expose
    public String _4Interval;
    @SerializedName("5. Output Size")
    @Expose
    public String _5OutputSize;
    @SerializedName("6. Time Zone")
    @Expose
    public String _6TimeZone;

    public MetaData with1Information(String _1Information) {
        this._1Information = _1Information;
        return this;
    }

    public MetaData with2Symbol(String _2Symbol) {
        this._2Symbol = _2Symbol;
        return this;
    }

    public MetaData with3LastRefreshed(String _3LastRefreshed) {
        this._3LastRefreshed = _3LastRefreshed;
        return this;
    }

    public MetaData with4Interval(String _4Interval) {
        this._4Interval = _4Interval;
        return this;
    }

    public MetaData with5OutputSize(String _5OutputSize) {
        this._5OutputSize = _5OutputSize;
        return this;
    }

    public MetaData with6TimeZone(String _6TimeZone) {
        this._6TimeZone = _6TimeZone;
        return this;
    }

}
