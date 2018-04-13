package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.callback.NorWinListener;
import kualian.dc.deal.application.callback.OnPwListener;
import kualian.dc.deal.application.ui.create.ImportWallet;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.widget.codeView.ExportPrivateKeyWindow;
import kualian.dc.deal.application.widget.codeView.PwWindow;

/**
 * Created by admin on 2018/3/25.
 */

public class WalletTool extends SourceDelegate implements OnPwListener, NorWinListener {
    private TextView revise_pw,export_private_key,export_keystore,create_wallet,import_wallet,delete_wallet,mTitle,export_pub_key;
    private PwWindow pwWindow;
    private ExportPrivateKeyWindow dialog;
    private View view;
    private String flag = "";

    @Override
    public Object setLayout() {
        return R.layout.wallet_tool;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        revise_pw = rootView.findViewById(R.id.revise_pw);
        export_private_key = rootView.findViewById(R.id.export_private_key);
//        export_keystore = rootView.findViewById(R.id.export_keystore);
        create_wallet = rootView.findViewById(R.id.create_wallet);
        import_wallet = rootView.findViewById(R.id.import_wallet);
        delete_wallet = rootView.findViewById(R.id.delete_wallet);
        export_pub_key = rootView.findViewById(R.id.export_pub_key);
        setOnClickViews(revise_pw,export_private_key,export_keystore,create_wallet,import_wallet,delete_wallet,export_pub_key);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.wallet_tool);
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
        this.view = view;
        switch (view.getId()){
            case R.id.revise_pw:
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CLICK_CHANGE_PASSWORD);
                start(new RevisePw());
                break;
            case R.id.export_private_key:
                MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.CLICK_EXPORT_PRIVATEKEY);
                flag = "private";
                pwWindow = new PwWindow(WalletApp.getContext(), WalletTool.this);
                pwWindow.show(view);
                break;
            case R.id.export_pub_key:
                flag = "pub";
                pwWindow = new PwWindow(WalletApp.getContext(), WalletTool.this);
                pwWindow.show(view);
                break;
//            case R.id.export_keystore:
////                start(new ExportKeystore());
//                break;
            case R.id.create_wallet:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_WALLET_CREATE);
                start(CreateWallet.getInstance("mine"));
                break;
            case R.id.import_wallet:
                start(ImportWallet.getInstance("walletTool"));
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_IMPORT_WALLET);
                break;
            case R.id.delete_wallet:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.MINE_DELETE_WALLET);
                start(new DeleteWallet());
                break;
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
                FragmentManager fragmentManager = getFragmentManager();
                dialog = new ExportPrivateKeyWindow(flag,this);
                dialog.show(getFragmentManager(),"dialog");
                /*exportPrivateKeyWindow = new ExportPrivateKeyWindow(WalletApp.getContext(), WalletTool.this);
                exportPrivateKeyWindow.show(view);*/
            }
        }else {
            ToastUtil.showToastShort(getResources().getString(R.string.error_pw));
        }
    }


    @Override
    public void pressSure() {

    }

    @Override
    public void pressCancle() {
        dialog.dismiss();
    }

    @Override
    public void pressCopy(String privateKey) {
        MobclickAgent.onEvent(WalletApp.getContext(),UmengConstants.COPY_PRIVATEKEY);
        RxToast.showToast(R.string.copy_private_ok);
        CommonUtil.copyContent(_mActivity, privateKey);
        dialog.dismiss();
    }
}
