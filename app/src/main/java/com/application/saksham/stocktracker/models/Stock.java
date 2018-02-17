package com.application.saksham.stocktracker.models;


/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class Stock {
    private String stockName;
    private double currentPrice;
    private double changeInPrice; // change from previous close price
    private boolean closed;
    private double intradayLowPrice;
    private double intradayHighPrice;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getChangeInPrice() {
        return changeInPrice;
    }

    public void setChangeInPrice(double changeInPrice) {
        this.changeInPrice = changeInPrice;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public double getIntradayLowPrice() {
        return intradayLowPrice;
    }

    public void setIntradayLowPrice(double intradayLowPrice) {
        this.intradayLowPrice = intradayLowPrice;
    }

    public double getIntradayHighPrice() {
        return intradayHighPrice;
    }

    public void setIntradayHighPrice(double intradayHighPrice) {
        this.intradayHighPrice = intradayHighPrice;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    private double openingPrice;

    public String getIncreaseDecreaseText() {
        return changeInPrice > 0 ? "+" + changeInPrice : "-" + changeInPrice;
    }
}
