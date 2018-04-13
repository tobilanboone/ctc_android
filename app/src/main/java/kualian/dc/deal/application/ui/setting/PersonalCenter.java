package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.wallet.WalletAccount;

/**
 * Created by idmin on 2018/3/21.
 */

public class PersonalCenter extends SourceDelegate {
    private TextView mKey;
    private Button btn;
    private AutoCompleteTextView mNick;
    private static final String TAG="tag";
    private WalletDao walletDao=new WalletDao();
    public static PersonalCenter getInstance(String nick){
        PersonalCenter personalCenter =new PersonalCenter();
        Bundle bundle =new Bundle();
        bundle.putString(TAG,nick);
        personalCenter.setArguments(bundle);
        return personalCenter;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        btn = rootView.findViewById(R.id.delete_wallet);


        mKey = rootView.findViewById(R.id.setting_key);
        mNick = rootView.findViewById(R.id.setting_nick);
        String string = getArguments().getString(TAG);
        mNick.setText(string);
        findToolBar(rootView);
        toolTitle.setText(R.string.setting_personal);
        toolNext.setText(R.string.sure);
        setOnClickViews(mKey,toolBack,btn,toolNext);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.setting_key:
                start(new PrivateKeyDelegate());
                break;
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
                _mActivity.onBackPressed();
                break;
            case R.id.delete_wallet:
                break;
            case R.id.toolbar_next:
                String name = mNick.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    RxToast.showToast(R.string.tips_name_empty);
                }else {
                    List<WalletAccount> walletAccounts = walletDao.queryAll();
                    if (walletAccounts.size()>0){
                        for (WalletAccount account : walletAccounts) {

                            if (account.getWalletName().equals(name)) {
                                RxToast.showToast(R.string.tips_name_repetition);
                                return;
                            }
                        }
                    }
                    walletDao.update(WalletTable.WALLET_NAME,name, SpUtil.getInstance().getWalletID());
                    SpUtil.getInstance().setWalletName(name);

                    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_NAME_CHANGE);
                    if (callback!=null){
                        callback.executeCallback(null);
                    }
                    CommonUtil.hideSoftInput(_mActivity);
                    _mActivity.onBackPressed();
                }
                break;
        }
    }
}
