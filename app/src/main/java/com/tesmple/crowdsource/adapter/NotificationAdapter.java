package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.Notification;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by lypeer on 10/23/2015.
 */
public class

        NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    /**
     * 建立实例的activity的context
     */
    private Context context;

    /**
     * 装载notification的list
     */
    private List<Notification> notificationsList;

    /**
     * 点击的监听器的对象
     */
    private OnItemClickListener onItemClickListener;

    /**
     * adpater的构造方法
     *
     * @param context           调用的activity的context
     * @param notificationsList 装通知的数据的list
     */
    public NotificationAdapter(Context context, List<Notification> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Notification notification = notificationsList.get(position);
        holder.tvTime.setText(TimeUtils.judgeTime(Long.valueOf(notification.getTime()),
                System.currentTimeMillis() - Long.valueOf(notification.getTime())));
        holder.tvContent.setText(notification.getContent());

        if (!notification.isRead()) {
            holder.ivHaveNotRead.setVisibility(View.VISIBLE);
            holder.ivHaveNotRead.bringToFront();
        } else {
            holder.ivHaveNotRead.setVisibility(View.GONE);
        }

        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.whereEqualTo("username", notification.getPublisher());
        avQuery.setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    holder.tvName.setText((String) list.get(0).get("nickname"));
                    holder.sdvHeadPortrait.setImageURI(Uri.parse(list.get(0).getAVFile("head_portrait").getThumbnailUrl(false, 96, 96)));
                    if (list.get(0).get("gender").equals(App.getContext().getString(R.string.man))) {
                        holder.ivGender.setBackground(App.getContext().getResources().getDrawable(R.drawable.icon_male));
                    } else {
                        holder.ivGender.setBackground(App.getContext().getResources().getDrawable(R.drawable.icon_male));
                    }
                } else {
                    Log.e("NotificationAdaptError", e.getMessage() + "===" + e.getCode());
                    //没有缓存数据
                    if (e.getCode() != 120) {
                        Snackbar.make(holder.tvContent, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        if (onItemClickListener != null) {
            holder.btnMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.btnMain, pos);
                }
            });

            holder.btnMain.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongCick(holder.btnMain, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 发单者头像的simpledraweeview
         */
        private SimpleDraweeView sdvHeadPortrait;

        /**
         * 表示还没有阅读过的小红点
         */
        private ImageView ivHaveNotRead;

        /**
         * 发单者名字的textview
         */
        private TextView tvName;

        /**
         * 表示性别的simpledraweeview
         */
        private ImageView ivGender;

        /**
         * 表示通知的时间的textview
         */
        private TextView tvTime;

        /**
         * 通知的主主体内容
         */
        private TextView tvContent;

        /**
         * 显示按钮的点击效果的按钮
         */
        private ButtonFlat btnMain;

        public MyViewHolder(View itemView) {
            super(itemView);

            sdvHeadPortrait = (SimpleDraweeView) itemView.
                    findViewById(R.id.notification_sdv_head_portrait);
            ivHaveNotRead = (ImageView) itemView.findViewById(R.id.notification_iv_have_not_read);
            ivGender = (ImageView) itemView.findViewById(R.id.notification_sdv_gender);
            tvName = (TextView) itemView.findViewById(R.id.notification_tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.notification_tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.notification_tv_content);
            btnMain = (ButtonFlat) itemView.findViewById(R.id.notification_btn_main);
        }
    }

    /**
     * 点击事件的接口
     */
    public interface OnItemClickListener {

        /**
         * 短点击
         *
         * @param v        被点击的对象
         * @param position 被点击的view的位置
         */
        void onItemClick(View v, int position);

        /**
         * 长按
         *
         * @param v        被点击的对象
         * @param position 被点击的view的位置
         */
        void onItemLongCick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 提示数据有了变动，刷新数据的方法
     *
     * @param notificationsList 变动之后的list
     */
    public void refresh(List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
        notifyDataSetChanged();
    }
}