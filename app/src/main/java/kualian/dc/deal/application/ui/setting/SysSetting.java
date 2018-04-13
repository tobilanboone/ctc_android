package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by admin on 2018/3/25.
 */

public class SysSetting extends SourceDelegate {
    private TextView mTitle,language,icon;
    @Override
    public Object setLayout() {
        return R.layout.sys_setting;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        language = rootView.findViewById(R.id.select_language);
        icon = rootView.findViewById(R.id.select_icon);
        mTitle.setText(R.string.sys_setting);
        rootView.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
        setOnClickViews(language,icon);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.select_language:
                start(SettingDefault.getInstance(false));
                break;
            case R.id.select_icon:
                start(SettingDefault.getInstance(true));
                break;
            default:

                break;
        }
    }
}
