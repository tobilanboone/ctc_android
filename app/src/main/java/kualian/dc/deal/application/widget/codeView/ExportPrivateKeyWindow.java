package kualian.dc.deal.application.widget.codeView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bitcoinj.crypto.DeterministicKey;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.callback.NorWinListener;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by admin on 2018/3/29.
 */

public class ExportPrivateKeyWindow extends DialogFragment {

    private View mLayout;
    private LayoutInflater mInflater;
    private Context mContext;
    private Button comm_bt;
    private NorWinListener norWinListener;
    private EditText privateKey;
    private String key_type;

    public ExportPrivateKeyWindow (){

    }

    @SuppressLint("ValidFragment")
    public ExportPrivateKeyWindow (String flag,NorWinListener norWinListener){
        this.key_type = flag;
        this.norWinListener = norWinListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
    }
    private void autoFocus() {
        InputMethodManager imm = (InputMethodManager) WalletApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.export_private_key, container);
        mLayout.setBackgroundDrawable(new ColorDrawable(0xaa000000));
//        mLayoutsetHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setFocusable(true);
//        setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
//        setBackgroundDrawable(new ColorDrawable(0xAA000000)); // 加上一层黑色透明背景
        LinearLayout linearLayout = mLayout.findViewById(R.id.export_linear);
        LinearLayout button_linear = linearLayout.findViewById(R.id.button_linear);

        privateKey = linearLayout.findViewById(R.id.private_key);
        TextView cancle = button_linear.findViewById(R.id.cancel);
        TextView sure = button_linear.findViewById(R.id.sure);
        String walletSend = SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID());
        DeterministicKey deterministicKey = KeyUtil.genMasterPriKey(walletSend);
        if(key_type.equals("private")){
            privateKey.setText(KeyUtil.genSubPriKeyWifFromMasterKey(deterministicKey,3, null));
        }else if(key_type.equals("pub")){
            TextView tip = linearLayout.findViewById(R.id.safe_tip);
            tip.setVisibility(View.GONE);
            privateKey.setText(KeyUtil.getCTCPubKey(walletSend));
        }
//        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        autoFocus();
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(norWinListener != null){
                    norWinListener.pressCancle();
                }
//                if(StringUtil.isEmpty(comm_et.getText().toString().trim())){
//                    Toast.makeText(mContext, "请输入评论内容", 0).show();
//                    return ;
//                }
//                comment();
            }


        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(norWinListener != null){
                    norWinListener.pressCopy(privateKey.getText().toString());
                }
            }
        });

        /*comm_et.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });*/
        return mLayout;
    }








    /*private CodeView codeView;
    private LinearLayout contain;
    private NorWinListener norWinListener;
    public ExportPrivateKeyWindow(Context context) {

        init(context);
    }

    public ExportPrivateKeyWindow(Context context, NorWinListener norWinListener) {

        this.norWinListener = norWinListener;
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.export_private_key, null);
        setContentView(contentView);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setClippingEnabled(false); // 让PopupWindow同样覆盖状态栏
        setBackgroundDrawable(new ColorDrawable(0xAA000000)); // 加上一层黑色透明背景
        initView(contentView);
    }

    // 弹出PopupWindow
    public void show(View rootView) {
        showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    private void initView(View contentView) {
        LinearLayout linearLayout = contentView.findViewById(R.id.export_linear);
        LinearLayout button_linear = linearLayout.findViewById(R.id.button_linear);

        EditText peivateKey = linearLayout.findViewById(R.id.private_key);
        TextView cancle = button_linear.findViewById(R.id.cancel);
        TextView sure = button_linear.findViewById(R.id.sure);
        String walletSend = SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletID());
        DeterministicKey deterministicKey = KeyUtil.genMasterPriKey(walletSend);
        peivateKey.setText(KeyUtil.genSubPriKeyWifFromMasterKey(deterministicKey,3, null));
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (norWinListener != null){
                    norWinListener.pressCancle();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (norWinListener != null){
                    norWinListener.pressSure();
                }
            }
        });
        *//*if (isSetting) {
            final TextView textView = contentView.findViewById(R.id.wallet_pw_tip);
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            contentView.findViewById(R.id.wallet_pw_tip).setVisibility(View.VISIBLE);
            ivClose.setVisibility(View.GONE);
            title.setText(R.string.account_pw);
        } else {
            title.setText(R.string.account_input_pw);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }*//*

//        final KeyboardView keyboardView = (KeyboardView) contentView.findViewById(R.id.password_input);
//        codeView = (CodeView) contentView.findViewById(R.id.password_view);
//        contain =  contentView.findViewById(R.id.code_contain);
//        codeView.setShowType(CodeView.SHOW_TYPE_PASSWORD);
//        codeView.setLength(6);
//        keyboardView.setCodeView(codeView);
//        codeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyboardView.show();
//            }
//        });
//        codeView.setListener(new CodeView.Listener() {
//            @Override
//            public void onValueChanged(String value) {
//                // TODO: 2017/2/5  内容发生变化
//            }
//
//            @Override
//            public void onComplete(String value) {
//                if (onPwListener != null) {
//                    onPwListener.getPw(value);
//                }
//                // TODO: 2017/2/5 输入完成
//            }
//        });
    }

    public CodeView getCodeView() {
        return codeView;
    }
    public LinearLayout getContainView() {
        return contain;
    }*/
}
