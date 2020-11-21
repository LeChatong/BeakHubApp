package com.lechatong.beakhub.Adapter;

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

public class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.ViewHolder> {
    private Context context;

    private List<BhJob> bhJobs;


    public JobSearchAdapter(List<BhJob> jobList) {
        this.bhJobs = jobList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleJob;
        TextView descJob;
        TextView categoryJob;
        TextView user;
        TextView nbComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleJob = (TextView) itemView.findViewById(R.id.tv_job_search);
            descJob = (TextView) itemView.findViewById(R.id.tv_desc_search);
            categoryJob = (TextView) itemView.findViewById(R.id.tv_category_search);
            user = (TextView) itemView.findViewById(R.id.tv_user);
            nbComment = (TextView) itemView.findViewById(R.id.tv_search_nb_comment);
        }

    }

    @NonNull
    @Override
    public JobSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(context).inflate(R.layout.card_job_search, parent, false);
        return new JobSearchAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobSearchAdapter.ViewHolder holder, int position) {
        BhJob bhJob = this.bhJobs.get(position);
        holder.descJob.setText(bhJob.getDescription());
        holder.titleJob.setText(bhJob.getTitle());
        holder.categoryJob.setText(bhJob.getCategory());
        holder.user.setText(bhJob.getUser());
        holder.nbComment.setText(bhJob.getNumber_comment() + " comments");
    }

    @Override
    public int getItemCount() {
        return this.bhJobs.size();
    }

    public BhJob getBhJob(int position){
        return this.bhJobs.get(position);
    }
}
