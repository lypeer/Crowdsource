package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.AcceptedBillAdapter;
import com.tesmple.crowdsource.adapter.NotificationAdapter;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Notification;
import com.tesmple.crowdsource.object.NotificationLab;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.PushUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lypeer on 10/17/2015.
 */
public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 刷新单的refreshlayout
     */
    private SwipeRefreshLayout srlBill;

    /**
     * 显示单的recycleview的对象
     */
    private RecyclerView rvBill;

    /**
     * recycleview的adapter
     */
    private NotificationAdapter adapter;

    /**
     * 表示是否在刷新的布尔值，false表示没有刷新，true表示正在刷新
     */
    private static boolean isRefreshing = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtils.GET_NOTIFICATION_SUCCESSFULLY:
                    adapter.refresh(NotificationLab.getInstance().getNotificationList());
                    srlBill.setRefreshing(false);
                    isRefreshing = false;
                    break;
                case StringUtils.GET_NOTIFICATION_FAILED:
                    Snackbar.make(rvBill, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                    srlBill.setRefreshing(false);
                    isRefreshing = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initToolBar();

        init();
    }

    /**
     * 初始化toolbar的方法
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.prompt_notification);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化的方法
     */
    private void init() {
        srlBill = (SwipeRefreshLayout) findViewById(R.id.notification_srl_notification);
        rvBill = (RecyclerView) findViewById(R.id.notification_rv_notification);
        adapter = new NotificationAdapter(this, NotificationLab.getInstance().getNotificationList());
        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                if (!NotificationLab.getInstance().getNotificationList().get(position).isRead()) {
                    NotificationLab.getInstance().getNotificationList().get(position).setIsRead(true);
                    NotificationLab.getInstance().reverseList();
                    final JSONArray jsonArray = new JSONArray();
                    for (Notification notification : NotificationLab.getInstance().getNotificationList()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("is_read", notification.isRead());
                            jsonObject.put("alert", notification.getContent());
                            jsonObject.put("time", notification.getTime());
                            jsonObject.put("sender", notification.getPublisher());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    AVQuery<AVObject> avQuery = new AVQuery<>("UserHelper");
                    avQuery.whereEqualTo("username", User.getInstance().getUserName());
                    avQuery.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e == null) {
                                list.get(0).put("notification", jsonArray);
                                list.get(0).saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            jump(NotificationLab.getInstance().getNotificationList().get(position).getType());
                                        } else {
                                            Log.e("NotificActSaveError", e.getMessage() + "===" + e.getCode());
                                            Snackbar.make(rvBill, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.e("NotificActSaveError", e.getMessage() + "===" + e.getCode());
                                Snackbar.make(rvBill, R.string.please_check_your_network, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    jump(NotificationLab.getInstance().getNotificationList().get(position).getType());
                }
            }

            @Override
            public void onItemLongCick(View v, int position) {

            }
        });

        rvBill.setAdapter(adapter);
        rvBill.setLayoutManager(new LinearLayoutManager(this));
        rvBill.setItemAnimator(new DefaultItemAnimator());

        srlBill.setOnRefreshListener(this);
        srlBill.setRefreshing(true);
        isRefreshing = true;
        NotificationLab.getInstance().clearList();
        getNotifications();

        rvBill.setOnTouchListener(new View.OnTouchListener() {
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

    /**
     * 跳转到别的地方去的方法
     *
     * @param type 这个通知的类型
     */
    private void jump(String type) {
        if (type.equals(StringUtils.PUSH_BECOME_APPLICANT) ||
                type.equals(StringUtils.PUSH_HAVE_ROBBED) ||
                type.equals(StringUtils.PUSH_CONFIRMER_REMOVE_BILL) ||
                type.equals(StringUtils.PUSH_REMIND_PUBLISHER) ||
                type.equals(StringUtils.PUSH_SYSTEM_FINISH)) {
            finish();
            MainActivity.changeViewpagerItem(1);
        } else if (type.equals(StringUtils.PUSH_BECOME_COMFIRMER) ||
                type.equals(StringUtils.PUSH_NOT_BECOME_COMFIRMER) ||
                type.equals(StringUtils.PUSH_PUBLISHER_REMOVE_BILL) ||
                type.equals(StringUtils.PUSH_FINISH_BILL)) {
            finish();
            MainActivity.changeViewpagerItem(2);
        }
    }

    @Override
    public void onRefresh() {
        srlBill.setRefreshing(true);
        isRefreshing = true;
        NotificationLab.getInstance().clearList();
        getNotifications();
    }

    /**
     * 获得通知的方法
     */
    private void getNotifications() {
        AVQuery<AVObject> avQuery1 = new AVQuery<>("UserHelper");
        avQuery1.whereEqualTo("username", User.getInstance().getUserName());
        avQuery1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    JSONArray jsonArray = list.get(0).getJSONArray("notification");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Notification notification = new Notification();
                            try {
                                JSONObject tempJsonObject = new JSONObject(jsonArray.get(i).toString());
                                notification.setTime((String) tempJsonObject.get("time"));
                                notification.setContent(tempJsonObject.getString("alert"));
                                notification.setIsRead(tempJsonObject.getBoolean("is_read"));
                                notification.setPublisher(tempJsonObject.getString("sender"));
                                notification.setType(PushUtils.getPushType(tempJsonObject.getString("alert")));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            NotificationLab.getInstance().addNotification(notification);

                        }
                    }
                    NotificationLab.getInstance().reverseList();
                    Message message = new Message();
                    message.what = StringUtils.GET_NOTIFICATION_SUCCESSFULLY;
                    mHandler.sendMessage(message);
                } else {
                    Log.e("BillUtilsUserHelper", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.GET_NOTIFICATION_FAILED;
                    mHandler.sendMessage(message);
                }
            }
        });
    }
}

