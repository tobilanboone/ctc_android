package kualian.dc.deal.application.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.bitcoinj.params.MainNetParams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ctc_shares1_decode.transaction.CTCTransaction;
import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ResponseCode;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.SendRequest;
import kualian.dc.deal.application.bean.TradeBean;
import kualian.dc.deal.application.bean.TradeResponse;
import kualian.dc.deal.application.bean.data;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.loader.AppLoader;
import kualian.dc.deal.application.ui.camera.RequestCodes;
import kualian.dc.deal.application.ui.contact.ContactSelectDelegate;
import kualian.dc.deal.application.ui.scan.ActivityScanerCode;
import kualian.dc.deal.application.util.BitcoinAddressValidator;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.NetUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.widget.codeView.PayWindow;
import kualian.dc.deal.application.widget.codeView.PwWindow;

/**
 * Created by idmin on 2018/2/26.
 */

public class PayDelegate extends SourceDelegate implements OnPwListener {
    private static final String Sign = "PayDelegate";
    //back, coinName,coinMoney, cancel, minus, add,contact,moneyCoinKind, cashCoinkind  AutoCompleteTextView
    private TextView title, back, coinMoney, goNext,scan;
    private TextView payCash;
    private EditText payMoney,payAddress,payRemark;
    private String receive_address, key;
    private View view;
    private PayWindow window;
    private PwWindow pwWindow;
    private CoinType coinType;
    private ImageView icon, cashKind;
    private double minFee,maxFee,recommendFee,fixedFeeD;
    private int feeType;
    public static PayDelegate getInstance(CoinType coinType) {
        MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.ENTER_PAYMENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Sign, coinType);
        PayDelegate receiveDelegate = new PayDelegate();
        receiveDelegate.setArguments(bundle);
        return receiveDelegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.transfer;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);

        if (getArguments() != null) {
            coinType = (CoinType) getArguments().getSerializable(Sign);
        }
        view = rootView;
        back = rootView.findViewById(R.id.toolbar_back);
//        next = rootView.findViewById(R.id.toolbar_next);
        title = rootView.findViewById(R.id.toolbar_title);

//        address = rootView.findViewById(R.id.coin_address);
//        coinName = rootView.findViewById(R.id.coin_name);
        coinMoney = rootView.findViewById(R.id.coin_money);
        scan = rootView.findViewById(R.id.scan_zxing);
        icon = rootView.findViewById(R.id.coin_icon);
        payCash = rootView.findViewById(R.id.fixedfee);

//        cancel = rootView.findViewById(R.id.cancel);
        goNext = rootView.findViewById(R.id.next);
//        minus = rootView.findViewById(R.id.minus);
//        add = rootView.findViewById(R.id.add);
//        contact = rootView.findViewById(R.id.contact);

        payMoney = rootView.findViewById(R.id.pay_money);
        payAddress = rootView.findViewById(R.id.pay_address);
        payRemark = rootView.findViewById(R.id.remark);


        //moneyKind = rootView.findViewById(R.id.select_kind);
//        moneyCoinKind = rootView.findViewById(R.id.coin_kind);
//        cashCoinkind = rootView.findViewById(R.id.cash_kind);
//        rootView.findViewById(R.id.head).setBackgroundColor(getResources().getColor(R.color.transparent));

        if(coinType.getFeeType() == 2){
            fixedFeeD = Double.valueOf(coinType.getFixedFee());
            payCash.setText(String.valueOf(fixedFeeD));
        }else{
            minFee=Double.parseDouble(coinType.getMinFee());
            maxFee=Double.parseDouble(coinType.getMaxFee());
            recommendFee=Double.parseDouble(coinType.getRecommendFee());
        }

//        payCash.setText(coinType.getRecommendFee());
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        setOnClickViews(back, goNext,scan);
        title.setText(R.string.wallet_pay);
//        next.setVisibility(View.VISIBLE);

//        coinName.setText(coinType.getCoinName());
//        address.setText(coinType.getCoinAddress());
        coinMoney.setText(coinType.getCoinNum());
        //icon.setImageResource(coinType.getCoinResource());
//        moneyCoinKind.setText(coinType.getCoinName());
//        cashCoinkind.setText(coinType.getCoinName());

        SpUtil.getInstance().setUUID(UUID.randomUUID().toString());
    }

    public void buildTrade(String pw) {
        TradeBean requestBean = new TradeBean();
        TradeBean.HeaderBean headerBean = new TradeBean.HeaderBean(KeyUtil.getRandom(),SpUtil.getInstance().getUUID());
        headerBean.setTrancode(Constants.tran_build);
        TradeBean.BodyBean body = new TradeBean.BodyBean(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coinType.getCoinIndex(), MainNetParams.get()),KeyUtil.getCTCPubKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())),coinType.getCoinName(), payAddress.getText().toString(), payMoney.getText().toString(), payRemark.getText().toString(), payCash.getText().toString());
        requestBean.setBody(body);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.tran_build);
    }

    public void sendBroadcast(TradeResponse tradeResponse) {

        SendRequest requestBean = new SendRequest();
        SendRequest.HeaderBean headerBean = new SendRequest.HeaderBean(KeyUtil.getRandom(),SpUtil.getInstance().getUUID());
        headerBean.setTrancode(Constants.tran_send);
        //KeyUtil.genSignMessage(tradeResponse.getData().getInputs(),
//        tradeResponse.getData().getUnsignTranHex(), coinType.getCoinAddress(),
//                KeyUtil.genSubPriKeyWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coinType.getCoinIndex(), MainNetParams.get()))
        SendRequest.BodyBean body = new SendRequest.BodyBean();
        String s1 = payAddress.getText().toString();
        String signaddress = "";
        /*if(s1.length()>33){
            signaddress = s1.substring(0,s1.length()-32);
        }else {
            signaddress = s1;
        }*/
        signaddress = s1;
        String s2 = payMoney.getText().toString();
        SendRequest.BodyBean.TradeBean tradeBean = new SendRequest.BodyBean.TradeBean(coinType.getCoinAddress(), coinType.getCoinName(), s1, s2, payRemark.getText().toString(), payCash.getText().toString());
        data data = tradeResponse.getData().getData();
        String walletSend = SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID());
        String trx_data = data.getTrx_data();
        //online:40ecc6870dca9f97c9bb642c6161bb611848fad561424153b3ce57e478ca7057
        //test:5feb5509c06cff2dea62886886943cd4c96e0c671c12593cc81e3cc96787a247
        String s = KeyUtil.genSubPriKeyWifFromMasterKey(KeyUtil.genMasterPriKey(walletSend), coinType.getCoinIndex(), MainNetParams.get());
        Map<String, String> stringStringMap = CTCTransaction.signTransaction(trx_data, s, signaddress, s2,"40ecc6870dca9f97c9bb642c6161bb611848fad561424153b3ce57e478ca7057");
//        result.put("expiration", sdf.format(dt));
//        result.put("signatures", Util.bytesToHex(signature));
//        result.put("result", "1");
        if(!stringStringMap.get("result").equals("1")){
            ToastUtil.showToastShort(UIutils.getString(R.string.transaction_failure));
            AppLoader.showLoading(_mActivity);
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(stringStringMap.get("signatures"));
        data.getTrx().setSignatures(list);
        data.getTrx().setExpiration(stringStringMap.get("expiration"));
        body.setData(data);
        /*List<SendRequest.BodyBean.Input> inputList = new ArrayList<>();
        if (tradeResponse.getData().getInputs() == null) {
            return;
        }
        for (int i = 0; i < tradeResponse.getData().getInputs().size(); i++) {
            SendRequest.BodyBean.Input input = new SendRequest.BodyBean.Input(tradeResponse.getData().getInputs().get(i).getScriptPubKey()
                    , tradeResponse.getData().getInputs().get(i).getAmout()
                    , tradeResponse.getData().getInputs().get(i).getScriptPubKey()
                    , tradeResponse.getData().getInputs().get(i).getVout());
            inputList.add(input);
        }
        body.setInputs(inputList);*/
        body.setTranContent(tradeBean);
        requestBean.setBody(body);
        requestBean.setHeader(headerBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.tran_send);
    }

    double number;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.scan_zxing:
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CLICK_SCANNER);
                startActivityForResult(new Intent(_mActivity, ActivityScanerCode.class), RequestCodes.SCAN);
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_SCAN, new IGlobalCallback<String>() {
                            @Override
                            public void executeCallback(@Nullable String args) {
                                payAddress.setText(args);
                            }
                        });
                break;
            case R.id.next:
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CLICK_SEND);
                if (!TextUtils.isEmpty(payAddress.getText().toString()) && !TextUtils.isEmpty(payMoney.getText().toString()) && !TextUtils.isEmpty(payCash.getText().toString())) {
                    if (!BitcoinAddressValidator.assertBitcoin(payAddress.getText().toString().trim(), coinType.getCoinName()) && !CommonUtil.isNumeric(payAddress.getText().toString().trim()) && !CommonUtil.isNumericExp(payAddress.getText().toString().trim())) {
                        RxToast.showToast(R.string.trade_address_error);
                        return;
                    }
//                    if (Double.parseDouble(payCash.getText().toString()) < Double.parseDouble(coinType.getMinFee())) {
//                        RxToast.showToast(R.string.tips_cash_lack);
//                    } else {
                        pwWindow = new PwWindow(_mActivity,  this);
                        pwWindow.show(view);
//                    }
                } else {
                    RxToast.showToast(R.string.tips_finish);
                }
                break;
            case R.id.cancel:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.contact:
                start(ContactSelectDelegate.getInstance(true));
                CallbackManager.getInstance().addCallback(CallbackType.ON_RESULT, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        if (args != null) {
                            payAddress.setText(args);
                        }
                    }
                });
                break;
            case R.id.add:
                if (TextUtils.isEmpty(payCash.getText().toString())) {
                    number = 0;
                } else {
                    number = Double.parseDouble(payCash.getText().toString());
                }
                String add = add(number, minFee);
                if (Double.parseDouble(add)>maxFee){
                    RxToast.showToast(R.string.tips_fee_much);
                    return;
                }
                payCash.setText(add(number, minFee));
                break;
            case R.id.minus:
                if (TextUtils.isEmpty(payCash.getText().toString())) {
                    RxToast.showToast(R.string.tips_cash_lack);
                } else if (Double.parseDouble(payCash.getText().toString()) < Constants.cash) {
                    RxToast.showToast(R.string.tips_cash_lack);
                } else {
                    number = Double.parseDouble(payCash.getText().toString());
                    if (number <= 0) {
                    } else {
                        payCash.setText(minus(number, minFee));
                    }
                }
            default:
        }
    }


    @Override
    public void getPw(String pw) {
        AppLoader.showLoading(_mActivity);
        if (KeyUtil.getPwMessage(pw).equals(SpUtil.getInstance().getWalletPw())) {
            key = pw;
            buildTrade(pw);
            if(pwWindow != null){
                pwWindow.dismiss();
            }
        } else {
            RxToast.showToast(getResources().getString(R.string.error_pw));
        }
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String result = response.getResponse();
                if (tag.equals(Constants.tran_build)) {
                    TradeResponse tradeResponse = null;
                    try {
                        tradeResponse = new Gson().fromJson(result, TradeResponse.class);
                    } catch (Exception e) {
                        return;
                    }
                    if (tradeResponse != null) {
                        if (tradeResponse.getRtnCode().equals(Constants.RTNCODE_OK)) {
                            sendBroadcast(tradeResponse);
                        } else {
                            if (NetUtil.isNetworkAvailable(getContext())) {
                                RxToast.showToast(tradeResponse.getErrMsg());
                            } else {
                                RxToast.showToast(getResources().getString(R.string.view_no_network));
                            }
                            AppLoader.stopLoading();
                        }
                    } else {
                        AppLoader.stopLoading();
                    }
                } else {
                    AppLoader.stopLoading();
                    ResponseCode resultCode = null;
                    try {
                        resultCode = new Gson().fromJson(result, ResponseCode.class);
                    } catch (Exception e) {
                        return;
                    }
                    if (resultCode.getRtnCode() == 1) {
//                        RxToast.showToast(R.string.trade_success);
                        _mActivity.onBackPressed();
                    } else {

                    }


                }
            }
        });

    }

    public String add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal bigDecimal = new BigDecimal(b1.add(b2).doubleValue());
        return bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String minus(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal bigDecimal = new BigDecimal(b1.subtract(b2).doubleValue());
        return bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).toString();
    }
}
