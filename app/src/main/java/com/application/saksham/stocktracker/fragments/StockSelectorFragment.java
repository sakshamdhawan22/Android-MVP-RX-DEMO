package com.application.saksham.stocktracker.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.saksham.stocktracker.R;
import com.application.saksham.stocktracker.app.StockTrackerApp;
import com.application.saksham.stocktracker.communication.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Saksham Dhawan on 2/17/18.
 */

public class StockSelectorFragment extends BottomSheetDialogFragment {

    @BindView(R.id.input_stock)
    EditText stockEditText;
    View mView;
    @BindView(R.id.button_stock_selection)
    Button stockSeletionButton;

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

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ButterKnife.bind(this, mView);
    }


    private void focusEditTextAndOpenKeyboard() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(stockEditText.getWindowToken(), 0);
    }

    public class StockSymbolWrapper {
        public String stockName;
        public StockSymbolWrapper(String stockName) {
            this.stockName = stockName;
        }
    }
}