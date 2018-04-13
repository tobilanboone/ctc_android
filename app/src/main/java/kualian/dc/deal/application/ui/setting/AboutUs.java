package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.window.UIutils;

/**
 * Created by admin on 2018/3/25.
 */

public class AboutUs extends SourceDelegate {
    private TextView use_agreement,private_term,version_log,version_update,setting_connect;
    @Override
    public Object setLayout() {
        return R.layout.about_us;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        use_agreement = rootView.findViewById(R.id.use_agreement);
        version_log = rootView.findViewById(R.id.version_log);
        version_update = rootView.findViewById(R.id.version_update);
        setting_connect = rootView.findViewById(R.id.setting_connect);
        setOnClickViews(use_agreement,version_log,version_update,private_term,setting_connect);
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
        Map<String,String> map = new HashMap<>();
        switch (view.getId()){
            case R.id.use_agreement:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.APP_TERMS_OF_USE);
                map.clear();
                map.put("question", UIutils.getString(R.string.agreement_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_deal)));
                break;
            case R.id.version_log:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.APP_VERSION_LOG);
                map.clear();
                map.put("question", UIutils.getString(R.string.version_log));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_update_tips)));
                break;
            case R.id.version_update:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.APP_CHECK_UPDATE);
                break;
            case R.id.setting_connect:
                MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.APP_CONTACT_US);
                start(new ConnectUs());
                break;
            default:

                break;
        }
    }
}
