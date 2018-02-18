package com.application.saksham.stocktracker.models;


import java.util.HashMap;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class Stock {
    private boolean validStock;
    private String stockName;
    private double currentPrice;
    private double changeInPrice; // change from previous close price
    private boolean closed;
    private double intradayLowPrice;
    private double intradayHighPrice;
    private String lastUpdatedDate;
    private HashMap<String,Double> historicalData; // date->stock price map

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

    public boolean isValidStock() {
        return validStock;
    }

    public void setValidStock(boolean validStock) {
        this.validStock = validStock;
    }

    public HashMap<String, Double> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(HashMap<String, Double> historicalData) {
        this.historicalData = historicalData;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
