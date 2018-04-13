package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.ui.adapter.MyHelpAdapter;

/**
 * Created by admin on 2018/3/31.
 */

public class HelpCenterList extends SourceDelegate implements MyHelpAdapter.OnItemClickListener {

    private RecyclerView listview;
    private static List<Map<String,String>> listContent = new ArrayList<>();
    private TextView mTitle;
    private static String titleT;
    private TextView back;

    public static HelpCenterList getInstance(List<Map<String,String>> list,String title){
        listContent = list;
        titleT = title;
        return new HelpCenterList();
    }
    @Override
    public Object setLayout() {
        return R.layout.help_center_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        listview = rootView.findViewById(R.id.help_list);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        mTitle.setText(titleT);
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WalletApp.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        // 设置适配器
        MyHelpAdapter myRecyclerAdapter = new MyHelpAdapter(listContent);
        listview.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.setOnItemClickListener(this);
        back = rootView.findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }


    @Override
    public void onItemClick(Map<String, String> map) {
        start(HepCenterDetails.getInStance(map,titleT));
    }
}
