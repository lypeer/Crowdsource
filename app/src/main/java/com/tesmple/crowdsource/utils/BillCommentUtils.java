package com.tesmple.crowdsource.utils;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.alibaba.fastjson.JSON;
/*import com.alibaba.fastjson.JSONObject;*/
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.BillComment;
import com.tesmple.crowdsource.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * 评论发送的目标bill
     */
    private static AVObject targetBill;

    /**
     * 获取billcomment的list
     * @param targetFragment 传入目标fragment
     * @return 返回一个装着BillComment的list
     */
    public static List<BillComment> getBillCommentList(String targetFragment){
        List<BillComment> tempBillCommentList = new ArrayList<>();
        if(targetFragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
            tempBillCommentList = billCommentList;
        }
        return tempBillCommentList;
    }

    /**
     * 尝试发送一条新的评论
     * @param targetfragment 传入目标fragment
     * @param handler   传入静态handler从而传回发送结果
     * @param billComment 传入需要发送的评论billcomment
     */
    public static void startPostBillCommentTransaction(final String targetfragment,
                                                       final Handler handler,
                                                       final BillComment billComment,
                                                       final String billObjectId){
        if(targetfragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
            String tableName = "Bill";
            targetBill = new AVObject(tableName);
            final AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
            query.getInBackground(billObjectId, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    targetBill = avObject;
                    com.alibaba.fastjson.JSONObject myObject = new com.alibaba.fastjson.JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = targetBill.getJSONArray("comment");
                    myObject.put("publisher", User.getInstance().getUserName());
                    myObject.put("content", billComment.getContent());
                    myObject.put("creatAt",billComment.getCreatAt());
                    myObject.put("whichBill",billComment.getWhichBill());
                    try {
                        if(jsonArray != null){
                            jsonArray.put(jsonArray.length(),myObject);
                        }
                        else{
                            jsonArray = new JSONArray();
                            jsonArray.put(myObject);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    targetBill.put("comment", jsonArray);
                    targetBill.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Message message = new Message();
                                message.what = StringUtils.START_POST_BILL_COMMENT_SUCCESSFULLY;
                                handler.sendMessage(message);
                                Log.i("LeanCloud", "Post successfully.");
                            } else {
                                Message message = new Message();
                                message.what = StringUtils.START_POST_BILL_COMMENT_FAILED;
                                handler.sendMessage(message);
                                Log.i("1comment===e", e.getMessage() + "+" + e.getCode());
                                Log.e("LeanCloud", "Post failed");
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 尝试从云端获取指定bill的评论列表
     * @param targetFragment 传入目标fragment
     * @param handler 传入静态的handler
     * @param billObjectId 传入目标bill的objectId
     */
    public static void startGetBillCommentTransaction(final String targetFragment,
                                                      final Handler handler,
                                                      String billObjectId){
        AVQuery<AVObject> avQuery = new AVQuery<>("Bill");
        avQuery.getInBackground(billObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    if(targetFragment.equals(StringUtils.FRAGMENT_BILL_COMMENT)){
                        JSONArray commentJsonArray = avObject.getJSONArray("comment");
                        //Log.i("commentArraylength", String.valueOf(commentJsonArray.length()));
                        if(commentJsonArray != null){
                            for(int i = 0;i < commentJsonArray.length();i++){
                                BillComment billComment = new BillComment();
                                try {
                                    JSONObject tempJsonObeject = new JSONObject(commentJsonArray.getString(i).toString());
                                    billComment.setContent(tempJsonObeject.getString("content"));
                                    billComment.setPublisher(tempJsonObeject.getString("publisher"));
                                    billComment.setCreatAt(Long.valueOf(tempJsonObeject.get("creatAt").toString()));
                                    billCommentList.add(billComment);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        Message message = new Message();
                        message.what = StringUtils.START_GET_BILL_COMMENT_SUCCESSFULLY;
                        handler.sendMessage(message);
                    }
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
