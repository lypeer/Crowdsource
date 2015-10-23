package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.Bill;

import java.util.List;

/**
 * Created by lypeer on 10/23/2015.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

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
    public NotificationAdapter(Context context , List<Bill> billsList){
        this.context = context;
        this.billsList = billsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_publish , parent , false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Bill bill = billsList.get(position);





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
         * 表示性别的simpledraweeview
         */
        private SimpleDraweeView sdvGender;

        /**
         * 表示通知的时间的textview
         */
        private TextView tvTime;

        /**
         * 通知的主主体内容
         */
        private TextView tvContent;

        public MyViewHolder(View itemView) {
            super(itemView);

            sdvHeadPortrait = (SimpleDraweeView) itemView.
                    findViewById(R.id.notification_sdv_head_portrait);
            sdvGender = (SimpleDraweeView) itemView.findViewById(R.id.notification_sdv_gender);
            tvName = (TextView) itemView.findViewById(R.id.notification_tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.notification_tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.notification_tv_content);
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