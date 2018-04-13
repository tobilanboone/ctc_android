package kualian.dc.deal.application.ui.news;

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

public class NewsDelegate extends SourceDelegate{
    private static NewsDelegate instance=null;
    private WebView webview;
    public static NewsDelegate getInstance() {

        instance = new NewsDelegate();
        return instance;
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
        return R.layout.delegate_news;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        //ButterKnife.bind(this,rootView);
   /*   rootView.findViewById(R.id.my_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentDelegate().start(SeedDelegate.getInstance(false));
            }
        });*/
        rootView.findViewById(R.id.toolbar_back).setVisibility(View.INVISIBLE);
        TextView textView = rootView.findViewById(R.id.toolbar_title);
        textView.setText(R.string.news);
        webview = rootView.findViewById(R.id.find_webview);
//        webview.loadUrl("");
    }

}
