package kualian.dc.deal.application.widget.codeView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.NorWinListener;

/**
 * Created by admin on 2018/3/27.
 */

public class NormalWindow extends PopupWindow{
    private int layout;
    private String title,content,cancle,sure;
    private CodeView codeView;
    private LinearLayout contain;
    private NorWinListener norWinListener;
//    public NormalWindow(Context context) {
//
//        init(context);
//    }

    public NormalWindow(Context context, int layout,String title,String tip,String content,String cancle,String sure,NorWinListener norWinListener) {
        this.layout = layout;
        this.title = title;
        this.content = content;
        this.cancle = cancle;
        this.sure = sure;
        this.norWinListener = norWinListener;
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(layout, null);
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
        LinearLayout linearLayout = contentView.findViewById(R.id.nor_liner);
        TextView contentV = contentView.findViewById(R.id.nor_content);
        TextView cancleV = linearLayout.findViewById(R.id.nor_cancle);
        TextView sureV = linearLayout.findViewById(R.id.nor_sure);
        contentV.setText(content);
        cancleV.setText(cancle);
        sureV.setText(sure);
        cancleV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (norWinListener != null){
                    norWinListener.pressCancle();
                }
            }
        });
        sureV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (norWinListener != null){
                    norWinListener.pressSure();
                }
            }
        });
        /*if (isSetting) {
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
        }*/

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
    }
}
