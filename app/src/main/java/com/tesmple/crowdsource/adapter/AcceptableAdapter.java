package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.Bill;

import java.text.SimpleDateFormat;
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
     * @param context 调用的activity的context
     * @param billsList 装载单的数据的list
     */
    public AcceptableAdapter(Context context , List<Bill> billsList){
        this.context = context;
        this.billsList = billsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_acceptable_bill , parent , false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Bill bill = billsList.get(position);
        holder.tvStatus.setText(bill.getStatus());
        holder.tvDetail.setText(bill.getDetail());
        holder.tvAward.setText(bill.getAward());
        //剩余时间
        String timeLeft = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE).
                format(bill.getDeadline() - System.currentTimeMillis());
        holder.tvLeftTimeHour.setText(timeLeft.split(":")[0]);
        holder.tvLeftTimeMinutes.setText(timeLeft.split(":")[1]);
        holder.tvLeftTimeSecond.setText(timeLeft.split(":")[2]);

        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null){
                    bill.setPublisherName((String) list.get(0).get("name"));
                    bill.setPublisherSchool((String) list.get(0).get("school"));
                    bill.setPublisherHeadPortrait(list.get(0).getAVFile("head_portrait").getUrl());
                    holder.sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
                    holder.tvName.setText(bill.getPublisherName());
                    holder.tvSchool.setText(bill.getPublisherSchool());
                }else {
                    Log.e("AcceptableAdapterError", e.getMessage() + "===" + e.getCode());
                    Snackbar.make(holder.tvAward, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return billsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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

        /**
         * 剩下的时间的秒数
         */
        private TextView tvLeftTimeSecond;

        public MyViewHolder(View itemView) {
            super(itemView);

            sdvHeadPortrait = (SimpleDraweeView) itemView.
                    findViewById(R.id.acceptable_bill_sdv_head_portrait);
            tvName = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_name);
            tvSchool = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_school);
            tvStatus = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_status);
            tvDetail = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_detail);
            tvAward = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_award);
            tvLeftTimeHour = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_hour);
            tvLeftTimeMinutes = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_minutes);
            tvLeftTimeSecond = (TextView) itemView.findViewById(R.id.acceptable_bill_tv_left_time_second);
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
     * @param billsList 变动之后的list
     */
    public void refresh(List<Bill> billsList){
        this.billsList = billsList;
        notifyDataSetChanged();
    }
}