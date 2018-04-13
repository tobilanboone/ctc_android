package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.window.UIutils;

/**
 * Created by admin on 2018/3/29.
 */

public class HelpCenter extends SourceDelegate {

    private View back;
    private TextView mTitle,noun_explanation,transfer_transactions,forget_pw,wallet_safe,wallet_operation;
    private List<Map<String,String>> listContent = new ArrayList<>();
    private Map<String,String> map;

    @Override
    public Object setLayout() {
        return R.layout.common_pro;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        back = rootView.findViewById(R.id.toolbar_back);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        noun_explanation = rootView.findViewById(R.id.noun_explanation);
        transfer_transactions = rootView.findViewById(R.id.transfer_transactions);
        forget_pw = rootView.findViewById(R.id.forget_pw);
        wallet_safe = rootView.findViewById(R.id.wallet_safe);
        wallet_operation = rootView.findViewById(R.id.wallet_operation);
        mTitle.setText(getResources().getText(R.string.help_center));
        setOnClickViews(noun_explanation,transfer_transactions,forget_pw,wallet_safe,wallet_operation);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });


    }

    @Override
    public void onClick(View view) {
        listContent.clear();
        switch (view.getId()){
            case R.id.noun_explanation:
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.mnemonic_phrase));
                map.put("content",UIutils.getString(R.string.mnemonic_phrase_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.private_question));
                map.put("content",UIutils.getString(R.string.private_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.keystore_question));
                map.put("content",UIutils.getString(R.string.keystore_content));
                listContent.add(map);
                start(HelpCenterList.getInstance(listContent,getResources().getString(R.string.noun_explanation)));
                break;
            case R.id.transfer_transactions:
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.pay_question));
                map.put("content",UIutils.getString(R.string.pay_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.payfail_question));
                map.put("content",UIutils.getString(R.string.payfail_content));
                listContent.add(map);
                start(HelpCenterList.getInstance(listContent,getResources().getString(R.string.transfer_transactions)));
                break;
            case R.id.forget_pw:
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.forget_question));
                map.put("content",UIutils.getString(R.string.forget_content));
                listContent.add(map);
                start(HelpCenterList.getInstance(listContent,getResources().getString(R.string.forget_pw)));
                break;
            case R.id.wallet_safe:
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.modify_question));
                map.put("content",UIutils.getString(R.string.modify_content));
                listContent.add(map);
                start(HelpCenterList.getInstance(listContent,getResources().getString(R.string.wallet_safe)));
                break;
            case R.id.wallet_operation:
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.create_wallet_question));
                map.put("content",UIutils.getString(R.string.create_wallet_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.copy_seeed_question));
                map.put("content",UIutils.getString(R.string.copy_seeed_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.export_privatekey_question));
                map.put("content",UIutils.getString(R.string.export_privatekey_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.import_wallet_question));
                map.put("content",UIutils.getString(R.string.import_wallet_content));
                listContent.add(map);
                map = new HashMap<String,String>();
                map.put("question", UIutils.getString(R.string.delete_wallet_question));
                map.put("content",UIutils.getString(R.string.delete_wallet_content));
                listContent.add(map);
                start(HelpCenterList.getInstance(listContent,getResources().getString(R.string.wallet_operation)));
                break;
            default:

                break;
        }
    }
}
