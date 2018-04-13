package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by idmin on 2018/2/10.
 */

public class FindDelegate extends SourceDelegate{
    private static FindDelegate instance=null;
    private WebView webview;

    public static FindDelegate getInstance() {

        return   new FindDelegate();
    }
    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getViewImp() {
        return null;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_find;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        rootView.findViewById(R.id.toolbar_back).setVisibility(View.INVISIBLE);
        TextView textView = rootView.findViewById(R.id.toolbar_title);
        textView.setText(R.string.candy);
        webview = rootView.findViewById(R.id.find_webview);
//        webview.loadUrl("");
    }
}
