package com.tesmple.crowdsource.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.activity.ConfirmSuccessfullyActivity;
import com.tesmple.crowdsource.adapter.ApplicantAdapter;
import com.tesmple.crowdsource.object.Applicant;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.PushUtils;
import com.tesmple.crowdsource.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/10/18.
 */
public class ApplicantFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 根视图的对象
     */
    private View rootView = null;

    /**
     * applicant fragment的报名者刷新view
     */
    private SwipeRefreshLayout srlApplicant;

    /**
     * applicant fragment的报名者显示RecyclerView
     */
    private RecyclerView rvApplicant;

    /**
     * applicant fragment的RecyclerView的Adapter
     */
    private ApplicantAdapter applicantAdapter;

    /**
     * 绑定的bill
     */
    private Bill bill;

    /**
     * 表示是否在刷新的布尔值，false表示没有刷新，true表示正在刷新
     */
    private static boolean isRefreshing = false;

    private static List<Applicant> applicantList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY:
                    PushUtils.startPushTransaction(mHandler, StringUtils.PUSH_NOT_BECOME_COMFIRMER, bill);
                    PushUtils.startPushTransaction(mHandler, StringUtils.PUSH_BECOME_COMFIRMER, bill);
                    Intent intent = new Intent(getActivity(), ConfirmSuccessfullyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.billDeadLine), bill.getDeadline().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case StringUtils.CHANGE_BILL_STATUS_FAILED:
                    Snackbar.make(rvApplicant, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_applicant, container, false);
            getBundle();
            initView();
        }
        return rootView;
    }

    /**
     * 获得bundle的目标bill
     */
    private void getBundle() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill) bundle.getSerializable("bill");
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        srlApplicant = (SwipeRefreshLayout) rootView.findViewById(R.id.applicant_srl_applicant);
        rvApplicant = (RecyclerView) rootView.findViewById(R.id.applicant_rv_applicant);

        applicantAdapter = new ApplicantAdapter(getActivity(), applicantList,bill);

        rvApplicant.setAdapter(applicantAdapter);
        rvApplicant.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvApplicant.setItemAnimator(new DefaultItemAnimator());

        srlApplicant.setOnRefreshListener(this);
        srlApplicant.setRefreshing(true);
        isRefreshing = true;
        applicantList.clear();
        setApplicantList();

        if(bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
            applicantAdapter.setOnItemClickListener(new ApplicantAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    showSureDialog(applicantList.get(position));
                }

                @Override
                public void onItemLongCick(View v, int position) {

                }
            });
        }else if (bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)){
            applicantAdapter.setOnItemClickListener(null);
        }

        rvApplicant.setOnTouchListener(new View.OnTouchListener() {
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

    private void setApplicantList() {
        if (bill.getApplicant() == null) {

        } else if (!bill.getApplicant().contains("=")) {
            //final Applicant applicant = new Applicant();
            AVQuery<AVObject> avQuery = new AVQuery<>("_User");
            avQuery.whereEqualTo("username", bill.getApplicant());
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (list.size() != 0) {
                            Applicant applicant = new Applicant();
                            applicant.setUsername(list.get(0).get("username").toString());
                            applicant.setApplicantName(list.get(0).get("nickname").toString());
                            applicant.setApplicantSchool(list.get(0).get("department").toString());
                            applicant.setApplicantHeadPortrait(list.get(0).getAVFile("head_portrait").getUrl());
                            applicant.setApplicantIsChecked(true);
                            applicantList.add(applicant);
                            Log.i("applicantlist", String.valueOf(applicantList.size()));
                            srlApplicant.setRefreshing(false);
                            isRefreshing = false;
                            applicantAdapter.refresh(applicantList);
                        }
                    } else {
                        Log.i("e1", e.getMessage());
                    }
                }
            });
        } else {
            final String[] tempList = bill.getApplicant().split("=");
            for (int i = 0; i < tempList.length; i++) {
                AVQuery<AVObject> avQuery = new AVQuery<>("_User");
                avQuery.whereEqualTo("username", tempList[i]);
                avQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            Applicant applicant = new Applicant();
                            applicant.setUsername(list.get(0).get("username").toString());
                            applicant.setApplicantName(list.get(0).get("nickname").toString());
                            applicant.setApplicantSchool(list.get(0).get("department").toString());
                            applicant.setApplicantHeadPortrait(list.get(0).getAVFile("head_portrait").getUrl());
                            Log.i("checklinfo", bill.getStatus().equals(StringUtils.BILL_STATUS_TWO) + " " + applicant.getUsername() + " " + bill.getConfirmer());
                            if(bill.getStatus().equals(StringUtils.BILL_STATUS_TWO) && applicant.getUsername().equals(bill.getConfirmer())){
                                applicant.setApplicantIsChecked(true);
                            }else {
                                applicant.setApplicantIsChecked(false);
                            }
                            applicantList.add(applicant);
                            srlApplicant.setRefreshing(false);
                            isRefreshing = false;
                            applicantAdapter.refresh(applicantList);
                            Log.i("applicantlist", String.valueOf(applicantList.size()));
                        } else {
                            Log.i("e2", e.getMessage());
                        }
                    }
                });
            }
        }
    }

    /**
     * 显示让用户U确认的dialog的方法
     *
     * @param applicant 当前的报名者
     */
    private void showSureDialog(final Applicant applicant) {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.prompt_remind)
                .setMessage(App.getContext().getString(R.string.prompt_let_be_confirmer, applicant.getApplicantName()))
                .setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirm(applicant);
                    }
                })
                .setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * 确认报名者接单的方法
     *
     * @param applicant 被确认接单的报名者
     */
    private void confirm(Applicant applicant) {
        bill.setConfirmer(applicant.getUsername());
        bill.setStatus(StringUtils.BILL_STATUS_TWO);
        BillUtils.changeBillStatus(mHandler, bill, StringUtils.BILL_STATUS_TWO);
    }

    /**
     * 刷新报名者列表
     */
    @Override
    public void onRefresh() {
        srlApplicant.setRefreshing(true);
        isRefreshing = true;
        applicantList.clear();
        setApplicantList();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
