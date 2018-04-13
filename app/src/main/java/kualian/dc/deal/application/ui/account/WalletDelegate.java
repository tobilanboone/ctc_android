package kualian.dc.deal.application.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.WalletRequest;
import kualian.dc.deal.application.bean.WalletResponse;
import kualian.dc.deal.application.callback.WalletListListener;
import kualian.dc.deal.application.database.AssetDao;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.ui.adapter.WalletCoinAdapter;
import kualian.dc.deal.application.ui.create.AddCoin;
import kualian.dc.deal.application.ui.create.SaveSeed;
import kualian.dc.deal.application.ui.setting.CreateWallet;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.UpdateUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.WalletAccount;
import kualian.dc.deal.application.widget.DivItemDecoration;
import kualian.dc.deal.application.widget.codeView.WalletListWindow;

/**
 * Created by idmin on 2018/2/10.
 */

public class WalletDelegate extends SourceDelegate implements WalletListListener {
    private RecyclerView mRecyclerView;
    IGlobalCallback callbackReName = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
    IGlobalCallback change = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
    private TextView mAllMoney, mSymbol, mAdd, empty_add, mSelect,copy_seed_tip,wallet_name;
    private ImageView mPay, mReceive,wallet_change;
    private static WalletDelegate instance = null;
    private BaseQuickAdapter mAdapter;
    private List<CoinType> mList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private List<String> mCoins = new ArrayList<>();
    private LinearLayoutCompat empty;
    private RelativeLayout content;
    private CheckBox mSwitch;
    private String totalUsdtAmt, totalCnyAmt, defaultLanguage;
    private CoinDao coinDao ;
    private AssetDao assetDao ;
    private WalletListWindow walletListWindow;

    public static WalletDelegate getInstance() {
        return new WalletDelegate();
    }

    @Override
    protected void onEvent() {
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        if (!SpUtil.getInstance().getWalletID().equals("")){
            coinDao= new CoinDao();
            assetDao=new AssetDao();
            mList = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
        }
        if (mList != null && mList.size() > 0) {
            mCoins.clear();

            //TODO：根据货币类型添加
            mList.get(0).setCoinIndex(3);
            for (int i = 0; i < mList.size(); i++) {
                mCoins.add(mList.get(i).getCoinName());
            }
        } else {
            empty.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new WalletCoinAdapter(R.layout.item_coin_list_row, mList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new DivItemDecoration(getContext(), false));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CoinType coinType = (CoinType) adapter.getItem(position);
                getParentDelegate().start(RecordDelegate.getInstance(coinType));
            }
        });
        setData(new Gson().fromJson(SpUtil.getInstance().getAccountAsset(), WalletResponse.class));
        callNeT();
        //checkUpdateNet();
        refreshWallet();
        checkUpdate();

        //switchAccount();
        empty.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    private void switchAccount() {
        coinDao = new CoinDao();
        assetDao = new AssetDao();
    }


    private void checkUpdate() {
        if (!SpUtil.getInstance().getCheckDate().equals(CommonUtil.getCurrentDate())) {
            if (!SpUtil.getInstance().getDefaultVersion().equals(SpUtil.getInstance().getVersion())) {
                if (getContext().getExternalFilesDir("") != null) {
                    UpdateUtil.check(getContext(), false, true, false, false, false, 998);
                }
                SpUtil.getInstance().setCheckDate(CommonUtil.getCurrentDate());
            }
        }
    }

    /**
     * 刷新数据
     */
    private void callNeT() {
        wallet_name.setText(SpUtil.getInstance().getWalletName());

        if (SpUtil.getInstance().getWalletID() != null) {
            WalletRequest.HeaderBean header = new WalletRequest.HeaderBean(KeyUtil.getRandom());
            header.setTrancode(Constants.asset_query);
            WalletRequest.BodyBean body = new WalletRequest.BodyBean(mCoins);
            WalletRequest contactHandle = new WalletRequest();
            contactHandle.setBody(body);
            contactHandle.setHeader(header);
            presenter.queryServiceData(new Gson().toJson(contactHandle), Constants.asset_query);
        }
    }




    private void refreshWallet() {
        /*LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletName()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletID()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getLoginPw()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletPw()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getIsSaveSeed()+"");
        LogUtils.e("ctc test11:---","回来了");

        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        for (WalletAccount walletAccount : walletAccounts) {
            LogUtils.e("ctc test11:---",walletAccount.toString());
        }*/
        printLog();
        refresh();
        printLog();
        CallbackManager.getInstance().addCallback(CallbackType.REFRESH_DATA, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                printLog();
                refresh();
                printLog();
                /*//    public  String walletName;
                //    public  String walletId;
                //    public  String walletLoginPw;
                //    public  String walletTradePw;
                //    public  int walletDefaultIndex;
                //    public  String wallet_seed;
                //    public  String wallet_is_copy;
                LogUtils.e("ctc test:---",SpUtil.getInstance().getWalletName()+"");
                LogUtils.e("ctc test:---",SpUtil.getInstance().getWalletID()+"");
                LogUtils.e("ctc test:---",SpUtil.getInstance().getLoginPw()+"");
                LogUtils.e("ctc test:---",SpUtil.getInstance().getWalletPw()+"");
                LogUtils.e("ctc test:---",SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())+"");
                LogUtils.e("ctc test:---",SpUtil.getInstance().getIsSaveSeed()+"");
                LogUtils.e("ctc test:---","回来了");

                WalletDao walletDao = new WalletDao();
                List<WalletAccount> walletAccounts = walletDao.queryAll();
                for (WalletAccount walletAccount : walletAccounts) {
                    LogUtils.e("ctc test:---",walletAccount.toString());
                }
                if(!SpUtil.getInstance().getIsSaveSeed()){
                    copy_seed_tip.setVisibility(View.VISIBLE);
                    copy_seed_tip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(SpUtil.getInstance().getIsSaveSeed()){
                                copy_seed_tip.setVisibility(View.INVISIBLE);
                            }else{
                                start(new SaveSeed());
                            }

                        }
                    });
                }*/

            }
        });
        CallbackManager.getInstance().addCallback(CallbackType.COIN_TAG, new IGlobalCallback<Boolean>() {
            @Override
            public void executeCallback(@Nullable Boolean args) {
                switchAccount();
                mList = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
                if (mList != null) {
                    mCoins.clear();
                    if (mList.size() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        content.setVisibility(View.GONE);
                    } else {
                        mList.get(0).setCoinIndex(3);
                        empty.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        mCoins.add(mList.get(i).getCoinName());
                    }
                    mAdapter.getData().clear();
                    mAdapter.addData(mList);
                    if (args) {
                        refresh();
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.wallet_index;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycle_coin);
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        mAllMoney = rootView.findViewById(R.id.coin_total_money);
        mSymbol = rootView.findViewById(R.id.coin_total_symbol);
        mAdd = rootView.findViewById(R.id.coin_add);
//        mPay = rootView.findViewById(R.id.coin_pay);
//        mReceive = rootView.findViewById(R.id.coin_receive);
        empty_add = rootView.findViewById(R.id.empty_add);
//        mSelect = rootView.findViewById(R.id.select);
        empty = rootView.findViewById(R.id.empty_view);
        content = rootView.findViewById(R.id.content);
        mSwitch = rootView.findViewById(R.id.money_select);
        wallet_change = rootView.findViewById(R.id.wallet_change);
        wallet_name = rootView.findViewById(R.id.wallet_name);
        copy_seed_tip = rootView.findViewById(R.id.copy_seed_tip);
        CoinDao coinDao = new CoinDao();
        if(!SpUtil.getInstance().getIsSaveSeed()){
            copy_seed_tip.setVisibility(View.VISIBLE);
            copy_seed_tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SpUtil.getInstance().getIsSaveSeed()){
                        copy_seed_tip.setVisibility(View.INVISIBLE);
                    }else{
                        MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.HOME_WALLET_BACKUP_MNEMONIC);
                        getParentDelegate().start(new SaveSeed());
                    }

                }
            });
        }
        defaultLanguage = SpUtil.getInstance().getDefaultLanguage();
        if (defaultLanguage == null) {
            defaultLanguage = SpUtil.getInstance().getSystemLanguage();
        }
        SpUtil.getInstance().setDefaultMoney(defaultLanguage.equals(Constants.LANGAE_EN));


        mSwitch.setChecked(!SpUtil.getInstance().getDefaultMoney());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.getInstance().setDefaultMoney(!b);
                if (SpUtil.getInstance().getDefaultMoney()) {
                    mAllMoney.setText(totalUsdtAmt);
                    mSymbol.setText("$");
                } else {
                    mAllMoney.setText(totalCnyAmt);
                    mSymbol.setText("¥");
                }
                mAdapter.notifyItemChanged(0);
            }
        });
        setOnClickViews(mAdd, mPay, mReceive, empty_add,wallet_change);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callNeT();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.coin_add:
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.HOME_WALLET_ADD);
                getParentDelegate().start(AddCoin.getInstance(true, null));
                break;
            case R.id.empty_add:
                getParentDelegate().start(AddCoin.getInstance(true, null));
                break;
            case R.id.coin_pay:
                getParentDelegate().start(PayDelegate.getInstance(mList.get(0)));
                break;

            case R.id.coin_receive:
                getParentDelegate().start(ReceiveDelegate.getInstance(mList.get(0)));
                break;
            case R.id.wallet_change:
                walletListWindow = new WalletListWindow(this);
                walletListWindow.show(view);
                break;
            default:
        }
    }

    public void setData(WalletResponse walletResponse) {
        if (walletResponse.getData().getAssets() != null) {
            totalCnyAmt = walletResponse.getData().getTotalCnyAmt();
            totalUsdtAmt = walletResponse.getData().getTotalUsdtAmt();
            boolean defaultMoney = SpUtil.getInstance().getDefaultMoney();
            LogUtils.e("test:::",defaultMoney+"");
            if (SpUtil.getInstance().getDefaultMoney()) {
                mAllMoney.setText(totalUsdtAmt);
                mSymbol.setText("$");
            } else {
                mAllMoney.setText(totalCnyAmt);
                mSymbol.setText("¥");
            }
            List<WalletResponse.DataBean.AssetsBean> assets = walletResponse.getData().getAssets();
            if (assets.size() > 0 && mList.size() > 0) {
                mList.get(0).setCoinNum(assets.get(0).getNum());
                mList.get(0).setCoinMoney(assets.get(0).getCnyAmt());
                mList.get(0).setUsMoney(assets.get(0).getUsdtAmt());
                mList.get(0).setMinFee(assets.get(0).getMinFee());
                mList.get(0).setMaxFee(assets.get(0).getMaxFee());
                mList.get(0).setRecommendFee(assets.get(0).getRecommendFee());
                mList.get(0).setRecommendFee(assets.get(0).getRecommendFee());
                mList.get(0).setFixedFee(assets.get(0).getFixedFee());
                mList.get(0).setFeeType(assets.get(0).getFeeType());
            }
            mAdapter.notifyItemChanged(0);
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                if (tag.equals(Constants.asset_query)) {
                    if (response.getResponse() != null) {
                        WalletResponse walletResponse = null;
                        try {
                            walletResponse = new Gson().fromJson(response.getResponse(), WalletResponse.class);
                        } catch (Exception e) {
                            RxToast.showToast(R.string.error_state);
                            return;
                        }
                        if (walletResponse != null) {
                            if (walletResponse.getErrCode().equals(Constants.ERRCODE_ERROR)) {
                                if (assetDao!=null&&assetDao.queryAssetByWalletId(SpUtil.getInstance().getWalletID()).length() != 0) {
                                    walletResponse = new Gson().fromJson(assetDao.queryAssetByWalletId(SpUtil.getInstance().getWalletID()), WalletResponse.class);
                                }
                            } else {
                                if (assetDao!=null){
                                    assetDao.deleteByWalletId(SpUtil.getInstance().getWalletID());
                                    assetDao.add(response.getResponse());

                                }
//                                LogUtils.i("asset"+assetDao.queryAsset());
                            }
                        }
                        if (walletResponse.getData() == null) {
                            return;
                        }
                        setData(walletResponse);
                    }
                }
            }
        });

    }

    @Override
    public void change(String type) {
        if(walletListWindow != null){
            walletListWindow.dismiss();
        }
        if("change".equals(type)){
            refresh();
            if(change != null){
                change.executeCallback(true);
            }
            Intent intent=new Intent();
            intent.setAction("change_name_action");
            intent.putExtra("name", SpUtil.getInstance().getWalletName());
            WalletApp.getContext().sendBroadcast(intent,null);
        }else if("create".equals(type)){
            MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.HOME_WALLET_CREATE);
            getParentDelegate().start(CreateWallet.getInstance("index"));
        }

    }
    private void refresh(){
        mSwitch.setChecked(!SpUtil.getInstance().getDefaultMoney());
        if (SpUtil.getInstance().getDefaultMoney()) {
            mAllMoney.setText(totalUsdtAmt);
            mSymbol.setText("$");
        } else {
            mAllMoney.setText(totalCnyAmt);
            mSymbol.setText("¥");
        }
        mAdapter.notifyItemChanged(0);
        callNeT();
        boolean b = SpUtil.getInstance().getIsSaveSeed();
        if(!b){
            copy_seed_tip.setVisibility(View.VISIBLE);
            copy_seed_tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SpUtil.getInstance().getIsSaveSeed()){
                        copy_seed_tip.setVisibility(View.INVISIBLE);
                    }else{
                        MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.HOME_WALLET_BACKUP_MNEMONIC);
                        getParentDelegate().start(new SaveSeed());
                    }

                }
            });
        }else {
            copy_seed_tip.setVisibility(View.INVISIBLE);
        }
        CoinDao coinDao = new CoinDao();
        List<CoinType> coinTypeList = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
        mAdapter.getData().clear();
        mAdapter.addData(coinTypeList);
    }
    private void printLog(){
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletName()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletID()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getLoginPw()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletPw()+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())+"");
        LogUtils.e("ctc test11:---",SpUtil.getInstance().getIsSaveSeed()+"");
        LogUtils.e("ctc test11:---","回来了");

        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        for (WalletAccount walletAccount : walletAccounts) {
            LogUtils.e("ctc test11:---",walletAccount.toString());
        }
    }


}
