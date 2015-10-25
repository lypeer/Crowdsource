package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by lypeer on 10/15/2015.
 */
public class AcceptableBillAdapter extends BaseAdapter {

    /**
     * 调用adapter的界面的adapter
     */
    private Context context;

    /**
     * 装载bill的list
     */
    private List<Bill> billsList;

    public AcceptableBillAdapter(Context context , List<Bill> billsList){
        this.context = context;
        this.billsList = billsList;
    }


    @Override
    public int getCount() {
        return billsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_acceptable_bill , parent , false);
            viewHolder.sdvHeadPortrait = (SimpleDraweeView)convertView.
                    findViewById(R.id.acceptable_bill_sdv_head_portrait);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_name);
            viewHolder.tvSchool = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_school);
            viewHolder.tvStatus = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_status);
            viewHolder.tvDetail = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_detail);
            viewHolder.tvAward = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_award);
            viewHolder.tvLeftTimeHour = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_left_time_hour);
            viewHolder.tvLeftTimeMinutes = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_left_time_minutes);
            viewHolder.tvLeftTimeSecond = (TextView)convertView.findViewById(R.id.acceptable_bill_tv_left_time_second);
//            viewHolder.btnForDetail = (ButtonRectangle)convertView.findViewById(R.id.acceptable_bill_btn_for_detail);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final Bill bill = billsList.get(position);
        viewHolder.tvStatus.setText(bill.getStatus());
        viewHolder.tvDetail.setText(bill.getDetail());
        viewHolder.tvAward.setText(bill.getAward());
        //剩余时间
        ArrayList<String> timeList = new ArrayList<String>();
        timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
        viewHolder.tvLeftTimeHour.setText(timeList.get(0));
        viewHolder.tvLeftTimeMinutes.setText(timeList.get(1));
        viewHolder.tvLeftTimeSecond.setText(timeList.get(2));
        /*String timeLeft = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE).
                format(bill.getDeadline() - System.currentTimeMillis());
        viewHolder.tvLeftTimeHour.setText(timeLeft.split(":")[0]);
        viewHolder.tvLeftTimeMinutes.setText(timeLeft.split(":")[1]);
        viewHolder.tvLeftTimeSecond.setText(timeLeft.split(":")[2]);*/

        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.whereEqualTo("username", bill.getPublisherPhone());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null){
                    bill.setPublisherName((String) list.get(0).get("nickname"));
                    bill.setPublisherSchool((String)list.get(0).get("major"));
                    bill.setPublisherHeadPortrait(list.get(0).getAVFile("head_portrait").getThumbnailUrl(false , 96 , 96));
                    viewHolder.sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
                    viewHolder.tvName.setText(bill.getPublisherName());
                    viewHolder.tvSchool.setText(bill.getPublisherSchool());
                }else {
                    Log.e("AcceptableAdapterError", e.getMessage() + "===" + e.getCode());
                    Snackbar.make(viewHolder.tvAward, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{

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
//
//        /**
//         * 点击查看详情的button
//         */
//        private ButtonRectangle btnForDetail;

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
