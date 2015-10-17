package com.tesmple.crowdsource.fragment;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tesmple.crowdsource.R;

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

    private RecyclerView rvApplicant;

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
     * 初始化view视图
     */
    private void initView(){
        srlApplicant = (SwipeRefreshLayout)rootView.findViewById(R.id.applicant_srl_applicant);
        rvApplicant = (RecyclerView)rootView.findViewById(R.id.applicant_rv_applicant);
    }

    @Override
    public void onRefresh() {

    }
}
