package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.BillComment;
import com.tesmple.crowdsource.object.Comment;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.net.CookieHandler;
import java.util.List;

/**
 * Created by ESIR on 2015/10/18.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    /**
     * 实例的context
     */
    private Context context;

    /**
     * 评论列表list
     */
    private List<BillComment> commentList;


    /**
     * adpater的构造方法
     *
     * @param context     调用的activity的context
     * @param commentList 装载评论数据的list
     */
    public CommentAdapter(Context context, List<BillComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        final BillComment comment = commentList.get(position);
        avQuery.whereEqualTo("username", comment.getPublisher());
        // TODO: 10/27/2015
        /**
         * 缓存方式待商榷
         */
        avQuery.setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Bill bill = new Bill();
                    bill.setPublisherName((String) list.get(0).get("nickname"));
                    bill.setPublisherSchool((String) list.get(0).get("school"));
                    bill.setPublisherHeadPortrait(list.get(0).getAVFile("head_portrait").getThumbnailUrl(false, 96, 96));
                    holder.commentSdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
                    holder.commentTvName.setText(bill.getPublisherName());
                    holder.commentTvSchoolName.setText(list.get(0).get("major").toString());
                    holder.commentTvDetail.setText(comment.getContent());
                    holder.commentTvFavoritenum.setText(TimeUtils.judgeTime(comment.getCreatAt(),
                            System.currentTimeMillis() - comment.getCreatAt()));
                } else {
                    Log.e("AcceptableAdapterError", e.getMessage() + "===" + e.getCode());
                    //没有缓存数据
                    if (e.getCode() != 120) {
                        Snackbar.make(holder.itemView, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * 评论者头像的simpledraweeview
         */
        private SimpleDraweeView commentSdvHeadPortrait;

        /**
         * 评论者头像的TextView
         */
        private TextView commentTvName;

        /**
         * 评论者学校的TextView
         */
        private TextView commentTvSchoolName;

        /**
         * 评论详情的TextView
         */
        private TextView commentTvDetail;

        /**
         * comment点赞数的CheckBox
         */
        private CheckBox commentCbFavorite;

        /**
         * comment点赞数的TextView
         */
        private TextView commentTvFavoritenum;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentSdvHeadPortrait = (SimpleDraweeView) itemView.findViewById(R.id.comment_sdv_head_portrait);
            commentTvName = (TextView) itemView.findViewById(R.id.comment_tv_name);
            commentTvSchoolName = (TextView) itemView.findViewById(R.id.comment_tv_schoolname);
            commentTvDetail = (TextView) itemView.findViewById(R.id.comment_tv_detail);
            commentCbFavorite = (CheckBox) itemView.findViewById(R.id.comment_cb_favorite);
            commentTvFavoritenum = (TextView) itemView.findViewById(R.id.comment_tv_favoritenum);

        }
    }

    /**
     * 提示数据有了变动，刷新数据的方法
     *
     * @param billCommentList 变动之后的list
     */
    public void refresh(List<BillComment> billCommentList) {
        this.commentList = billCommentList;
        notifyDataSetChanged();
    }
}
