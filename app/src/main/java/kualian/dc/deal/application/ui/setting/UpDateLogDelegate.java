package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by idmin on 2018/3/13.
 */

public class UpDateLogDelegate extends SourceDelegate{
    @Override
    public Object setLayout() {
        return R.layout.delegate_update_log;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        findToolBar(rootView);
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
        toolTitle.setVisibility(View.VISIBLE);
        toolTitle.setText(R.string.setting_update_tips);
    }
}
