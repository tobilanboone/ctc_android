package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.ui.adapter.MyRecyclerAdapter;
import kualian.dc.deal.application.wallet.WalletAccount;

/**
 * Created by admin on 2018/3/28.
 */

public class ChangeWallet extends SourceDelegate{
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;

    @Override
    public Object setLayout() {
        return R.layout.wallet_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        recyclerView = rootView.findViewById(R.id.recy_list);
        linearLayout = rootView.findViewById(R.id.create_wallet);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建钱包
            }
        });
        init();
    }
    private void init(){
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WalletApp.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        WalletDao walletDao = new WalletDao();
        List<WalletAccount> walletAccounts = walletDao.queryAll();
        // 设置适配器
        recyclerView.setAdapter(new MyRecyclerAdapter(walletAccounts));

    }
}
