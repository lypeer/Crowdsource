package com.tesmple.crowdsource.fragment;

import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.activity.RequestDetailOfApplicant;
import com.tesmple.crowdsource.adapter.AcceptableAdapter;
import com.tesmple.crowdsource.adapter.CommentAdapter;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.BillComment;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.BillCommentUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/10/18.
 */
public class BillCommentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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

    /**
     * comment fragment的评论输入框
     */
    private AutoCompleteTextView commentAutoTvComment;

    /**
     * 发表评论的button
     */
    private ButtonRectangle commentBtrCommiteComment;

    /**
     * 传入fragment的bill对象
     */
    private Bill bill;


    private static List<BillComment> commentList = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtils.START_GET_BILL_COMMENT_SUCCESSFULLY:
                    //App.dismissDialog();
                    commentList = BillCommentUtils.getBillCommentList(StringUtils.FRAGMENT_BILL_COMMENT);
                    commentAdapter.refresh(commentList);
                    srlComment.setRefreshing(false);
                    break;
                case StringUtils.START_GET_BILL_COMMENT_FAILED:
                    Snackbar.make(rvComment, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    srlComment.setRefreshing(false);
                    break;
                case StringUtils.START_POST_BILL_COMMENT_SUCCESSFULLY:
                    BillCommentUtils.clearList(StringUtils.FRAGMENT_BILL_COMMENT);
                    BillCommentUtils.startGetBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT, handler, bill.getObjectId());
                    App.dismissDialog();
                    break;
                case StringUtils.START_POST_BILL_COMMENT_FAILED:
                    App.dismissDialog();
                    Snackbar.make(rvComment, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_comment , container , false);
            getBundle();
            initView();
            setButtons();
        }
        return rootView;
    }

    private void getBundle(){
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill)bundle.getSerializable("bill");
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

        commentAutoTvComment = (AutoCompleteTextView)rootView.findViewById(R.id.comment_autotv_comment);
        commentBtrCommiteComment = (ButtonRectangle)rootView.findViewById(R.id.comment_btr_commitecomment);


        srlComment.setOnRefreshListener(this);
        srlComment.setRefreshing(true);
        BillCommentUtils.clearList(StringUtils.FRAGMENT_BILL_COMMENT);
        BillCommentUtils.startGetBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT, handler, bill.getObjectId());
    }

    /**
     * 设置button监听事件
     */
    private void setButtons(){
        commentBtrCommiteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attempCommitComment();
                } catch (AVException e) {
                    Log.i("comment===e",e.getMessage()+"+"+e.getCode());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 尝试发表
     */
    private void attempCommitComment() throws AVException {
        String comment = commentAutoTvComment.getText().toString();
        BillComment billComment = new BillComment();
        billComment.setContent(commentAutoTvComment.getText().toString());
        billComment.setPublisher(User.getInstance().getUserName());
        billComment.setWhichBill(bill.getObjectId());
        billComment.setCreatAt(TimeUtils.getCurrentTimeMillis());
        if(!comment.equals("")){
            BillCommentUtils.startPostBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT,
                    handler,
                    billComment,
                    bill.getObjectId());
            App.showDialog(getActivity());
            commentAutoTvComment.setText("");
        }
    }

    @Override
    public void onRefresh() {
        srlComment.setRefreshing(true);
        BillCommentUtils.clearList(StringUtils.FRAGMENT_BILL_COMMENT);
        BillCommentUtils.startGetBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT, handler, bill.getObjectId());
    }
}
