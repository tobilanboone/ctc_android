package kualian.dc.deal.application.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.CountryBean;
import kualian.dc.deal.application.ui.adapter.SelectCountryAdapter;

public class CountryActivity extends BaseActivity implements SelectCountryAdapter.OnItemClickListener {

    private RecyclerView recycle_country;
    private List<CountryBean>countryBeanList = new ArrayList<>();
    private TextView back;

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
        setContentView(R.layout.activity_country);
        recycle_country = (RecyclerView) findViewById(R.id.country_recycler);
        ((TextView) findViewById(R.id.toolbar_title)).setText("选择国家");
        back = ((TextView) findViewById(R.id.toolbar_back));
        back.setBackgroundResource(R.mipmap.login_icon_cancel);
    }

    @Override
    public void initData() {
        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(WalletApp.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_country.setLayoutManager(layoutManager);
        String json = WalletApp.getJson("country.json");
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String countryInfo = jsonArray.get(i).toString();
                String[] split = countryInfo.split("-");
                if (split.length == 3){
                    CountryBean countryBean = new CountryBean();
                    countryBean.setCountryEN(split[0]);
                    countryBean.setCountryZH(split[1]);
                    countryBean.setCountryCode(split[2]);
                    countryBeanList.add(countryBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 设置适配器
        SelectCountryAdapter myRecyclerAdapter = new SelectCountryAdapter(countryBeanList);
        recycle_country.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initListener() {
        setOnClickViews(back);
    }


    @Override
    public void onItemClick(String country, String code) {
        Intent intent = new Intent();
        intent.putExtra("country",country);
        intent.putExtra("code",code);
        CountryActivity.this.setResult(RESULT_OK,intent);
        CountryActivity.this.finish();
    }
}
