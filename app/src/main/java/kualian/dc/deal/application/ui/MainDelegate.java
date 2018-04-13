package kualian.dc.deal.application.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.MySupportFragment;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.HomeBean;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.bean.TabEntity;
import kualian.dc.deal.application.bean.UpdateRequestBean;
import kualian.dc.deal.application.bean.UpdateResponseBean;
import kualian.dc.deal.application.callback.CustomTabEntity;
import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.ui.account.FindDelegate;
import kualian.dc.deal.application.ui.account.WalletDelegate;
import kualian.dc.deal.application.ui.activity.LoginActivity;
import kualian.dc.deal.application.ui.create.RestoreDelegate;
import kualian.dc.deal.application.ui.news.NewsDelegate;
import kualian.dc.deal.application.ui.setting.CreateWallet;
import kualian.dc.deal.application.ui.setting.SettingDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.widget.CommonTabLayout;
import kualian.dc.deal.application.widget.codeView.CodeView;
import kualian.dc.deal.application.widget.codeView.KeyboardView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by idmin on 2018/2/10.
 */

public class MainDelegate extends SourceDelegate {
//    private EditText wallet_name,wallet_pw,wallet_pw_again;
//    private TextView pw_error;
    private CommonTabLayout commonTabLayout;
    private ArrayList<MySupportFragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private boolean isCanShow = true;
    private int[] mIconUnselectIds = {
            R.mipmap.tab_wallet_nor, R.mipmap.tab_candy_nor, R.mipmap.tab_zx_normal, R.mipmap.tab_mine_nor};
    private int[] mIconSelectIds = {
            R.mipmap.tab_wallet_press, R.mipmap.tab_candy_press, R.drawable.tab_zx_press,  R.drawable.tab_mine_press};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    public static MainDelegate getInstance() {
        return new MainDelegate();
    }

    @Override
    protected void onEvent() {
        checkUpdateNet();
    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    //升级检测
    private void checkUpdateNet() {
        UpdateRequestBean requestBean = new UpdateRequestBean();
        UpdateRequestBean.HeaderBean headerBean = new UpdateRequestBean.HeaderBean(KeyUtil.getRandom());
        headerBean.setTrancode(Constants.update_query);
        requestBean.setHeader(headerBean);
        UpdateRequestBean.BodyBean bodyBean=new UpdateRequestBean.BodyBean();
        requestBean.setBody(bodyBean);
        presenter.queryServiceData(new Gson().toJson(requestBean), Constants.update_query);
    }
    @Override
    protected void lazyFetchData() {
        if (findChildFragment(WalletDelegate.class) == null) {
            mTitles = getResources().getStringArray(R.array.sort_item);
            mFragments.add(WalletDelegate.getInstance());
            mFragments.add(FindDelegate.getInstance());
            mFragments.add(NewsDelegate.getInstance());
           // mFragments.add(ContactDelegate.getInstance());
            mFragments.add(SettingDelegate.getInstance());
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
            }
            commonTabLayout.setTabData(mTabEntities, _mActivity, R.id.fragment_contain, mFragments, true);
            commonTabLayout.setCurrentTab(0);
            final ISupportFragment[] delegateArray = mFragments.toArray(new ISupportFragment[4]);
            getSupportDelegate().loadMultipleRootFragment(R.id.fragment_contain, 0, delegateArray);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.main_page_layout;
    }

    View view, root;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        root = rootView;
        commonTabLayout = rootView.findViewById(R.id.common_tab);
//        SpUtil.getInstance().getWalletPw() == null
        if (SpUtil.getInstance().getWalletPw() == null) { //TODO:待修改
            ViewStub viewStub = rootView.findViewById(R.id.view_stub);
            viewStub.setLayoutResource(R.layout.main_layout);

            view = viewStub.inflate();
            TextView masonry = (TextView) view.findViewById(R.id.masonry);
            TextView create = (TextView) view.findViewById(R.id.begin_creat);
//            pw_error = ((TextView) view.findViewById(R.id.wallet_pw_error_tip));
//            wallet_name = (EditText) view.findViewById(R.id.wallet_name);
//            wallet_pw = (EditText) view.findViewById(R.id.wallet_pw);
//            wallet_pw_again = (EditText) view.findViewById(R.id.wallet_pw_again);
            setOnClickViews(masonry, create);
        } else {
            if (SpUtil.getInstance().getIsReLogin()) {
                ViewStub viewStub = rootView.findViewById(R.id.view_stub);
                viewStub.setLayoutResource(R.layout.delegate_login);
                view = viewStub.inflate();
                final KeyboardView keyboardView = view.findViewById(R.id.password_input);
                CodeView codeView = view.findViewById(R.id.password_view);
                LinearLayoutCompat contain=view.findViewById(R.id.code_contain);
                codeView.setShowType(CodeView.SHOW_TYPE_PASSWORD);
                codeView.setLength(6);
                keyboardView.setCodeView(codeView);
                codeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keyboardView.show();
                    }
                });
                codeView.setListener(new CodeView.Listener() {
                    @Override
                    public void onValueChanged(String value) {

                    }

                    @Override
                    public void onComplete(String value) {
                        // TODO: 2017/2/5
                        if (KeyUtil.getPwMessage(value).equals(SpUtil.getInstance().getLoginPw())) {
                            if (view != null) {
                                view.setVisibility(View.GONE);
                            }
                        } else {
                            Latte.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtil.setTranslateAnimationX(contain);
                                    WalletTool.vibrateOnce(_mActivity, 200);
                                    codeView.clear();
                                    RxToast.showToast(getResources().getString(R.string.error_pw));
                                }
                            },200);

                        }
                    }
                });
                view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isCanShow = false;
                        start(RestoreDelegate.newInstance(null));
                    }
                });
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.begin_creat:
                MobclickAgent.onEvent(getContext(), UmengConstants.WALLET_CREATE);
                start(CreateWallet.getInstance("first"));
                break;
            case R.id.masonry:
                Intent intent = new Intent(_mActivity,LoginActivity.class);
                startActivity(intent);
                break;
        }
//        if (view.getId() == R.id.account_restore) {
//            start(new ImportWallet());//恢复钱包
//        } else {
           /* *//*String seedStr = "hello word this is china";
            // 生成主私钥
            DeterministicKey masterKey = CTCKeyUtil.genMasterPriKey(seedStr);
            Yjw yjw = CTCKeyUtil.genMasterPriKey(seedStr);
            // 生成CTC的子私钥与地址
            ECKey ctcPriKey = CTCKeyUtil.genSubKeyFromMasterKey(masterKey, 3);
            String ctcPriKeyWif = CTCKeyUtil.genCTCPriKeyWif(ctcPriKey);
            String ctcAddr = CTCKeyUtil.genCTCAddress(ctcPriKey);
            String ctcPubKeyBase58 = CTCKeyUtil.genCTCPubKeyBase58(ctcPriKey);
            System.out.println("CTC的私钥：" + ctcPriKeyWif );
            System.out.println("CTC的地址："  + ctcAddr );
            System.out.println("CTC的BASE58公钥：" + ctcPubKeyBase58);*//*


            YjwCh yjwCh = new YjwCh("first","second");
            SpUtil.getInstance().setObject("yjw",yjwCh);
            YjwCh yjw = SpUtil.getInstance().getObject("yjw", YjwCh.class);
            yjw.test();

            DeterministicKey masterKey = CTCKeyUtil.genMasterPriKey("");*/
//            if (wallet_name.getText().toString().isEmpty()){
//                Toast.makeText(WalletApp.getContext(),"钱包名称不能为空",Toast.LENGTH_LONG).show();
//            }else if (wallet_pw.getText().toString().length() < 8){
//                pw_error.setVisibility(View.VISIBLE);
//            }else if (!wallet_pw.getText().toString().equals(wallet_pw_again.getText().toString())){
//                Toast.makeText(WalletApp.getContext(),"两次密码不一致",Toast.LENGTH_LONG).show();
//            }else{
//                SpUtil.getInstance().setWalletPw(wallet_pw.getText().toString());
//                String walletPw = SpUtil.getInstance().getWalletPw();
//                String p = wallet_pw.getText().toString();
//                String seedWordStr = KeyUtil.getSeedWordStr();
//
//                SpUtil.getInstance().setWalletName(wallet_name.getText().toString());
//
//                SpUtil.getInstance().setIsSaveSeed(false);
//
//
//                //保存seed
//                WalletDao walletDao = new WalletDao();
//                SpUtil.getInstance().setWalletID(KeyUtil.genWalletIdFromSeed(seedWordStr));
//                SpUtil.getInstance().setWalletSend(SpUtil.getInstance().getWalletID(), seedWordStr);
//                walletDao.add(KeyUtil.genWalletIdFromSeed(seedWordStr),SpUtil.getInstance().getWalletName(),SpUtil.getInstance().getLoginPw(), walletPw,0,seedWordStr,"false");
//                //添加货币
//                CtcMain coin = new CtcMain();
//                CoinDao coinDao = new CoinDao();
//                coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coin.getCoinIndex(), MainNetParams.get()));
//                coinDao.add(coin.getCoinResource(), coin.getCoinAddress(), coin.getCoinName(), coin.getCoinIndex(),SpUtil.getInstance().getWalletID());
//
//                List<CoinType> coinTypes = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
//                createWallet(coinTypes);
//                addCoins(coinTypes);
//
//                start(new SaveSeed());//创建钱包成功
//            }
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (SpUtil.getInstance().getWalletPw() != null && isCanShow) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
            IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
            if (callback != null){
                callback.executeCallback(true);
            }

        }


    }
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            RxToast.showToast(R.string.tips_exit);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {
        if(tag.equals(Constants.update_query)){
            UpdateResponseBean responseBean = new Gson().fromJson(response.getResponse(), UpdateResponseBean.class);
            if (responseBean!=null&&responseBean.getData()!=null){
                SpUtil.getInstance().setVersion(responseBean.getData().getVersion());
                SpUtil.getInstance().setUpdateUrl(responseBean.getData().getDownload_url());
            }
        }else {
            LogUtils.e("CTC--",tag);
        }

    }
}
