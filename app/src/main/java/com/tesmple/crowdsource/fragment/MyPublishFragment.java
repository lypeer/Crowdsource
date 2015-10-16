package com.tesmple.crowdsource.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.AcceptableAdapter;
import com.tesmple.crowdsource.adapter.MyPublishAdapter;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lypeer on 10/14/2015.
 */
public class MyPublishFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    /**
     * 根视图的对象
     */
    private View rootView = null;

    /**
     * 刷新单的refreshlayout
     */
    private SwipeRefreshLayout srlBill;

    /**
     * 显示单的recycleview的对象
     */
    private RecyclerView rvBill;

    /**
     * recycleview的adapter
     */
    private MyPublishAdapter adapter;

    /**
     * 装单的billlist
     */
    private static List<Bill> billList = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtils.START_GET_BILL_TRANSACTION_SUCCESSFULLY:
                    billList = BillUtils.getBillsList(StringUtils.FRAGMENT_MY_PUBLISH);
//                    adapter.refresh(billList);
                    adapter.refresh(billList);
                    srlBill.setRefreshing(false);
                    break;
                case StringUtils.START_GET_BILL_TRANSACTION_FAILED:
//                    Snackbar.make(lvBill, R.string.please_check_your_network , Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(rvBill, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    srlBill.setRefreshing(false);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_my_publish , container , false);
            initView();
        }
        return rootView;
    }

    /**
     * 初始化fragment中的各种view的方法
     */
    private void initView(){
        srlBill = (SwipeRefreshLayout)rootView.findViewById(R.id.my_publish_srl_bill);
        rvBill = (RecyclerView)rootView.findViewById(R.id.my_publish_rv_bill);
        adapter = new MyPublishAdapter(getActivity() , billList);

        rvBill.setAdapter(adapter);
        rvBill.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBill.setItemAnimator(new DefaultItemAnimator());

        srlBill.setOnRefreshListener(this);

        srlBill.setRefreshing(true);
        BillUtils.clearList(StringUtils.FRAGMENT_MY_PUBLISH);
        BillUtils.startGetBillTransaction(StringUtils.FRAGMENT_MY_PUBLISH, handler, false, 0);
    }

    @Override
    public void onRefresh() {
        srlBill.setRefreshing(true);
        BillUtils.clearList(StringUtils.FRAGMENT_MY_PUBLISH);
        BillUtils.startGetBillTransaction(StringUtils.FRAGMENT_MY_PUBLISH , handler , false , 0);
    }
}
