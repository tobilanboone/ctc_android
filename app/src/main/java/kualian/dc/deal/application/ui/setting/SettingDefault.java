package kualian.dc.deal.application.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceActivity;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnLanguageListener;
import kualian.dc.deal.application.ui.adapter.SettingDefaultAdapter;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.widget.DivItemDecoration;

/**
 * Created by idmin on 2018/2/28.
 */

public class SettingDefault extends SourceDelegate implements OnLanguageListener {
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
    private static final String TAG = "SettingDefault";
    private List<CoinType> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    private TextView mBack, mTitle, mNext;
    private boolean mIsCoin;
    private String language;
    private String moeyType;

    public static SettingDefault getInstance(boolean isCoin) {
        SettingDefault settingDefault = new SettingDefault();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isCoin);
        settingDefault.setArguments(bundle);
        return settingDefault;
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        if (getArguments().getBoolean(TAG)) {
            mIsCoin = true;
            mTitle.setText(R.string.select_icon);
            mData.add(new CoinType(getResources().getString(R.string.yuan)));
            mData.add(new CoinType(getResources().getString(R.string.dollar)));
        } else {
            mData.add(new CoinType(getResources().getString(R.string.setting_chinese)));
            mData.add(new CoinType(getResources().getString(R.string.setting_english)));
            /*mData.add(new CoinType(getResources().getString(R.string.setting_korean)));*/
            mTitle.setText(R.string.setting_language);
            mIsCoin = false;
        }
        mAdapter = new SettingDefaultAdapter(R.layout.item_setting_default, mData, mIsCoin,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mNext.setText(R.string.sure);
        mNext.setVisibility(View.VISIBLE);
        mNext.setTextColor(getResources().getColor(R.color.ctc_textColor));
        mNext.setCompoundDrawables(null, null, null, null);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mRecyclerView = rootView.findViewById(R.id.recycler);
        mBack = rootView.findViewById(R.id.toolbar_back);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mNext = rootView.findViewById(R.id.toolbar_next);
        setOnClickViews(mBack, mNext);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_setting_default;
    }

    //核心设置的代码
    protected void switchLanguage(String language) {
        Resources resources = getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case "zh":
                config.locale = Locale.CHINESE;
                resources.updateConfiguration(config, dm);
                break;
            case "en":
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);
                break;
            default:
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.toolbar_back) {
            _mActivity.onBackPressed();
        } else {
            if (!mIsCoin) {
                if (language!=null){
                    if(language.equals(Constants.LANGAE_ZH)){
                        MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.SETUP_LANGUAGE_ZH);
                    }else if(language.equals(Constants.LANGAE_EN)){
                        MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.SETUP_LANGUAGE_EN);
                    }
                    switchLanguage(language);
                    SpUtil.getInstance().setDefaultLanguage(language);
                    Intent intent = new Intent(_mActivity, SourceActivity.class);
                    startActivity(intent);
                    _mActivity.finish();
                }else {
                    _mActivity.onBackPressed();
                }
            }else {
                if(moeyType != null){
                    SpUtil.getInstance().setDefaultMoney(moeyType.equals(Constants.DOLLAR));

                }else{
                    SpUtil.getInstance().setDefaultMoney(SpUtil.getInstance().getDefaultLanguage().equals(Constants.LANGAE_EN));
                }
                if (callback != null){
                    callback.executeCallback(true);
                }
                _mActivity.onBackPressed();
            }
           /* switchLanguage("en");
            SpUtil.getInstance().setDefaultLanguage("en");
            Intent intent =new Intent(_mActivity,SourceActivity.class);
            startActivity(intent);
            _mActivity.finish();*/
           /* android.os.Process.killProcess( android.os.Process.myPid());
            System.exit(0);*/
            // popTo(MainDelegate.class,true);
            //System.exit();
            //startActivity(new Intent(_mActivity,SplashActivity.class));
        }
    }

    @Override
    public void getLanguage(String pw) {
        if(mIsCoin){
            moeyType = pw;
        }else{
            language = pw;
        }

    }
}
