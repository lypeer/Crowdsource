package com.tesmple.crowdsource.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.Comment;

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
    private List<Comment> commentList;

    /**
     * adpater的构造方法
     * @param context 调用的activity的context
     * @param commentList 装载评论数据的list
     */
    public CommentAdapter(Context context,List<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment , parent , false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.commentTvName.setText(comment.getCommentName());
        holder.commentTvSchoolName.setText(comment.getCommentSchool());
        holder.commentTvDetail.setText(comment.getCommentDetail());
    }

    @Override
    public int getItemCount() {
        return 0;
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

            commentSdvHeadPortrait = (SimpleDraweeView)itemView.findViewById(R.id.comment_sdv_head_portrait);
            commentTvName = (TextView)itemView.findViewById(R.id.comment_tv_name);
            commentTvSchoolName = (TextView)itemView.findViewById(R.id.comment_tv_schoolname);
            commentTvDetail = (TextView)itemView.findViewById(R.id.comment_tv_detail);
            commentCbFavorite = (CheckBox)itemView.findViewById(R.id.comment_cb_favorite);
            commentTvFavoritenum = (TextView)itemView.findViewById(R.id.comment_tv_favoritenum);
        }
    }
}
