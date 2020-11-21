package com.lechatong.beakhub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.WebService.BeakHubService;

import static android.content.Context.MODE_PRIVATE;

public class JobFragment extends Fragment implements ServiceCallback<APIResponse> {

    private Intent intent;

    private Context context;

    private Long job_id, is_author;

    private static final String ID_JOB = "JOB_ID";

    private static final String IS_AUTHOR = "IS_AUTHOR";

    private static final String PREFS = "PREFS";

    private TextView tv_title, tv_description, tv_category, tv_user;

    public JobFragment() {
    }

    public static JobFragment newInstance() {
        return new JobFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProfileJobActivity activity = (ProfileJobActivity) getActivity();

        assert activity != null;
        job_id = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(ID_JOB,0);
        is_author = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(IS_AUTHOR,0);

        View view = inflater.inflate(R.layout.fragment_job, container, false);

        tv_title = (TextView) view.findViewById(R.id.tvTitleJob);
        tv_description = (TextView) view.findViewById(R.id.tvDescriptionJob);
        tv_category = (TextView) view.findViewById(R.id.tvCategoryJob);
        tv_user = (TextView) view.findViewById(R.id.tvUSerJob);

        BeakHubService.getJobById(this, job_id);

        FloatingActionButton fab = view.findViewById(R.id.btn_edit_job);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(is_author.intValue() == 1){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 200){
            BhJob bhJob = Deserializer.getJob(value.getDATA());
            tv_title.setText(bhJob.getTitle());
            tv_description.setText(bhJob.getDescription());
            tv_category.setText(bhJob.getCategory());
            tv_user.setText(bhJob.getUser());
        }
    }

    @Override
    public void error(Throwable throwable) {

    }
}
