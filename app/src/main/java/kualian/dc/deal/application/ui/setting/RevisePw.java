package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.ui.create.ImportWallet;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.window.UIutils;

/**
 * Created by admin on 2018/3/25.
 */

public class RevisePw extends SourceDelegate {
    private TextView mTitlte;
    private TextView import_btn;
    private Button next;
    private EditText oldPw,newPw,newPwAgain;

    @Override
    public Object setLayout() {
        return R.layout.revise_pw;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTitlte =rootView.findViewById(R.id.toolbar_title);
        import_btn = rootView.findViewById(R.id.import_btn);
        next = rootView.findViewById(R.id.btn_next);
        oldPw = rootView.findViewById(R.id.old_pw);
        newPw = rootView.findViewById(R.id.old_pw);
        newPwAgain = rootView.findViewById(R.id.old_pw);
        mTitlte.setText(R.string.revise_pw);
        setOnClickViews(import_btn,next);
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
            case R.id.btn_next:
                String string = oldPw.getText().toString();

                if(!KeyUtil.getPwMessage(string).equals(SpUtil.getInstance().getWalletPw())){
                    ToastUtil.showToastShort(UIutils.getString(R.string.error_pw));
                }else if(string.length()<8){
                    ToastUtil.showToastShort(UIutils.getString(R.string.setting_login_pw_short));
                }else if (!newPw.getText().toString().equals(newPwAgain.getText().toString())){
                    ToastUtil.showToastShort(UIutils.getString(R.string.same_password));
                }else if(newPw.getText().toString().length() ==0 || newPwAgain.getText().toString().length() == 0){
                    ToastUtil.showToastShort(UIutils.getString(R.string.password_can_not_be_empty));
                }else {
                    SpUtil.getInstance().setWalletPw(KeyUtil.getPwMessage(newPw.getText().toString()));
                    WalletDao walletDao = new WalletDao();
                    boolean update = walletDao.update(WalletTable.WALLET_TRADE_PW, SpUtil.getInstance().getWalletPw(), SpUtil.getInstance().getWalletID());
                    if (update){
                        ToastUtil.showToastShort(UIutils.getString(R.string.success));
                        MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CHANGE_PASSWORD_SUCCESS);
                        _mActivity.onBackPressed();
                    }else {
                        ToastUtil.showToastShort(UIutils.getString(R.string.fail));
                    }
                }
                break;
            case R.id.import_btn:
                start(ImportWallet.getInstance("changePw"));
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CHANGE_PASSWORD_WALLET_IMPORT);
                break;
            default:

                break;
        }
    }


}
