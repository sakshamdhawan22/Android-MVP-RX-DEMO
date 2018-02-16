package com.application.saksham.stocktracker.models;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class Stock {
    private float currentPrice;
    private float changeInPrice; // change from previous close price
    private boolean closed;
    private float intradayLowPrice;
    private float intradayHighPrice;

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getChangeInPrice() {
        return changeInPrice;
    }

    public void setChangeInPrice(float changeInPrice) {
        this.changeInPrice = changeInPrice;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public float getIntradayLowPrice() {
        return intradayLowPrice;
    }

    public void setIntradayLowPrice(float intradayLowPrice) {
        this.intradayLowPrice = intradayLowPrice;
    }

    public float getIntradayHighPrice() {
        return intradayHighPrice;
    }

    public void setIntradayHighPrice(float intradayHighPrice) {
        this.intradayHighPrice = intradayHighPrice;
    }

    public float getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(float openingPrice) {
        this.openingPrice = openingPrice;
    }

    private float openingPrice;
}
