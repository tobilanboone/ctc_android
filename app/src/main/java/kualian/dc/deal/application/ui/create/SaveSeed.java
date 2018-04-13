package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.ui.setting.CreateWallet;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.widget.codeView.PwWindow;

/**
 * Created by admin on 2018/3/27.
 */

public class SaveSeed extends SourceDelegate implements OnPwListener {
    PwWindow pwWindow;
    private static final String TAG = "tag";
    private TextView toolbar_back,toolTitle;
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
    IGlobalCallback callbackName = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
    private static String pageTag = "";
    public static SaveSeed getInstance(String tag) {
        SaveSeed saveSeed = new SaveSeed();

        pageTag = tag;
        return saveSeed;
    }
    @Override
    public Object setLayout() {
        return R.layout.help_word;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        Button copy_wallet = rootView.findViewById(R.id.copy_wallet);

        toolbar_back = rootView.findViewById(R.id.toolbar_back);
        setOnClickViews(copy_wallet,toolbar_back);
        toolTitle = rootView.findViewById(R.id.toolbar_title);
        toolTitle.setText(getResources().getText(R.string.save_seed));
        MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.WALLET_CREATE_BACKUP_MNEMONIC);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.copy_wallet:
                pwWindow = new PwWindow(WalletApp.getContext(),this);
                pwWindow.show(view);
                break;
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
            default:

                break;
        }
    }

    @Override
    public void getPw(String pw) {
        String pwMessage = KeyUtil.getPwMessage(pw);
        String walletPw = SpUtil.getInstance().getWalletPw();
        if(walletPw.equals(pwMessage)){
            if(pwWindow != null && pwWindow.isShowing()){
                pwWindow.dismiss();
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.WALLET_CREATE_BACKUP_MNEMONIC);
                start(new SeedDelegate());
            }
        }else {
            ToastUtil.showToastShort(getResources().getString(R.string.error_pw));
        }
    }
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (callback != null){
            callback.executeCallback(true);
        }
        if (callbackName != null ){
            callbackName.executeCallback(true);
        }
        if (pageTag.equals("mine") || pageTag.equals("none")){
            popTo(CreateWallet.class,true);
        }else if(pageTag.equals("index")){
            return super.onBackPressedSupport();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                _mActivity.finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                RxToast.showToast(R.string.tips_exit);
            }
            return true;
        }
        LogUtils.e("ctc test:","点击了返回");
        return super.onBackPressedSupport();
    }




}
