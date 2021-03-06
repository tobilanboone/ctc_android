package kualian.dc.deal.application.ui.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.base.BaseView;
import kualian.dc.deal.application.base.SourceDelegate;
import kualian.dc.deal.application.bean.ContactBean;
import kualian.dc.deal.application.bean.ResponseData;
import kualian.dc.deal.application.database.ContactDao;
import kualian.dc.deal.application.ui.adapter.ContactAdapter;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;
import kualian.dc.deal.application.widget.WaveSideBarView;

/**
 * Created by idmin on 2018/2/28.
 */

public class ContactSelectDelegate extends SourceDelegate {
    private BaseMultiItemQuickAdapter mAdapter;
    private TextView clear;
    private static ContactSelectDelegate instance;
    private List<ContactBean> mData = new ArrayList<>();
    private List<ContactBean> mNewData = new ArrayList<>();
    private RecyclerView mRecycler;
    private WaveSideBarView sideBarView;
    private AutoCompleteTextView input;
    private static final String TAG = "ContactDelegate";
    private ContactDao contactDao=new ContactDao();
    public static ContactSelectDelegate getInstance(boolean isAddAddress) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TAG, isAddAddress);
        if (instance == null) {
            instance = new ContactSelectDelegate();
        }
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_contact_select;
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        super.onBindView(savedInstanceState, rootView);
        clear = rootView.findViewById(R.id.clear);
        mRecycler = rootView.findViewById(R.id.recycler);
        sideBarView = rootView.findViewById(R.id.slide_bar);
        findToolBar(rootView);
        toolTitle.setText(getResources().getString(R.string.contact));
        toolNext.setVisibility(View.GONE);
        input = rootView.findViewById(R.id.edit_input_);
    }

    @Override
    protected void onEvent() {
        super.onEvent();
        mData = contactDao.queryAll();
        Collections.sort(mData);
        mNewData.clear();
        mNewData.addAll(mData);

        mAdapter = new ContactAdapter(mData, true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        // mRecycler.addItemDecoration(new DivItemDecoration(getContext(), false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycler);
        //设置空视图
        mAdapter.setEmptyView(R.layout.view_empty);
        TextView empty = mAdapter.getEmptyView().findViewById(R.id.empty_text);
        empty.setText(R.string.contact_no);
        ImageView emptyImage = mAdapter.getEmptyView().findViewById(R.id.empty_image);
        emptyImage.setImageResource(R.drawable.empty_contact);
        initListener();
        setOnClickViews(clear,toolBack);
    }

    private void initListener() {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String content = input.getText().toString();
                if (content.length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                    mAdapter.getData().clear();
                    mAdapter.addData(getNewList(content));
                } else {
                    clear.setVisibility(View.GONE);
                    mAdapter.getData().clear();
                    mAdapter.addData(mNewData);
                }

            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                IGlobalCallback callback = CallbackManager.getInstance().getCallback(CallbackType.ON_RESULT);
                if (callback!=null){
                    //返回地址
                    ContactBean contactBean = (ContactBean) (mAdapter.getData().get(position));
                    callback.executeCallback(contactBean.getContactAddr());
                    CommonUtil.hideSoftInput(_mActivity);
                    _mActivity.onBackPressed();
                }

            }
        });
        sideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int section = getPositionForSection(letter.charAt(0));
                mRecycler.scrollToPosition(section);
            }
        });
    }

    private List<ContactBean> getNewList(String content) {
        List<ContactBean> list = new ArrayList<>();
        for (ContactBean bean : mNewData) {
            if (bean.getNickName().contains(content) || bean.getContactAddr().contains(content)) {
                list.add(bean);
            }

        }
        return list;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.clear:
                input.setText("");
                break;
            case R.id.toolbar_back:
                CommonUtil.hideSoftInput(_mActivity);
               _mActivity.onBackPressed();
                break;

        }
       /* if (view.getId() == R.id.clear) {

        } else {
            getParentDelegate().start(new AddContact());

        }*/
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        int count = mData.size();
        for (int i = 0; i < count; i++) {
            char firstChar = mData.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void getServiceData(ResponseData response, String tag) {

    }


}
