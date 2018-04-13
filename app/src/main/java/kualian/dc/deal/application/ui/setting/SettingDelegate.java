package kualian.dc.deal.application.ui.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;

/**
 * Created by idmin on 2018/2/10.
 */

public class SettingDelegate extends SourceDelegate {
    private TextView  mine, safe, language, default_coin, about_us, problem, update,contact;
    private TextView ctc_wallet_name;
    private RelativeLayout ctc_wallet_tool,ctc_sys_setting,ctc_help_center,ctc_about_us;
    //private Switch mSwitch;
    private static SettingDelegate instance = null;
    private IntentFilter intentFilter;
    private MylocalReceiver mylocalReceiver;

    public static SettingDelegate getInstance() {
       /* if (instance == null) {
            instance = new SettingDelegate();
        }*/
        return new SettingDelegate();
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ctc_wallet_name = rootView.findViewById(R.id.ctc_wallet_name);
        ctc_wallet_tool = rootView.findViewById(R.id.ctc_wallet_tool);
        ctc_sys_setting = rootView.findViewById(R.id.ctc_sys_setting);
        ctc_help_center = rootView.findViewById(R.id.ctc_help_center);
        ctc_about_us = rootView.findViewById(R.id.ctc_about_us);
//        mine = rootView.findViewById(R.id.setting_mine);
//        update = rootView.findViewById(R.id.check_update);
//        contact = rootView.findViewById(R.id.contact);
//        safe = rootView.findViewById(R.id.setting_safe);
//        language = rootView.findViewById(R.id.setting_language);
//        default_coin = rootView.findViewById(R.id.setting_coin);
//        about_us = rootView.findViewById(R.id.setting_about_us);
//        problem = rootView.findViewById(R.id.setting_problem);
//        setOnClickViews( mine, safe, language, default_coin, about_us, problem, update,contact);
        setOnClickViews( ctc_wallet_name, ctc_wallet_tool, ctc_sys_setting, ctc_help_center, ctc_about_us);
        ctc_wallet_name.setText(SpUtil.getInstance().getWalletName());
        onNameChanged();
        intentFilter = new IntentFilter();
        intentFilter.addAction("change_name_action");
        mylocalReceiver = new MylocalReceiver();
        WalletApp.getContext().registerReceiver(mylocalReceiver,intentFilter);
    }

    private void onNameChanged() {
        CallbackManager.getInstance().addCallback(CallbackType.ON_NAME_CHANGE, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                ctc_wallet_name.setText(SpUtil.getInstance().getWalletName());
            }
        });
        CallbackManager.getInstance().addCallback(CallbackType.REFRESH_DATA, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                ctc_wallet_name.setText(SpUtil.getInstance().getWalletName());
            }
        });
        CallbackManager.getInstance().addCallback(CallbackType.CHANGE_WALLET, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                ctc_wallet_name.setText(SpUtil.getInstance().getWalletName());
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
           /* case R.id.setting_pw:
                getParentDelegate().start(SettingPw.getInstance(false));
                break;*//*
            case R.id.setting_safe:
                getParentDelegate().start(new SecurityCenter());
                break;
           *//* case R.id.setting_problem:
                getParentDelegate().start(SettingPw.getInstance(true));
                break;*//*
            case R.id.setting_mine:
                getParentDelegate().start(PersonalCenter.getInstance(SpUtil.getInstance().getWalletName()));
                break;
            case R.id.setting_language:
                getParentDelegate().start(SettingDefault.getInstance(false));
                break;
            case R.id.setting_coin:
                getParentDelegate().start(SettingDefault.getInstance(true));
                break;
            case R.id.setting_about_us:
                getParentDelegate().start(new AboutUsDelegate());
                break;
            case R.id.contact:
                getParentDelegate().start(ContactDelegate.getInstance());
                break;
            case R.id.setting_problem:
                //getParentDelegate().start(new DelegateVerify());
                break;
            case R.id.check_update:
                getParentDelegate().start(new CheckUpdateDelegate());
                break;*/
            case R.id.ctc_wallet_name:
                break;
            case R.id.ctc_wallet_tool:
                getParentDelegate().start(new WalletTool());
                break;
            case R.id.ctc_sys_setting:
                getParentDelegate().start(new SysSetting());
                break;
            case R.id.ctc_help_center:
                getParentDelegate().start(new HelpCenter());
                break;
            case R.id.ctc_about_us:
                getParentDelegate().start(new AboutUs());
                break;
            default:
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WalletApp.getContext().unregisterReceiver(mylocalReceiver);
    }
    class MylocalReceiver extends BroadcastReceiver{

        /*
         * @param context The Context in which the receiver is running.
         * @param intent  The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            ctc_wallet_name.setText(intent.getStringExtra("name"));
        }
    }
}
