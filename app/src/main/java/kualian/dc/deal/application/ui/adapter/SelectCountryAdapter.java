package kualian.dc.deal.application.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.CountryBean;
import kualian.dc.deal.application.util.Constants;

/**
 * Created by admin on 2018/4/10.
 */

public class SelectCountryAdapter extends RecyclerView.Adapter<SelectCountryAdapter.MyHolder> {
    private boolean flag = false;
    private Context mContext;
    private List<CountryBean>countryBeanList;
    private SelectCountryAdapter.OnItemClickListener mItemClickListener;

    public SelectCountryAdapter(List<CountryBean> countryBeanList) {
        super();
        this.mContext = WalletApp.getContext();
        this.countryBeanList = countryBeanList;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return countryBeanList.size();
    }
    // 填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(SelectCountryAdapter.MyHolder holder, int position) {
        // TODO Auto-generated method stub
        Locale aDefault = Locale.getDefault();
        String language = aDefault.getLanguage();
        if (language.equals(Constants.LANGAE_ZH)){
            holder.country.setText(countryBeanList.get(position).getCountryZH());
        }else {
            holder.country.setText(countryBeanList.get(position).getCountryEN());
        }

        //如果设置了回调，则设置点击事件
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.country.getText().toString(),countryBeanList.get(position).getCountryCode());
                    holder.countrySelect.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public SelectCountryAdapter.MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.country_item, null);
        SelectCountryAdapter.MyHolder holder = new SelectCountryAdapter.MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        private TextView country;
        private TextView countrySelect;

        public MyHolder(View view) {
            super(view);
            country = (TextView) view.findViewById(R.id.country);
            countrySelect = (TextView) view.findViewById(R.id.select);

        }

    }
    //item的回调接口
    public interface OnItemClickListener{
        void onItemClick(String country,String code);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(SelectCountryAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
