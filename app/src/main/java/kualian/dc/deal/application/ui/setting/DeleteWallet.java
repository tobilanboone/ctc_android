package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.wallet.WalletAccount;
import kualian.dc.deal.application.widget.codeView.PwWindow;

/**
 * Created by admin on 2018/3/29.
 */

public class DeleteWallet extends SourceDelegate implements OnPwListener {
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
    IGlobalCallback callbackName = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
    private TextView sure,cancle;
    private PwWindow pwWindow;

    @Override
    public Object setLayout() {
        return R.layout.delete_wallet;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        sure =rootView.findViewById(R.id.sure);
        cancle = rootView.findViewById(R.id.cancle);
        TextView mtitle = rootView.findViewById(R.id.toolbar_title);
        mtitle.setText(UIutils.getString(R.string.delete_wallet));
        setOnClickViews(sure,cancle);
        rootView.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

            case R.id.cancle:
                _mActivity.onBackPressed();
                break;
            case R.id.sure:
                pwWindow = new PwWindow(WalletApp.getContext(), this);
                pwWindow.show(view);
                break;

            default:
                break;
        }
    }

    @Override
    public void getPw(String pw) {
        pwWindow.dismiss();
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())){
            WalletDao walletDao = new WalletDao();
            CoinDao coinDao = new CoinDao();
            coinDao.deleteByWalletId(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID()));
            boolean delete = walletDao.delete(SpUtil.getInstance().getWalletID());
            if(!delete){
                ToastUtil.showToastShort(UIutils.getString(R.string.fail));
                _mActivity.onBackPressed();
                return;
            }
            MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_DELETE_WALLET_SUCCESS);
            List<WalletAccount> walletAccounts = walletDao.queryAll();
            if(walletAccounts == null || walletAccounts.size() == 0){
                //跳转到钱包创建页面
                start(CreateWallet.getInstance("none"));
            }else{
                WalletAccount walletAccount = walletAccounts.get(0);
                SpUtil.getInstance().setWalletName(walletAccount.getWalletName());
                SpUtil.getInstance().setWalletPw(walletAccount.getWalletTradePw(),false);
                SpUtil.getInstance().setWalletID(walletAccount.getWalletId());
                SpUtil.getInstance().setWalletSend(walletAccount.getWalletId(),walletAccount.getWallet_seed());
                ToastUtil.showToastShort(UIutils.getString(R.string.success));
                if(callback != null){
                    callback.executeCallback(true);
                }
                if(callbackName != null){
                    callbackName.executeCallback(true);
                }
                _mActivity.onBackPressed();

            }
        }else{
            ToastUtil.showToastShort(UIutils.getString(R.string.error_pw));
        }
    }

}
