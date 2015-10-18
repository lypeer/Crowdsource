package com.tesmple.crowdsource.fragment;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.adapter.ApplicantAdapter;
import com.tesmple.crowdsource.object.Applicant;

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

    private static List<Applicant> applicantList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_applicant , container , false);
            initView();
        }
        return rootView;
    }

    /**
     * 初始化view视图
     */
    private void initView(){
        srlApplicant = (SwipeRefreshLayout)rootView.findViewById(R.id.applicant_srl_applicant);
        rvApplicant = (RecyclerView)rootView.findViewById(R.id.applicant_rv_applicant);

        Applicant applicant1 = new Applicant();
        applicant1.setApplicantName("余烜");
        applicant1.setApplicantSchool("电子科技大学");
        applicant1.setApplicantIsChecked(false);
        applicant1.setApplicantCreditValue(5);

        Applicant applicant2 = new Applicant();
        applicant2.setApplicantName("李宇航");
        applicant2.setApplicantSchool("电子科技大学");
        applicant2.setApplicantIsChecked(true);
        applicant2.setApplicantCreditValue(5);

        Applicant applicant3 = new Applicant();
        applicant3.setApplicantName("罗阳");
        applicant3.setApplicantSchool("电子科技大学");
        applicant3.setApplicantIsChecked(false);
        applicant3.setApplicantCreditValue(5);

        Applicant applicant4 = new Applicant();
        applicant4.setApplicantName("焦钰博");
        applicant4.setApplicantSchool("电子科技大学");
        applicant4.setApplicantIsChecked(false);
        applicant4.setApplicantCreditValue(5);

        Applicant applicant5 = new Applicant();
        applicant5.setApplicantName("王梓瑞");
        applicant5.setApplicantSchool("电子科技大学");
        applicant5.setApplicantIsChecked(false);
        applicant5.setApplicantCreditValue(5);

        applicantList.add(applicant1);
        applicantList.add(applicant2);
        applicantList.add(applicant3);
        applicantList.add(applicant4);
        applicantList.add(applicant5);


        applicantAdapter = new ApplicantAdapter(getActivity(),applicantList);

        rvApplicant.setAdapter(applicantAdapter);
        rvApplicant.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvApplicant.setItemAnimator(new DefaultItemAnimator());

        srlApplicant.setOnRefreshListener(this);
        srlApplicant.setRefreshing(true);
    }

    /**
     * 刷新报名者列表
     */
    @Override
    public void onRefresh() {
        srlApplicant.setRefreshing(true);
    }
}
