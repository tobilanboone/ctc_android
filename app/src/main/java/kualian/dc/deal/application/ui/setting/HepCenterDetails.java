package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by admin on 2018/3/31.
 */

public class HepCenterDetails extends SourceDelegate {
    private TextView mTitle,back,question,content;

    private static String questionT = "",contentT = "",titleT = "";
    public static HepCenterDetails getInStance(Map<String,String> map,String title){
        questionT = map.get("question");
        contentT = map.get("content");
        titleT = title;
        return new HepCenterDetails();
    }
    @Override
    public Object setLayout() {
        return R.layout.help__center_details;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        mTitle = rootView.findViewById(R.id.toolbar_title);
        back = rootView.findViewById(R.id.toolbar_back);
        question = rootView.findViewById(R.id.question);
        content = rootView.findViewById(R.id.content);
        mTitle.setText(titleT);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
        question.setMovementMethod(ScrollingMovementMethod.getInstance());
        if(questionT == null || questionT.length() == 0){
            question.setVisibility(View.GONE);
        }else{
            question.setText(questionT);
        }
        question.setMovementMethod(ScrollingMovementMethod.getInstance());
        content.setText(contentT);


    }
}
