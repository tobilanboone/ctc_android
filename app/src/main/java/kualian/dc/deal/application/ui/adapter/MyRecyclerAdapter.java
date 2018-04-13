package kualian.dc.deal.application.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.wallet.WalletAccount;

/**
 * Created by admin on 2018/3/28.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<WalletAccount> walletAccountList;
    private OnItemClickListener mItemClickListener;

    public MyRecyclerAdapter(List<WalletAccount> walletAccountList) {
        super();
        this.mContext = WalletApp.getContext();
        this.walletAccountList = walletAccountList;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return walletAccountList.size();
    }

    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyHolder holder, int position) {
        // TODO Auto-generated method stub
        holder.textView.setText(walletAccountList.get(position).getWalletName());
        //如果设置了回调，则设置点击事件
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(walletAccountList.get(position).getWalletId());

                }
            });
        }
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallet_list_item, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public MyHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.item);

        }

    }
    //item的回调接口
    public interface OnItemClickListener{
        void onItemClick(String walletId);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

}
