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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.CommentAdapter;
import com.tesmple.crowdsource.object.BillComment;
import com.tesmple.crowdsource.utils.BillCommentUtils;
import com.tesmple.crowdsource.utils.StringUtils;
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

    private AVObject bill;

    private static List<BillComment> commentList = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtils.START_GET_BILL_COMMENT_SUCCESSFULLY:
                    Log.i("fuck", "fragment有没有反应");
                    commentList = BillCommentUtils.getBillCommentList(StringUtils.FRAGMENT_BILL_COMMENT);
                    commentAdapter.refresh(commentList);
                    srlComment.setRefreshing(false);
                    break;
                case StringUtils.START_GET_BILL_COMMENT_FAILED:
                    Snackbar.make(rvComment, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    srlComment.setRefreshing(false);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_comment , container , false);
            initView();
            setButtons();
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

        commentAutoTvComment = (AutoCompleteTextView)rootView.findViewById(R.id.comment_autotv_comment);
        commentBtrCommiteComment = (ButtonRectangle)rootView.findViewById(R.id.comment_btr_commitecomment);

        srlComment.setOnRefreshListener(this);
        srlComment.setRefreshing(true);
        BillCommentUtils.clearList(StringUtils.FRAGMENT_BILL_COMMENT);
        BillCommentUtils.startGetBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT, handler, "56206d5460b2fe711120dff7");
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
        if(!comment.equals("")){
            String tableName = "Bill";
            bill = new AVObject(tableName);
            AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
            //bill = query.get("56206d5460b2fe711120dff7");
            query.getInBackground("56206d5460b2fe711120dff7", new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    bill = avObject;
                    JSONObject myObject = new JSONObject();
                    /*JSONArray jsonArray = bill.getJSONArray("comment");*/
                    JSONArray jsonArray = new JSONArray();
                    //Log.i("jsonArray", String.valueOf(jsonArray.length()));
                    myObject.put("publisher", "余烜");
                    myObject.put("comment", "good");
                    //jsonArray.put("余烜");
                    jsonArray.put(myObject);
                    jsonArray.optJSONObject(0);
                    jsonArray.put(myObject);
                    bill.put("comment", jsonArray);
                    bill.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.i("LeanCloud", "Save successfully.");
                            } else {
                                Log.i("1comment===e", e.getMessage() + "+" + e.getCode());
                                Log.e("LeanCloud", "Save failed.");
                            }
                        }
                    });
                }
            });
            //bill = AVObject.createWithoutData("Bill", "56206d5460b2fe711120dff7");
        }
    }

    @Override
    public void onRefresh() {
        srlComment.setRefreshing(true);
        BillCommentUtils.clearList(StringUtils.FRAGMENT_BILL_COMMENT);
        BillCommentUtils.startGetBillCommentTransaction(StringUtils.FRAGMENT_BILL_COMMENT, handler, "56206d5460b2fe711120dff7");
    }
}
