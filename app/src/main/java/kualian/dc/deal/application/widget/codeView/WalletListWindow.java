package kualian.dc.deal.application.widget.codeView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.callback.WalletListListener;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.ui.adapter.MyRecyclerAdapter;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.ToastUtil;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.wallet.WalletAccount;

/**
 * Created by admin on 2018/3/28.
 */

public class WalletListWindow extends PopupWindow implements MyRecyclerAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout,listLinear;
    private WalletListListener walletListListener;

    public WalletListWindow(WalletListListener walletListListener) {
        this.walletListListener = walletListListener;
        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(WalletApp.getContext()).inflate(R.layout.wallet_list, null);
        setContentView(contentView);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
        initView(contentView);
    }

    // 弹出PopupWindow
    public void show(View rootView) {
        showAtLocation(rootView, Gravity.TOP | Gravity.RIGHT, 0, 0);
    }

    private void initView(View contentView) {
        listLinear = contentView.findViewById(R.id.list_linear);
        TextView background = contentView.findViewById(R.id.background);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recyclerView = listLinear.findViewById(R.id.recy_list);

        linearLayout = listLinear.findViewById(R.id.create_wallet);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建钱包
                walletListListener.change("create");
            }
        });
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WalletApp.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        // 设置适配器
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(walletAccounts);
        recyclerView.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(String walletId) {
        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        WalletAccount walletAccount = new WalletAccount();
        for (WalletAccount walletAccount1 : walletAccounts) {
            if (walletAccount1.getWalletId().equals(walletId)){
                walletAccount = walletAccount1;
            }
        }
        if(TextUtils.isEmpty(walletAccount.getWalletId())){
            walletListListener.change("change");
            ToastUtil.showToastShort(UIutils.getString(R.string.switch_failure));
            return;
        }
        //切换当前钱包类型
        boolean isSaveSeed = walletAccount.getWallet_is_copy().equals("true");
        SpUtil.getInstance().setIsSaveSeed(isSaveSeed);
        SpUtil.getInstance().setWalletName(walletAccount.getWalletName());
        SpUtil.getInstance().setWalletID(walletAccount.getWalletId());
//        SpUtil.getInstance().setLoginPw(walletAccount.getWalletLoginPw()+"");
        SpUtil.getInstance().setWalletPw(walletAccount.getWalletTradePw(),false);
        SpUtil.getInstance().setWalletSend(SpUtil.getInstance().getWalletID(),walletAccount.getWallet_seed());
        //通知首页
        walletListListener.change("change");

    }
}
