package com.application.saksham.stocktracker.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.communication.EventBus;
import com.application.saksham.stocktracker.databinding.FragmentStockTrackingBinding;
import com.application.saksham.stocktracker.models.Stock;
import com.application.saksham.stocktracker.mvpviews.StockView;
import com.application.saksham.stocktracker.presenters.StockPresenter;
import com.application.saksham.stocktracker.repository.StockDataRepository;
import com.application.saksham.stocktracker.repository.StockLocalDataSource;
import com.application.saksham.stocktracker.repository.StockRemoteDataSource;
import com.application.saksham.stocktracker.utils.DateUtils;
import com.application.saksham.stocktracker.utils.SharedPreferencesHelper;
import com.application.saksham.stocktracker.utils.StringUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Saksham Dhawan on 2/16/18.
 */

public class StockTrackingFragment extends BaseFragment implements StockView {

    View mRootView;
    String currentStockName;
    Stock currentStock;

    private Unbinder mUnbinder;
    @BindView(R.id.textview_current_stock)
    TextView currentStockPriceTextView;
    @BindView(R.id.swipe_refesh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentStockTrackingBinding stockTrackingBinding;
    @BindView(R.id.loading_spinner_wrapper)
    RelativeLayout ProgressBarLayout;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.textview_closed)
    TextView updatedAgoTextView;

    StockPresenter stockPresenter;
    private Subscription timerSubsciption;


    @Override
    public String getTitle() {
        return StringUtils.getString(R.string.app_name);
    }

    public static BaseFragment newInstance() {
        return new StockTrackingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        currentStockName = SharedPreferencesHelper.getLastViewedStock();
        setRetainInstance(true);
    }

    private void initPresenter() {
        stockPresenter = new StockPresenter(StockDataRepository.getInstance(StockLocalDataSource.getInstance(),
                StockRemoteDataSource.getInstance()));
        stockPresenter.setMvpView(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stockTrackingBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_stock_tracking, container, false);
        mUnbinder = ButterKnife.bind(this, stockTrackingBinding.getRoot());
        return stockTrackingBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(StockRemoteDataSource.API_KEY.equals("")){
            String error = "Cannot run this project without a valid api key. Please get one from alphavantage.co";
            showError(error);
            return;
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            stockPresenter.fetchstock(currentStockName, true, true);
        });

        if (currentStock == null) // retained fragment opened for first time
            stockPresenter.fetchstock(currentStockName, false, false);
        else {
            onStockFetched(currentStock);
        }

        EventBus.getInstance().toObserverable()
                .subscribe(o -> {
                    if (o instanceof StockSelectorFragment.StockSymbolWrapper) {
                        String stockName = ((StockSelectorFragment.StockSymbolWrapper) o).stockName;
                        stockPresenter.fetchstock(stockName, false, false);
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void onResume() {
        super.onResume();
        timerSubsciption = rx.Observable.interval(1, TimeUnit.SECONDS)
                .startWith(0l)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (currentStock != null && !currentStock.isClosed()) {
                        updatedAgoTextView.setText(currentStock.getupdatedAgoString());
                    } else
                        updatedAgoTextView.setText(StringUtils.getString(R.string.market_closed));
                }, throwable -> throwable.printStackTrace());

    }

    @Override
    public void onPause() {
        super.onPause();
        timerSubsciption.unsubscribe();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        if (currentStock == null) {
            stockTrackingBinding.setShowRetry(true);
            stockTrackingBinding.setRetryMessage(message);
        } else {
            Toast.makeText(StockTrackerApp.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onStockFetched(Stock stock) {

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        stockTrackingBinding.setShowRetry(false);

        if (stock != null) {
            if (!stock.isValidStock()) {
                showError(StringUtils.getString(R.string.invalid_stock));
                stockPresenter.unsubscribe();
                openStockSelector();
                return;
            }
            Timber.d("recieved new stock data");

            stockTrackingBinding.setStock(stock);
            currentStock = stock;
            currentStockName = stock.getStockName();
            SharedPreferencesHelper.setLastViewedStock(currentStockName);
            drawGraph(stock);
        }
    }

    private void drawGraph(Stock stock) {
        if (stock.getHistoricalData() == null || stock.getHistoricalData().size() == 0 || lineChart == null) {
            return;
        }
        lineChart.setVisibility(View.VISIBLE);

        int index = stock.getHistoricalData().size() - 1;
        Entry[] entries = new Entry[index + 1];
        HashMap<Integer, String> xAxisValueToTextMap = new HashMap<>();

        String key = stock.getLastUpdatedDate();
        while (index >= 0) {
            if (stock.getHistoricalData().containsKey(key)) {
                entries[index] = new Entry(index, stock.getHistoricalData().get(key).floatValue());
                xAxisValueToTextMap.put(index, key);
                index--;
            }

            Date date = DateUtils.convertStringToDate(key);
            date.setTime(date.getTime() - 2);
            key = DateUtils.convertDateToString(date);

        }


        Description description = new Description();
        description.setText(StringUtils.getString(R.string.stock_history));
        lineChart.setDescription(description);


        LineDataSet lineDataSet = new LineDataSet(Arrays.asList(entries), StringUtils.getString(R.string.stock_price));

        lineChart.getAxisRight().setEnabled(false);

        lineChart.getXAxis().setValueFormatter((value, axis) -> {
            Timber.d("x value for  " + value + " is " + xAxisValueToTextMap.get((int) value));
            return xAxisValueToTextMap.get((int) value);
        });


        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        // create a data object with the datasets
        LineData data = new LineData(lineDataSet);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        // set data

        lineChart.setData(data);
        lineChart.setMaxVisibleValueCount(10);
        lineChart.setVisibleXRangeMaximum(10);
        lineChart.moveViewToX(100);
        lineChart.setScaleX(1);
        lineDataSet.setColor(ContextCompat.getColor(StockTrackerApp.getContext(), R.color.holo_orange_dark));
        lineDataSet.setFillColor(ContextCompat.getColor(StockTrackerApp.getContext(), R.color.holo_orange_dark));

    }

    @Override
    public void onStockFetchFailed() {
        Toast.makeText(StockTrackerApp.getContext(), getString(R.string.stock_name_invald), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_change)
    void onClickChange() {
        openStockSelector();
    }

    @OnClick(R.id.textview_current_stock)
    void openStockSelector() {
        StockSelectorFragment stockSelectorFragment = StockSelectorFragment.getInstance();
        stockSelectorFragment.setCancelable(true);
        stockSelectorFragment.show(getActivity().getSupportFragmentManager(), stockSelectorFragment.getTag());
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        stockPresenter.unsubscribe();
        super.onDestroy();
    }
}
