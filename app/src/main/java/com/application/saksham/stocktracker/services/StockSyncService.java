package com.application.saksham.stocktracker.services;

import com.application.saksham.stocktracker.presenters.StockPresenter;
import com.application.saksham.stocktracker.repository.StockDataRepository;
import com.application.saksham.stocktracker.repository.StockLocalDataSource;
import com.application.saksham.stocktracker.repository.StockRemoteDataSource;
import com.application.saksham.stocktracker.utils.SharedPreferencesHelper;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.SimpleJobService;

import timber.log.Timber;

/**
 * Created by Saksham Dhawan on 2/18/18.
 */

public class StockSyncService extends SimpleJobService {

    public static final String JOB_TAG = StockSyncService.class.getSimpleName();

    @Override
    public int onRunJob(JobParameters job) {
        Timber.d("running job");
        try {
            StockPresenter stockPresenter = new StockPresenter(
                    StockDataRepository.getInstance(StockLocalDataSource.getInstance(), StockRemoteDataSource.getInstance()));
            stockPresenter.fetchstock(SharedPreferencesHelper.getLastViewedStock(), true, false);
            Timber.d("job success");
            return JobService.RESULT_SUCCESS;
        } catch (Exception e) {
            Timber.d("job error");
            return JobService.RESULT_FAIL_RETRY;
        }
    }
}
