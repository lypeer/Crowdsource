package com.tesmple.crowdsource.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tesmple.crowdsource.R;

/**
 * Created by lypeer on 10/14/2015.
 */
public class AcceptableBillFragment extends Fragment {
    /**
     * 根视图的对象
     */
    private View rootView = null;


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

    }
}
