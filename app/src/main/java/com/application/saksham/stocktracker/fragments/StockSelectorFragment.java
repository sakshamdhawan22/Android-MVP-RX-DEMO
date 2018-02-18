package com.application.saksham.stocktracker.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.communication.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Saksham Dhawan on 2/17/18.
 */

public class StockSelectorFragment extends BottomSheetDialogFragment {

    @BindView(R.id.input_stock)
    EditText stockEditText;
    View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        mView = View.inflate(getContext(), R.layout.fragment_stock_selector, null);
        dialog.setContentView(mView);
        ButterKnife.bind(this, mView);
        focusEditTextAndOpenKeyboard();
        stockEditText.setOnFocusChangeListener(new MyFocusChangeListener());

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ButterKnife.bind(this, mView);
    }


    private void focusEditTextAndOpenKeyboard() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        stockEditText.requestFocus();
        stockEditText.performClick();
    }

    public static StockSelectorFragment getInstance() {
        StockSelectorFragment stockSelectorFragment = new StockSelectorFragment();
        return stockSelectorFragment;
    }


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @OnClick(R.id.button_stock_selection)
    void click() {
        if (stockEditText.getText().length() == 0) {
            Toast.makeText(StockTrackerApp.getContext(), R.string.enter_stock_error, Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss();
        EventBus.getInstance().send(new StockSymbolWrapper(stockEditText.getText().toString()));
        removeFocusAndCloseKeyboard();
    }

    private void removeFocusAndCloseKeyboard() {
        stockEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(stockEditText.getRootView().getWindowToken(), 0);
    }

    public class StockSymbolWrapper {
        public String stockName;

        public StockSymbolWrapper(String stockName) {
            this.stockName = stockName;
        }
    }
    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.input_stock && !hasFocus) {

                InputMethodManager imm =  (InputMethodManager) StockTrackerApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }
}