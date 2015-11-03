package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.Card;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lypeer on 10/16/2015.
 */
public class AcceptableAdapter extends RecyclerView.Adapter<AcceptableAdapter.MyViewHolder> {

    /**
     * 建立实例的activity的context
     */
    private Context context;

    /**
     * 装载bill的list
     */
    private List<Bill> billsList;

    /**
     * 点击的监听器的对象
     */
    private OnItemClickListener onItemClickListener;

    /**
     * adpater的构造方法
     *
     * @param context   调用的activity的context
     * @param billsList 装载单的数据的list
     */
    public AcceptableAdapter(Context context, List<Bill> billsList) {
        this.context = context;
        this.billsList = billsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_acceptable_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (billsList.size() != 0) {
            final Bill bill = billsList.get(position);
            holder.tvStatus.setText(bill.getStatus());
            holder.tvDetail.setText(bill.getDetail());
            holder.tvAward.setText(bill.getAward());
            //剩余时间
            ArrayList<String> timeList = new ArrayList<String>();
            timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
            holder.tvLeftTimeHour.setText(timeList.get(0));
            holder.tvLeftTimeMinutes.setText(timeList.get(1));
//        holder.tvLeftTimeSecond.setText(timeList.get(2));

            AVQuery<AVObject> avQuery = new AVQuery<>("_User");
            avQuery.setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
            avQuery.whereEqualTo("username", bill.getPublisherPhone());
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        bill.setPublisherName((String) list.get(0).get("nickname"));
                        bill.setPublisherSchool((String) list.get(0).get("major"));
                        bill.setPublisherHeadPortrait(list.get(0).getAVFile("head_portrait").getThumbnailUrl(false, 96, 96));
                        holder.sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
                        holder.tvName.setText(bill.getPublisherName());
                        holder.tvSchool.setText(bill.getPublisherSchool());
                    } else {
                        Log.e("AcceptableAdapterError", e.getMessage() + "===" + e.getCode());
                        //没有缓存数据
                        if (e.getCode() != 120) {
                            Snackbar.make(holder.tvAward, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }
                }
            });

            if (onItemClickListener != null) {
                holder.cvBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.cvBill, pos);
                    }
                });

                holder.cvBill.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongCick(holder.cvBill, pos);
                        return false;
                    }
                });
            }
        } else {
            holder.sdvHeadPortrait.setBackground(App.getContext().getResources().getDrawable(R.drawable.ic_systemgg));
            holder.tvName.setText(R.string.system_name);
            holder.tvSchool.setText(R.string.system_major);
            holder.tvSchool.setText(R.string.system_status);
            holder.tvDetail.setText(R.string.system_detail);
            holder.tvAward.setText(R.string.system_award);
            holder.tvLeftTimeHour.setText(R.string.system_hour);
            holder.tvLeftTimeMinutes.setText(R.string.system_hour);
        }
    }

    @Override
    public int getItemCount() {
        if (billsList.size() == 0) {
            return 1;
        } else {
            return billsList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        /**
         * 包裹的cardview
         */
        private CardView cvBill;

        /**
         * 发单者头像的simpledraweeview
         */
        private SimpleDraweeView sdvHeadPortrait;

        /**
         * 发单者名字的textview
         */
        private TextView tvName;

        /**
         * 发单者学校的textview
         */
        private TextView tvSchool;

        /**
         * 订单的状态的textview
         */
        private TextView tvStatus;

        /**
         * 订单详情的textview
         */
        private TextView tvDetail;

        /**
         * 用户名字的textview
         */
        private TextView tvAward;

        /**
         * 剩下的时间的小时数
         */
        private TextView tvLeftTimeHour;

        /**
         * 剩下的时间的分钟数
         */
        private TextView tvLeftTimeMinutes;

//        /**
//         * 剩下的时间的秒数
//         */
//        private TextView tvLeftTimeSecond;

        public MyViewHolder(View itemView) {
            super(itemView);

            cvBill = (CardView) itemView.findViewById(R.id.acceptable_bill_cv_bill);
            sdvHeadPortrait = (SimpleDraweeView) itemView.
                    findViewById(R.id.acceptable_bill_sdv_head_portrait);
            tvName = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_name);
            tvSchool = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_school);
            tvStatus = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_status);
            tvDetail = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_detail);
            tvAward = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_award);
            tvLeftTimeHour = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_hour);
            tvLeftTimeMinutes = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_minutes);
//            tvLeftTimeSecond = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_second);
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
     * @param billsList 变动之后的list
     */
    public void refresh(List<Bill> billsList) {
        this.billsList = billsList;
        notifyDataSetChanged();
    }
}
