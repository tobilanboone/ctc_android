package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.BaseListDelegate;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.bean.HomeBean;
import kualian.dc.deal.application.bean.ResponseCode;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.callback.OnCoinSelectListener;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.ui.adapter.CoinAdapter;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.WalletAccount;
import kualian.dc.deal.application.wallet.coins.CtcMain;
import kualian.dc.deal.application.widget.DivItemDecoration;
import kualian.dc.deal.application.widget.codeView.PwWindow;

/**
 * Created by idmin on 2018/2/24.
 */

public class AddCoin extends BaseListDelegate implements OnCoinSelectListener, OnPwListener {
    private List<CoinType> mData = new ArrayList<>();
    private List<CoinType> mSelect;
    //是添加货币还是第一次创建货币
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.COIN_TAG);
    private boolean isAdd;
    private CoinDao coinDao = new CoinDao();
//    private PayWindow payWindow;
    private PwWindow pwWindow;
    private static final String KEY = "key";
    private String mKey;

    public static AddCoin getInstance(boolean isAdd, String key) {
        AddCoin addCoin = new AddCoin();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isAdd);
        bundle.putString(KEY, key);
        addCoin.setArguments(bundle);
        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        for (WalletAccount walletAccount : walletAccounts) {
            if (walletAccount.getWalletId().equals(SpUtil.getInstance().getWalletID())){
                String walletSend = SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID());
                DeterministicKey deterministicKey = KeyUtil.genMasterPriKey(walletSend);
                String adress = KeyUtil.genSubPubAddrWifFromMasterKey(deterministicKey, 3, null);


            }
        }

        return addCoin;
    }


    @Override
    protected void onEvent() {
        super.onEvent();
        smartRefreshLayout.setRefreshing(false);
        if (getArguments() != null) {
            isAdd = getArguments().getBoolean(TAG);
            mKey = getArguments().getString(KEY);
        }
        CtcMain coinType1 = new CtcMain();
        mData.add(coinType1);
        WalletApp.setCoinTypeList(mData);

        if (!isAdd) {
            viewStub.setLayoutResource(R.layout.button_create);
            View inflate = viewStub.inflate();
            inflate.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelect != null && mSelect.size() > 0) {
                        AppLoader.showLoading(getContext());
                        AppLoader.setMessage(getResources().getString(R.string.account_create));
                        // SpUtil.getInstance().setIsSelectCoin()
                        refreshCoin();
                    } else {
                        RxToast.showToast(getResources().getString(R.string.account_select_tips));
                    }

                }
            });
            SpUtil.getInstance().setIsRestore(true);
            mHead.setVisibility(View.GONE);
            //mNext.setTextColor(getResources().getColor(R.color.white));
            mBack.setVisibility(View.GONE);
            mNext.setText(R.string.button_skip);
            mNext.setTextColor(getResources().getColor(R.color.gray_light_));
        } else {
            mNext.setText(R.string.sure);
//            payWindow = new PayWindow(_mActivity, false, this);
            pwWindow = new PwWindow(_mActivity,this);
        }
        mAdapter = new CoinAdapter(R.layout.delegate_add_coin, mData, this, isAdd, coinDao);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mAdapter.bindToRecyclerView(mRecyclerView);
        mTitle.setText(R.string.add_asset);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        mNext.setVisibility(View.VISIBLE);
        mNext.setCompoundDrawables(null, null, null, null);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        setOnClickViews(mNext);
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        smartRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (isAdd) {
//            payWindow.show(view);
            pwWindow.show(view);
        } else {
            popTo(MainDelegate.class, false);
        }

    }

    //刷新钱包
    private void refreshCoin() {
        if (!isAdd) {
            if (mSelect == null || mSelect.size() == 0) {
                return;
            }
        }
        coinDao.deleteByWalletId(SpUtil.getInstance().getWalletID());
        for (CoinType coin : mSelect) {
//            coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(mKey)), coin.getCoinIndex(), MainNetParams.get()));
            coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coin.getCoinIndex(), MainNetParams.get()));
            coinDao.add(coin.getCoinResource(), coin.getCoinAddress(), coin.getCoinName(), coin.getCoinIndex(),SpUtil.getInstance().getWalletID());
        }
        if (!isAdd) {
            createWallet(mSelect);
        } else {
            addCoins(mSelect);
        }
        if (callback != null) {
            callback.executeCallback(true);
        }
    }

    //向服务器请求添加货币
    private void addCoins(List<CoinType> coins) {
        if (coins == null) {
            return;
        }
        HomeBean requestBean = new HomeBean();
        HomeBean.HeaderBean headerBean = new HomeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.coin_add);
        List<HomeBean.BodyBean.AddrsBean> list = new ArrayList<>();
        for (CoinType coin : coins) {
            HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean(coin.getCoinAddress(), coin.getCoinName());
            list.add(address);
        }

        HomeBean.BodyBean bodyBean = new HomeBean.BodyBean(list);
        requestBean.setBody(bodyBean);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.coin_add);
    }

    private void createWallet(List<CoinType> coins) {
        HomeBean requestBean = new HomeBean();
        HomeBean.HeaderBean headerBean = new HomeBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.wallet_create);
        List<HomeBean.BodyBean.AddrsBean> list = new ArrayList<>();
        for (CoinType coin : coins) {
            HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean(coin.getCoinAddress(), coin.getCoinName());
            list.add(address);
        }
        /*HomeBean.BodyBean.AddrsBean address = new HomeBean.BodyBean.AddrsBean("100", "100");
        list.add(address);*/
        HomeBean.BodyBean bodyBean = new HomeBean.BodyBean(list);
        requestBean.setBody(bodyBean);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.wallet_create);
    }

    @Override
    public void onCoinSelect(List<CoinType> list) {
        mSelect = list;
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tag.equals(Constants.wallet_create)) {
                    AppLoader.stopLoading();
                    if (response.getResponse() != null) {
                        ResponseCode responseCode = null;
                        try {
                            responseCode = new Gson().fromJson(response.getResponse(), ResponseCode.class);
                        } catch (Exception e) {
                            return;
                        }
                        if (responseCode != null && responseCode.getRtnCode() == 1) {
                            popTo(MainDelegate.class, false);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void getPw(String pw) {
        String pwMessage = KeyUtil.getPwMessage(pw);
        String walletPw = SpUtil.getInstance().getWalletPw();
        if(walletPw.equals(pwMessage)){
            if(pwWindow != null && pwWindow.isShowing()){
                pwWindow.dismiss();
                refreshCoin();
                _mActivity.onBackPressed();
            }
        }else {
            ToastUtil.showToastShort(getResources().getString(R.string.error_pw));
        }
        /*if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            payWindow.getCodeView().clear();
            payWindow.dismiss();
            mKey = pw;
            refreshCoin();
            _mActivity.onBackPressed();
        } else {
            Latte.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtil.setTranslateAnimationX(payWindow.getContainView());
                    WalletTool.vibrateOnce(_mActivity, 200);
                    payWindow.getCodeView().clear();
                    // AppLoader.stopLoading();
                    RxToast.showToast(getResources().getString(R.string.error_pw));
                }
            }, 200);
        }*/
    }
}
