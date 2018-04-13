package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.bitcoinj.params.MainNetParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.HomeBean;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.callback.NorWinListener;
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.ui.setting.HepCenterDetails;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.WalletAccount;
import kualian.dc.deal.application.wallet.coins.CtcMain;
import kualian.dc.deal.application.widget.codeView.NormalWindow;

/**
 * Created by admin on 2018/3/28.
 */

public class ImportWallet extends SourceDelegate implements NorWinListener {
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
    IGlobalCallback callbackReName = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
    private NormalWindow normalWindow;
    private TextView seed_mean,private_agreement,back,mTitle;
    private EditText pw,pw_again,seed;
    private Button next;
    private CheckBox agreement;
    private String password,password_again,seed_content;
    private static String enter;
    public static ImportWallet getInstance(String flag){
        enter = flag;
       return new ImportWallet();
    }

    @Override
    public Object setLayout() {
        return R.layout.import_wallet_by_seed;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        seed_mean = rootView.findViewById(R.id.seed_mean);
        private_agreement = rootView.findViewById(R.id.agreement_jump);
        pw = rootView.findViewById(R.id.wallet_pw);
        pw_again = rootView.findViewById(R.id.wallet_pw_again);
        seed = rootView.findViewById(R.id.seed);
        next = rootView.findViewById(R.id.import_btn);
        agreement = rootView.findViewById(R.id.agreement_cb);
        back = rootView.findViewById(R.id.toolbar_back);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getText(R.string.import_wallet));
        setOnClickViews(seed_mean,private_agreement,next,back);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Map<String,String> map = new HashMap<>();
        switch (view.getId()){
            case R.id.seed_mean:
                map.put("question", UIutils.getString(R.string.mnemonic_phrase));
                map.put("content",UIutils.getString(R.string.mnemonic_phrase_content));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.noun_explanation)));
                break;
            case R.id.agreement_jump:
                map.put("question", UIutils.getString(R.string.agreement_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_deal)));
                break;

            case R.id.import_btn:
                password = pw.getText().toString();
                password_again = pw_again.getText().toString();
                seed_content = seed.getText().toString();
                String[] split = seed_content.split(" ");
                WalletDao walletDao = new WalletDao();
                List<WalletAccount> walletAccounts = walletDao.queryAll();
                if(split.length < 12 || split.length >18){
                    ToastUtil.showToastShort(UIutils.getString(R.string.seed_error));
                }else if(!password.equals(password_again)){
                    ToastUtil.showToastShort(UIutils.getString(R.string.same_password));
                }else if(password.length() == 0){
                    ToastUtil.showToastShort(UIutils.getString(R.string.password_can_not_be_empty));
                }else if (!agreement.isChecked()){
                    ToastUtil.showToastShort(UIutils.getString(R.string.agree_privacy_clause));
                }else {
                    for (WalletAccount walletAccount : walletAccounts) {
                        if(walletAccount.getWalletId().equals(KeyUtil.genWalletIdFromSeed(seed_content))){
                            normalWindow = new NormalWindow(WalletApp.getContext(),R.layout.help_word_dialog,"", "",UIutils.getString(R.string.password_reset),UIutils.getString(R.string.cancle),UIutils.getString(R.string.sure),this);
                            normalWindow.show(view);
                            return;
                        }
                    }
                    createWallet(false,seed_content,password);

                }
                break;
            case  R.id.toolbar_back:
                _mActivity.onBackPressed();
            default:

                break;
        }
    }

    @Override
    public void pressSure() {
        createWallet(true,seed_content,password);
        if(normalWindow != null ){
            normalWindow.dismiss();
        }
    }

    @Override
    public void pressCancle() {
        if(normalWindow != null ){
            normalWindow.dismiss();
        }
    }

    @Override
    public void pressCopy(String privateKey) {

    }

//    public  String walletName;
//    public  String walletId;
//    public  String walletLoginPw;
//    public  String walletTradePw;
//    public  int walletDefaultIndex;
//    public  String wallet_seed;
//    public  String wallet_is_copy;

    private void createWallet(boolean isCover,String seed,String pw){
        WalletDao walletDao = new WalletDao();
        SpUtil.getInstance().setWalletImportName(SpUtil.getInstance().getWalletImportName()+1);
        int walletImportName = SpUtil.getInstance().getWalletImportName();
        if(isCover){
            boolean update = walletDao.update(WalletTable.WALLET_TRADE_PW, KeyUtil.getPwMessage(pw), KeyUtil.genWalletIdFromSeed(seed));
            boolean updateName = walletDao.update(WalletTable.WALLET_NAME, UIutils.getString(R.string.new_wallet)+(walletImportName), KeyUtil.genWalletIdFromSeed(seed));
            if(!update){
                ToastUtil.showToastShort(UIutils.getString(R.string.import_failure));
            }else {
                SpUtil.getInstance().setWalletName(UIutils.getString(R.string.new_wallet)+(walletImportName));
                SpUtil.getInstance().setWalletID(KeyUtil.genWalletIdFromSeed(seed));
                SpUtil.getInstance().setLoginPw(pw);
                SpUtil.getInstance().setWalletPw(pw);
                SpUtil.getInstance().setWalletSend(SpUtil.getInstance().getWalletID(),seed);
                SpUtil.getInstance().setIsSaveSeed(false);
                if (callback != null){
                    callback.executeCallback(true);
                }
                if(enter.equals("create")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.WALLET_IMPORT_SUCCESS);
                }else if(enter.equals("changePw")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.CHANGE_PASSWORD_WALLET_IMPORT_SUCCESS);
                }else if(enter.equals("walletTool")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_IMPORT_WALLET_SUCCESS);
                }


                popTo(MainDelegate.class, false);
            }
        }else {
            boolean add = walletDao.add(KeyUtil.genWalletIdFromSeed(seed), UIutils.getString(R.string.new_wallet)+(walletImportName), KeyUtil.getPwMessage(pw), KeyUtil.getPwMessage(pw), 0, seed, "false");



            if(!add){
                ToastUtil.showToastShort(UIutils.getString(R.string.import_failure));
            }else {
                SpUtil.getInstance().setWalletName(UIutils.getString(R.string.new_wallet)+(walletImportName));
                SpUtil.getInstance().setWalletID(KeyUtil.genWalletIdFromSeed(seed));
//                SpUtil.getInstance().setLoginPw(pw);
                SpUtil.getInstance().setWalletPw(pw);
                SpUtil.getInstance().setWalletSend(SpUtil.getInstance().getWalletID(),seed);
                SpUtil.getInstance().setIsSaveSeed(false);
                CtcMain coin = new CtcMain();
                CoinDao coinDao = new CoinDao();
                coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coin.getCoinIndex(), MainNetParams.get()));
                boolean coinAdd = coinDao.add(coin.getCoinResource(), coin.getCoinAddress(), coin.getCoinName(), coin.getCoinIndex(),SpUtil.getInstance().getWalletID());

                List<CoinType> coinTypes = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
//                addCoins(coinTypes);
                createWallet(coinTypes);

                if (callback != null){
                    callback.executeCallback(true);
                }
                if (callbackReName != null){
                    callbackReName.executeCallback(true);
                }

                if(enter.equals("create")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.WALLET_IMPORT_SUCCESS);
                }else if(enter.equals("changePw")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.CHANGE_PASSWORD_WALLET_IMPORT_SUCCESS);
                }else if(enter.equals("walletTool")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_IMPORT_WALLET_SUCCESS);
                }
                popTo(MainDelegate.class, false);
            }
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
    //向服务器请求创建钱包
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
    public void getServiceData(ResponseData response, String tag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
