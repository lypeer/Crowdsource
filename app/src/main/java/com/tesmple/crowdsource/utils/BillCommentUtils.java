package com.tesmple.crowdsource.utils;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.BillComment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ESIR on 2015/10/20.
 */
public class BillCommentUtils  {


    /**
     * billcomment的存储list
     */
    private static List<BillComment> billCommentList = new ArrayList<>();

    public static List<BillComment> getBillCommentList(String targetFragment){
        List<BillComment> tempBillCommentList = new ArrayList<>();
        if(targetFragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
            tempBillCommentList = billCommentList;
        }
        return tempBillCommentList;
    }

    public static void startGetBillCommentTransaction(final String targetFragment,
                                                      final Handler handler,
                                                      String objectId){
        AVQuery<AVObject> avQuery = new AVQuery<>("Bill");
        avQuery.getInBackground("56206d5460b2fe711120dff7", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    if(targetFragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
                        JSONArray commentJsonArray = avObject.getJSONArray("comment");
                        Log.i("commentArraylength", String.valueOf(commentJsonArray.length()));
                        for(int i = 0;i < commentJsonArray.length();i++){
                            //billCommentList.add(commentJsonArray.get(i));
                            BillComment billComment = new BillComment();
                            try {
                                billComment.setContent(commentJsonArray.getString(i));
                                Log.i("commentjsonArray",commentJsonArray.getString(i));
                                billComment.setPublisher(commentJsonArray.getString(i));
                                billCommentList.add(billComment);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    Message message = new Message();
                    message.what = StringUtils.START_GET_BILL_COMMENT_SUCCESSFULLY;
                    handler.sendMessage(message);
                    Log.i("fuck", "utils有没有反应");
                }
                else {
                    Log.e("getBillCommentError", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.START_GET_BILL_COMMENT_FAILED;
                    handler.sendMessage(message);
                }
        }
        });

    }

    /**
     * 清除list的方法
     *
     * @param targetFragment 指定要清除list的fragment
     */
    public static void clearList(String targetFragment){
        if(targetFragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
            billCommentList.clear();
        }
    }
}
