package kualian.dc.deal.application.ui.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.web.WebDelegateHead;

/**
 * Created by idmin on 2018/2/24.
 */

public class WalletName extends SourceDelegate {
    private static final String TAG = "tag";
    private String seed;
    private TextView deal;
    public static WalletName getInstance(String seed) {
        WalletName walletName = new WalletName();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,seed);
        walletName.setArguments(bundle);

        return walletName;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_wallet_name;
    }
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        deal = rootView.findViewById(R.id.agree_deal);
        final AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.account_name);
        final TextView next = rootView.findViewById(R.id.button_next);
        seed=getArguments().getString(TAG);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString().trim())) {
                    next.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                } else {
                    next.setBackground(getResources().getDrawable(R.drawable.bg_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        rootView.findViewById(R.id.agree_deal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(WebDelegateHead.getInstance("",getResources().getString(R.string.agree_deal)));
            }
        });
        rootView.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(autoCompleteTextView.getText().toString().trim())){
                    SpUtil.getInstance().setWalletAddress(autoCompleteTextView.getText().toString());
                    CommonUtil.hideSoftInput(_mActivity);
                    SpUtil.getInstance().setWalletName(autoCompleteTextView.getText().toString());
                    start(WalletLoginWord.getInstance(false,seed));
                }
            }
        });
    }
}
