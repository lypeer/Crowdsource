package com.tesmple.crowdsource.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.CheckBox;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.object.Applicant;
import com.tesmple.crowdsource.object.BillComment;

import java.util.List;

/**
 * Created by ESIR on 2015/10/18.
 */
public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.MyViewHolder> {

    /**
     * 实例的context
     */
    private Context context;

    /**
     * 报名者的list
     */
    private List<Applicant> applicantList;

    /**
     * adpater的构造方法
     * @param context 调用的activity的context
     * @param applicantList 装载报名者数据的list
     */
    public ApplicantAdapter(Context context , List<Applicant> applicantList){
        this.context = context;
        this.applicantList = applicantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_applicant , parent , false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Applicant applicant = applicantList.get(position);
        holder.applicantTvName.setText(applicant.getApplicantName());
        holder.applicantTvSchoolName.setText(applicant.getApplicantSchool());
        holder.applicantCbIschecked.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return applicantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * 报名者头像的simpledraweeview
         */
        private SimpleDraweeView applicantSdvHeadPortrait;

        /**
         * 报名者头像的TextView
         */
        private TextView applicantTvName;

        /**
         * 报名者学校的TextView
         */
        private TextView applicantTvSchoolName;

        /**
         * 报名者是否被选中的CheckBox
         */
        private android.widget.CheckBox applicantCbIschecked;

        public MyViewHolder(View itemView) {
            super(itemView);

            applicantSdvHeadPortrait = (SimpleDraweeView)itemView.findViewById(R.id.applicant_sdv_head_portrait);
            applicantTvName = (TextView)itemView.findViewById(R.id.applicant_tv_name);
            applicantTvSchoolName = (TextView) itemView.findViewById(R.id.applicant_tv_schoolname);
            applicantCbIschecked = (android.widget.CheckBox)itemView.findViewById(R.id.applicant_cb_ischecked);
        }
    }

    /**
     * 提示数据有了变动，刷新数据的方法
     * @param applicantList 变动之后的list
     */
    public void refresh(List<Applicant> applicantList){
        this.applicantList = applicantList;
        notifyDataSetChanged();
    }
}
