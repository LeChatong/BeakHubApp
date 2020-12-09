package com.lechatong.beakhub.Adapter;

/*
 Author : LeChatong
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder>{
    private Context context;

    private List<BhJob> bhJobs;

    public JobAdapter(List<BhJob> list_Bh_job) {
        this.bhJobs = list_Bh_job;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(context).inflate(R.layout.card_job, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        BhJob bhJob = this.bhJobs.get(position);
        holder.tv_desc.setText(bhJob.getDescription());
        holder.tv_job.setText(bhJob.getTitle());
        holder.tv_category.setText(bhJob.getCategory());
        holder.tv_nb_comment.setText(bhJob.getNumber_comment()+ " comments");
        holder.tv_nb_like.setText(bhJob.getNumber_like() + " likes");
    }

    public BhJob getBhJob(int position){
        return this.bhJobs.get(position);
    }

    @Override
    public int getItemCount() {
        return this.bhJobs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_job;
        TextView tv_category;
        TextView tv_desc;
        TextView tv_nb_comment;
        TextView tv_nb_like;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_job = itemView.findViewById(R.id.tv_job);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_nb_comment = itemView.findViewById(R.id.tv_nb_comment);
            tv_nb_like = itemView.findViewById(R.id.tv_nb_like);
        }
    }

}
