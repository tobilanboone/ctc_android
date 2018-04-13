package kualian.dc.deal.application.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;

/**
 * Created by admin on 2018/3/26.
 */

public class ExportKeystore extends SourceDelegate {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] str = new String[]{WalletApp.getContext().getResources().getString(R.string.keystore_file),WalletApp.getContext().getResources().getString(R.string.qrcode)};
    @Override
    public Object setLayout() {
        return R.layout.export_keystore;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        viewPager = rootView.findViewById(R.id.export_keystore_vp);
        tabLayout = rootView.findViewById(R.id.export_keystore_tab);
        List<Fragment> list = new ArrayList<>();
        list.add(new ExportKeystoreFile());
        list.add(new ExportKeyStoreQrcode());
        viewPager.setAdapter(new MyAdapter(getFragmentManager(),list,str));
        tabLayout.setupWithViewPager(viewPager);
    }
    class MyAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragmentList = new ArrayList<>();
        private String[] title = new String[2];
        public MyAdapter(FragmentManager fm, List<Fragment> list,String[] strings) {
            super(fm);
            fragmentList = list;
            title = strings;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
