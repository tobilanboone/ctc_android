package kualian.dc.deal.application.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;

/**
 * Created by admin on 2018/3/28.
 */

public class MyHelpAdapter extends RecyclerView.Adapter<MyHelpAdapter.MyHolder> {
    private Context mContext;
    private List<Map<String,String>> listContent;
    private OnItemClickListener mItemClickListener;

    public MyHelpAdapter(List<Map<String,String>> list) {
        super();
        this.mContext = WalletApp.getContext();
        this.listContent = list;
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return listContent.size();
    }

    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyHolder holder, int position) {
        // TODO Auto-generated method stub
        final Map<String, String> stringStringMap = listContent.get(position);
        String listText = stringStringMap.get("question");
        holder.textView.setText(listText);
        //如果设置了回调，则设置点击事件
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(stringStringMap);

                }
            });
        }
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.help_list_item, null);
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
        void onItemClick(Map<String,String> map);
    }
    //定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

}
