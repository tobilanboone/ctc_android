package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.util.window.UIutils;

/**
 * Created by admin on 2018/3/29.
 */

public class ConnectUs extends SourceDelegate {

    private TextView email,tele,facebook,title;

    @Override
    public Object setLayout() {
        return R.layout.connect_us;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        email = rootView.findViewById(R.id.email);
        tele = rootView.findViewById(R.id.telegram);
        facebook = rootView.findViewById(R.id.facbook);
        title = rootView.findViewById(R.id.toolbar_title);
        title.setText(R.string.setting_connect);
        rootView.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setOnClickViews(email,facebook,tele);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Map<String,String>map = new HashMap<>();
        switch (view.getId()){
            case R.id.email:
                map.put("question", UIutils.getString(R.string.connect_email_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_connect)));
                break;
            case R.id.telegram:
                map.put("question", UIutils.getString(R.string.connect_telegram_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_connect)));
                break;

            case R.id.facbook:
                map.put("question", UIutils.getString(R.string.connect_facebook_question));
                start(HepCenterDetails.getInStance(map,UIutils.getString(R.string.setting_connect)));
                break;
        }
    }
}
