package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.RecordResponse;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/7.
 */

public class TradeSuccessDelegate extends SourceDelegate {
    private static final String TAG="TradeSuccessDelegate";
    private TextView mTradeTime, mTradeAddress, mTradeMessage, mCoinNum, mTradeId,tranState,payAddress;
    private ImageView mCoinIcon,type;
    private RecordResponse.DataBean.TradeBean tradeBean;
    public static TradeSuccessDelegate getInstance(RecordResponse.DataBean.TradeBean coinType){
        Bundle bundle=new Bundle();
        bundle.putSerializable(TAG,coinType);

        TradeSuccessDelegate trade=new TradeSuccessDelegate();
        trade.setArguments(bundle);
        return trade;

    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_trade_success;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTradeAddress = rootView.findViewById(R.id.receive_address);
        payAddress = rootView.findViewById(R.id.pay_address);
        mTradeTime = rootView.findViewById(R.id.trade_time);
        mTradeMessage = rootView.findViewById(R.id.trade_remark);
        mTradeId = rootView.findViewById(R.id.trade_id);
        mCoinNum = rootView.findViewById(R.id.coin_num);
        tranState = rootView.findViewById(R.id.tran_state);
        type = rootView.findViewById(R.id.tran_type);
        mCoinIcon = rootView.findViewById(R.id.coin_icon);
        findToolBar(rootView);
        setOnClickViews(toolBack);
        tradeBean= (RecordResponse.DataBean.TradeBean) getArguments().getSerializable(TAG);
        toolNext.setVisibility(View.GONE);
        toolTitle.setVisibility(View.VISIBLE);
        toolTitle.setText(R.string.record_detail);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        mTradeTime.setText(CommonUtil.stampToDate(tradeBean.getTranTime()));
        mTradeMessage.setText(tradeBean.getBak());

        mCoinNum.setText(tradeBean.getTranAmt()+" CTC");
        mTradeId.setText(tradeBean.getTxId());
        String address = KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID())), 3, null);
        if (tradeBean.getTranType().equals("1")){
            type.setBackgroundResource(R.mipmap.icon_zc);

            payAddress.setText(address);
            mTradeAddress.setText(tradeBean.getTargetAddr());
        }else {
            type.setBackgroundResource(R.mipmap.icon_zr);
            payAddress.setText(tradeBean.getTargetAddr());
            mTradeAddress.setText(address);
        }
        if (tradeBean.getTranState().equals("1")) {
            tranState.setText(R.string.record_unsure);
            tranState.setTextColor(getResources().getColor(R.color.text_red));
            mCoinIcon.setBackgroundResource(R.mipmap.icon_clz);
        } else if (tradeBean.getTranState().equals("2")) {
            tranState.setText(R.string.record_success);
            tranState.setTextColor(getResources().getColor(R.color.green));
            mCoinIcon.setBackgroundResource(R.mipmap.icon_cg);
        } else {
            tranState.setText(R.string.record_invalid);
            tranState.setTextColor(getResources().getColor(R.color.text_red));
            mCoinIcon.setBackgroundResource(R.mipmap.icon_failure);
        }
       // mTradeTime.setText(tradeBean.getTranTime());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.toolbar_back){
            _mActivity.onBackPressed();
        }

    }
}
