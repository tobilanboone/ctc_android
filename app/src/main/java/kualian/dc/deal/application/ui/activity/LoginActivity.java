package kualian.dc.deal.application.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.CountryBean;
import kualian.dc.deal.application.bean.RequestA;
import kualian.dc.deal.application.bean.RequestR;
import kualian.dc.deal.application.callback.ServerCallBack;
import kualian.dc.deal.application.presenter.impl.RequestUtil;

public class LoginActivity extends BaseActivity {

    private String json;
    private List<CountryBean>countryBeanList = new ArrayList<>();
    private Button next;
    private LinearLayout selectCountry;
    private TextView areaCode,country;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        StatusBarCompat.translucentStatusBar(this, true);
//        StatusBarLightMode(this);
//        initView();
//        initTitle();
//        initData();
//        initListener();
//    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.login_layout);
        next = (Button) findViewById(R.id.next);
        selectCountry = (LinearLayout) findViewById(R.id.select_country);
        areaCode = (TextView) findViewById(R.id.area_code);
        country = (TextView) findViewById(R.id.country);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        setOnClickViews(next,selectCountry);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.select_country:
                startActivityForResult(new Intent(this,CountryActivity.class),100);
                break;
            case R.id.next:
                register();
//                String old = "asdasd78asudasduasdasdaj23e3u8d323j23jdjd";
//                byte[] encode = Base64.getEncoder().encode(old.getBytes());
//                String newString  = new String(encode);
//                LogUtils.e("qwert:",old);
//                LogUtils.e("qwert:",newString);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100){
            String countryName = data.getStringExtra("country");
            String code = data.getStringExtra("code");
            country.setText(countryName);
            areaCode.setText("+"+code);
        }
    }
    private void register(){
        RequestA.packet packet = new RequestA.packet("100001");
        packet.setUsername("yanjunwei");
        packet.setPassword("123456");
        packet.setVerifyCode("123456");
        RequestUtil.execute(packet, new ServerCallBack<RequestR.responsePacket>() {
            @Override
            public void onSuccess(RequestR.responsePacket responsePacket) {

            }

            @Override
            public void onFailure() {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
