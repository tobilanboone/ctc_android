package kualian.dc.deal.application.ui.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.WordBean;
import kualian.dc.deal.application.callback.NorWinListener;
import kualian.dc.deal.application.database.WalletDao;
import kualian.dc.deal.application.database.WalletTable;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.UmengConstants;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.util.window.UIutils;
import kualian.dc.deal.application.widget.codeView.NormalWindow;

/**
 * 备份助记词
 */

public class DelegateVerify extends SourceDelegate implements NorWinListener {
    IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.REFRESH_DATA);
    private NormalWindow normalWindow;
    private RecyclerView select, unSelect;
    private BaseQuickAdapter selectAdapter, unSelectAdapter;
    private ArrayList<WordBean> words = new ArrayList<>();
    private ArrayList<String> selectWords = new ArrayList<>();
    private ArrayList<String> original = new ArrayList<>();
    private int size;
    private String seed;
    private static final String TAG = "tag";
    private Button btn;
    private TextView back,error,toolTitle;
    private boolean isCompleted;
    private ArrayList<String> wordList;
    public static DelegateVerify getInstance(String seed) {
        DelegateVerify delegateVerify = new DelegateVerify();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, seed);
        delegateVerify.setArguments(bundle);
        return delegateVerify;
    }

    @Override
    public Object setLayout() {
        return R.layout.help_word_03;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        btn = rootView.findViewById(R.id.button_next);
        seed = getArguments().getString(TAG);
        back = rootView.findViewById(R.id.toolbar_back);
        error = rootView.findViewById(R.id.restore_message);
        select = rootView.findViewById(R.id.recycle_select);
        unSelect = rootView.findViewById(R.id.recycle_un_select);
        toolTitle = rootView.findViewById(R.id.toolbar_title);
        toolTitle.setText(getResources().getText(R.string.save_seed));
        setOnClickViews(back,btn);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        wordList = KeyUtil.parseMnemonic(seed);
        original.addAll(wordList);
        shuffle(wordList);
        size=wordList.size();
        for (int i = 0; i < size; i++) {
            WordBean wordBean = new WordBean();
            wordBean.setContent(wordList.get(i));
            wordBean.setSelected(false);
            words.add(wordBean);
        }
        selectAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_word_select, selectWords) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                TextView view = helper.getView(R.id.word);
                view.setText(item);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isCompleted){
                            btn.setBackground(getResources().getDrawable(R.drawable.bg_white));
                            btn.setTextColor(getResources().getColor(R.color.text_gray));
                            isCompleted=false;
                        }
                        selectWords.remove(helper.getAdapterPosition());
                        selectAdapter.notifyDataSetChanged();
                        for (int i = 0; i < size; i++) {
                            if (words.get(i).getContent().equals(item)) {
                                if (words.get(i).isSelected()){
                                    words.get(i).setSelected(false);
                                    unSelectAdapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        };

        unSelectAdapter = new BaseQuickAdapter<WordBean, BaseViewHolder>(R.layout.item_word, words) {
            @Override
            protected void convert(BaseViewHolder helper, WordBean item) {
                TextView view = helper.getView(R.id.word);
                view.setText(item.getContent());
                if (item.isSelected()) {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                } else {
                    view.setBackground(null);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!item.isSelected()) {
                            view.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                            item.setSelected(true);
                            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                            selectWords.add(item.getContent());
                            selectAdapter.notifyDataSetChanged();
                            if (selectWords.size()==words.size()){
                                isCompleted=true;
                                btn.setBackground(getResources().getDrawable(R.drawable.bg_blue));
                                btn.setTextColor(getResources().getColor(R.color.white));
                            }
                        }
                    }
                });
            }
        };
        unSelectAdapter.bindToRecyclerView(unSelect);
        selectAdapter.bindToRecyclerView(select);
        unSelect.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        select.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
//        unSelect.setLayoutManager(new GridLayoutManager(getContext(), 5));
//        select.setLayoutManager(new GridLayoutManager(getContext(), 5));
        unSelect.setAdapter(unSelectAdapter);
        select.setAdapter(selectAdapter);
    }

    public <T> void shuffle(List<T> list) {
        int size = list.size();
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            // 获取随机位置
            int randomPos = random.nextInt(size);
            // 当前元素与随机元素交换
            Collections.swap(list, i, randomPos);
        }
    }
    private boolean verifyMnemonic() {
        boolean isSeedValid = true;
        for (int i = 0; i < size; i++) {
            if (!selectWords.get(i).equals(original.get(i))) {
                isSeedValid=false;
                setError(error, R.string.restore_error_checksum);
            }
        }

        return isSeedValid;
    }

    private void setError(TextView error, int restore_error_checksum) {
        error.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.toolbar_back:
                _mActivity.onBackPressed();
                break;
            case R.id.button_next:
                if (isCompleted){
                    if (verifyMnemonic()){
                        normalWindow = new NormalWindow(WalletApp.getContext(),R.layout.help_word_dialog,"", "",UIutils.getString(R.string.move_seed_tip),UIutils.getString(R.string.cancle),UIutils.getString(R.string.sure),this);
                        normalWindow.show(view);

                    }
                }
                break;
        }
    }

    @Override
    public void pressSure() {
        normalWindow.dismiss();
        MobclickAgent.onEvent(WalletApp.getContext(), UmengConstants.WALLET_CREATE_DELETE_MNEMONIC);
//        //存私钥
//        BigInteger privKey = KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(SpUtil.getInstance().getWalletPw())).getPrivKey();
//        SpUtil.getInstance().setMasterPriKey(SpUtil.getInstance().getWalletPw(),String.valueOf(privKey));
        WalletDao walletDao = new WalletDao();
        walletDao.update(WalletTable.WALLET_IS_COPY_SEED,"true", SpUtil.getInstance().getWalletID());
        SpUtil.getInstance().setIsSaveSeed(true);
        if (callback != null){
            callback.executeCallback(true);
        }
        popTo(MainDelegate.class, false);
    }

    @Override
    public void pressCancle() {
        normalWindow.dismiss();
    }

    @Override
    public void pressCopy(String privateKey) {

    }
}
