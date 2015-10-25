package com.tesmple.crowdsource.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.activity.RequestDetailOfApplicant;
import com.tesmple.crowdsource.activity.RequestDetailOfPublisher;
import com.tesmple.crowdsource.adapter.AcceptableAdapter;
import com.tesmple.crowdsource.adapter.AcceptableBillAdapter;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lypeer on 10/14/2015.
 */
public class AcceptableBillFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    /**
     * 根视图的对象
     */
    private View rootView = null;

    /**
     * 刷新单的refreshlayout
     */
    private static SwipeRefreshLayout srlBill;

//    /**
//     * 显示单的listview
//     */
//    private ListView lvBill;
//
//    /**
//     * listview的adapter
//     */
//    private AcceptableBillAdapter adapter;

    /**
     * 显示单的recycleview的对象
     */
    private static RecyclerView rvBill;

    /**
     * recycleview的adapter
     */
    private static AcceptableAdapter adapter;

    /**
     * 表示是否在刷新的布尔值，false表示没有刷新，true表示正在刷新
     */
    private static boolean isRefreshing = false;

    /**
     * 装单的billlist
     */
    private static List<Bill> billList = new ArrayList<>();

    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtils.START_GET_BILL_TRANSACTION_SUCCESSFULLY:
                    billList = BillUtils.getBillsList(StringUtils.FRAGMENT_ACCEPTABLE_BILL);
//                    adapter.refresh(billList);
                    adapter.refresh(billList);
                    srlBill.setRefreshing(false);
                    isRefreshing = false;
                    break;
                case StringUtils.START_GET_BILL_TRANSACTION_FAILED:
//                    Snackbar.make(lvBill, R.string.please_check_your_network , Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(rvBill, R.string.please_check_your_network , Snackbar.LENGTH_SHORT).show();
                    srlBill.setRefreshing(false);
                    isRefreshing = false;
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_acceptable_bill , container , false);
            initView();
        }
        return rootView;
    }

    /**
     * 初始化fragment中的各种view的方法
     */
    private void initView(){
        srlBill = (SwipeRefreshLayout)rootView.findViewById(R.id.acceptable_bill_srl_bill);
//        lvBill = (ListView)rootView.findViewById(R.id.acceptable_bill_lv_main);
//        adapter = new AcceptableBillAdapter(getActivity() , billList);
//
//        lvBill.setAdapter(adapter);
//        lvBill.setDivider(new ColorDrawable(App.getContext().getResources().getColor(R.color.empty)));
//        lvBill.setDividerHeight(16);
        rvBill = (RecyclerView)rootView.findViewById(R.id.acceptable_bill_rv_bill);
        adapter = new AcceptableAdapter(getActivity() , billList);
        adapter.setOnItemClickListener(new AcceptableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity() , RequestDetailOfApplicant.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill" , billList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongCick(View v, int position) {

            }
        });

        rvBill.setAdapter(adapter);
        rvBill.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBill.setItemAnimator(new DefaultItemAnimator());

        srlBill.setOnRefreshListener(this);

        rvBill.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        srlBill.setRefreshing(true);
        isRefreshing = true;
        BillUtils.clearList(StringUtils.FRAGMENT_ACCEPTABLE_BILL);
        BillUtils.startGetBillTransaction(StringUtils.FRAGMENT_ACCEPTABLE_BILL , handler , false , 0);
    }

    /**
     * 外部刷新内容
     */
    public static void notifyDateChanged(){
        BillUtils.clearList(StringUtils.FRAGMENT_ACCEPTABLE_BILL);
        BillUtils.startGetBillTransaction(StringUtils.FRAGMENT_ACCEPTABLE_BILL, handler, false, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        srlBill.setRefreshing(true);
        isRefreshing = true;
        BillUtils.clearList(StringUtils.FRAGMENT_ACCEPTABLE_BILL);
        BillUtils.startGetBillTransaction(StringUtils.FRAGMENT_ACCEPTABLE_BILL, handler, false, 0);
    }
}
