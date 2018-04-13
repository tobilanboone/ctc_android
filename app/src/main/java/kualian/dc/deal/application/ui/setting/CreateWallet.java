package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import kualian.dc.deal.application.database.CoinDao;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.ui.create.ImportWallet;
import kualian.dc.deal.application.ui.create.SaveSeed;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.coins.CtcMain;

/**
 * Created by admin on 2018/3/29.
 */

public class CreateWallet extends SourceDelegate implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    private TextView pw_error,agreement_jump,create;
    private EditText wallet_name;
    private EditText wallet_pw;
    private EditText wallet_pw_again;
    private static final String TAG = "tag";
    private static String pageTag;
    private CheckBox chexbox;
    private TextView viewById;

    public static CreateWallet getInstance(String tag) {
        CreateWallet createWallet = new CreateWallet();
        pageTag = tag;
        return createWallet;
    }
    @Override
    public Object setLayout() {
        return R.layout.create_wallet;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        TextView restore = (TextView) rootView.findViewById(R.id.account_restore);
        create = (TextView) rootView.findViewById(R.id.account_create);
        create.setBackgroundColor(getResources().getColor(R.color.hint_color));
        pw_error = ((TextView) rootView.findViewById(R.id.wallet_pw_error_tip));
        agreement_jump = ((TextView) rootView.findViewById(R.id.agreement_jump));
        wallet_name = (EditText) rootView.findViewById(R.id.wallet_name);
        wallet_pw = (EditText) rootView.findViewById(R.id.wallet_pw);
        wallet_pw_again = (EditText) rootView.findViewById(R.id.wallet_pw_again);
        toolTitle = rootView.findViewById(R.id.toolbar_title);
        toolTitle.setText(getResources().getText(R.string.create_wallet));
        chexbox = rootView.findViewById(R.id.agreement_cb);
        wallet_name.addTextChangedListener(this);
        wallet_pw.addTextChangedListener(this);
        wallet_pw_again.addTextChangedListener(this);
        chexbox.setOnCheckedChangeListener(this);
        wallet_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pw_error.setVisibility(View.VISIBLE);
                }else {
                    pw_error.setVisibility(View.GONE);
                }
            }
        });
        setOnClickViews(restore, create,agreement_jump);
        rootView.findViewById(R.id.toolbar_back).setVisibility(View.GONE);
    }
    public void onClick(View view) {
        Map<String,String> map = new HashMap<>();
        if (view.getId() == R.id.account_restore) {
            start(ImportWallet.getInstance("create"));//恢复钱包
            MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.WALLET_IMPORT);
        } else if(view.getId() ==R.id.agreement_jump){
                map.put("question", UIutils.getString(R.string.agreement_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_deal)));
        }else {
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
            if (wallet_name.getText().toString().isEmpty()){
                ToastUtil.showToastShort(UIutils.getString(R.string.tips_name_empty));
            }else if (wallet_pw.getText().toString().length() < 8){
                pw_error.setVisibility(View.VISIBLE);
            }else if (!wallet_pw.getText().toString().equals(wallet_pw_again.getText().toString())){
                ToastUtil.showToastShort(UIutils.getString(R.string.same_password));
            }else if(!chexbox.isChecked()){
                ToastUtil.showToastShort(UIutils.getString(R.string.agree_privacy_clause));
            }else{
                SpUtil.getInstance().setWalletPw(wallet_pw.getText().toString());
                String walletPw = SpUtil.getInstance().getWalletPw();
                String p = wallet_pw.getText().toString();
                String seedWordStr = KeyUtil.getSeedWordStr();

                SpUtil.getInstance().setWalletName(wallet_name.getText().toString());

                SpUtil.getInstance().setIsSaveSeed(false);


                //保存seed
                WalletDao walletDao = new WalletDao();
                SpUtil.getInstance().setWalletID(KeyUtil.genWalletIdFromSeed(seedWordStr));
                SpUtil.getInstance().setWalletSend(SpUtil.getInstance().getWalletID(), seedWordStr);
                walletDao.add(KeyUtil.genWalletIdFromSeed(seedWordStr),SpUtil.getInstance().getWalletName(),SpUtil.getInstance().getLoginPw(), walletPw,0,seedWordStr,"false");
                //添加货币
                CtcMain coin = new CtcMain();
                CoinDao coinDao = new CoinDao();
                coin.setCoinAddress(KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), coin.getCoinIndex(), MainNetParams.get()));
                coinDao.add(coin.getCoinResource(), coin.getCoinAddress(), coin.getCoinName(), coin.getCoinIndex(),SpUtil.getInstance().getWalletID());
                List<CoinType> coinTypes = coinDao.queryByWalletId(SpUtil.getInstance().getWalletID());
                createWallet(coinTypes);
                if(pageTag.equals("first")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.WALLET_CREATE_SUCCESS);
                }else if(pageTag.equals("index")){
                    MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.HOME_WALLET_CREATE_SUCCESS);
                }else if(pageTag.equals("mine")){
                    MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.MINE_WALLET_CREATE_SUCCESS);
                }
                start(SaveSeed.getInstance(pageTag));//创建钱包成功


            }
        }
    }

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if("none".equals(pageTag)){
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                _mActivity.finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                RxToast.showToast(R.string.tips_exit);
            }
            return true;
        }
        return super.onBackPressedSupport();
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


    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use  in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        isComplete();
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isComplete();
    }
    private boolean isComplete(){
        String name = wallet_name.getText().toString();
        String pw = wallet_pw.getText().toString();
        String pwAgain = wallet_pw_again.getText().toString();
        boolean checked = chexbox.isChecked();
        if(!checked || name.isEmpty() || pw.isEmpty() || pwAgain.isEmpty()){
            create.setBackgroundColor(getResources().getColor(R.color.hint_color));
            return false;
        }else {
            create.setBackgroundColor(getResources().getColor(R.color.btn_color));
            return true;
        }
    }
}
