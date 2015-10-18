package com.tesmple.crowdsource.fragment;

import android.os.Bundle;
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
import com.tesmple.crowdsource.adapter.CommentAdapter;
import com.tesmple.crowdsource.object.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/10/18.
 */
public class CommentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * fragment的根布局
     */
    private View rootView;

    /**
     * fragment的评论者刷新view
     */
    private SwipeRefreshLayout srlComment;

    /**
     * fragment的评论者显示RecyclerView
     */
    private RecyclerView rvComment;

    /**
     * fragment的评论者列表adapter
     */
    private CommentAdapter commentAdapter;

    private static List<Comment> commentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_comment , container , false);
            initView();
        }
        return rootView;
    }

    /**
     * 初始化根布局
     */
    private void initView(){
        srlComment = (SwipeRefreshLayout)rootView.findViewById(R.id.comment_srl_comment);
        rvComment = (RecyclerView)rootView.findViewById(R.id.comment_rv_comment);

        commentAdapter = new CommentAdapter(getActivity(),commentList);

        rvComment.setAdapter(commentAdapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvComment.setItemAnimator(new DefaultItemAnimator());

        srlComment.setOnRefreshListener(this);
        srlComment.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        srlComment.setRefreshing(true);
    }
}
