package com.tesmple.crowdsource.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ApplicantAdapter;
import com.tesmple.crowdsource.object.Applicant;
import com.tesmple.crowdsource.object.Bill;

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

    private static List<Applicant> applicantList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_applicant , container , false);
            getBundle();
            initView();
        }
        return rootView;
    }

    /**
     * 获得bundle的目标bill
     */
    private void getBundle(){
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill)bundle.getSerializable("bill");
    }

    /**
     * 初始化view视图
     */
    private void initView(){
        srlApplicant = (SwipeRefreshLayout)rootView.findViewById(R.id.applicant_srl_applicant);
        rvApplicant = (RecyclerView)rootView.findViewById(R.id.applicant_rv_applicant);

        applicantAdapter = new ApplicantAdapter(getActivity(),applicantList);

        rvApplicant.setAdapter(applicantAdapter);
        rvApplicant.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvApplicant.setItemAnimator(new DefaultItemAnimator());

        srlApplicant.setOnRefreshListener(this);
        srlApplicant.setRefreshing(true);
        applicantList.clear();
        setApplicantList();
    }

    private void setApplicantList(){
        if(bill.getApplicant()==null){

        }else if(!bill.getApplicant().contains("=")){
            //final Applicant applicant = new Applicant();
            AVQuery<AVObject> avQuery = new AVQuery<>("_User");
            avQuery.whereEqualTo("username", bill.getApplicant());
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        applicantList.clear();
                        Applicant applicant = new Applicant();
                        applicant.setApplicantName(list.get(0).get("name").toString());
                        applicant.setApplicantSchool(list.get(0).get("department").toString());
                        applicantList.add(applicant);
                        Log.i("applicantlist", String.valueOf(applicantList.size()));
                        srlApplicant.setRefreshing(false);
                        applicantAdapter.refresh(applicantList);
                    }
                    else {
                        Log.i("e1",e.getMessage());
                    }
                }
            });
        }else {
            String[] tempList = bill.getApplicant().split("=");
            //applicantList.clear();
            for(int i = 0 ;i < tempList.length ; i ++){
                AVQuery<AVObject> avQuery = new AVQuery<>("_User");
                avQuery.whereEqualTo("username", tempList[i]);
                avQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            Applicant applicant = new Applicant();
                            applicant.setApplicantName(list.get(0).get("name").toString());
                            applicant.setApplicantSchool(list.get(0).get("department").toString());
                            applicantList.add(applicant);
                            srlApplicant.setRefreshing(false);
                            applicantAdapter.refresh(applicantList);
                            Log.i("applicantlist", String.valueOf(applicantList.size()));
                        }else {
                            Log.i("e2",e.getMessage());
                        }
                    }
                });
            }
        }
    }

    /**
     * 刷新报名者列表
     */
    @Override
    public void onRefresh() {
        srlApplicant.setRefreshing(true);
        setApplicantList();
    }

}
