package com.tu.place.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tu.place.R;
import com.tu.place.model.Comment;
import com.tu.place.utils.AppUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder> {

    private Context context;
    private List<Comment> arrComment;
    private ItemListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvTime, tvComment;
        public CircleImageView imgAvatar;

        public MyViewHolder(View view) {
            super(view);

            imgAvatar = (CircleImageView) view.findViewById(R.id.imAvatar);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvComment = (TextView) view.findViewById(R.id.tv_comment);
        }
    }


    public AdapterComment(Context context, List<Comment> arrComment, ItemListener listener) {
        this.arrComment = arrComment;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Comment comment = arrComment.get(position);
        holder.tvName.setText(comment.getUserName());
        holder.tvTime.setText(AppUtils.miliToDateString(comment.getTime()));
        holder.tvComment.setText(comment.getContent());
        Picasso.with(context).load(comment.getUrlAvatar()).placeholder(R.drawable.ic_map).into(holder.imgAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrComment.size();
    }

    public interface ItemListener{
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

}
