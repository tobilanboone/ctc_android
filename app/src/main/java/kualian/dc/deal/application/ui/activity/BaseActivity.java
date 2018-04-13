package kualian.dc.deal.application.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import qiu.niorgai.StatusBarCompat;

import static kualian.dc.deal.application.base.SourceActivity.StatusBarLightMode;

/**
 * Created by admin on 2018/4/10.
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, true);
        StatusBarLightMode(this);
        initView();
        initTitle();
        initData();
        initListener();
    }

    public abstract void initTitle();
    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();

    public void setOnClickViews(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
