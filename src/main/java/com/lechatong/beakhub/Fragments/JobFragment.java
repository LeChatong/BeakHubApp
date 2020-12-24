package com.lechatong.beakhub.Fragments;

/*
 * Author : LeChatong
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lechatong.beakhub.Activities.AddJobActivity;
import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Entities.Job;
import com.lechatong.beakhub.Entities.UserLikeJob;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Models.BhUserLikeJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Streams.JobStreams;
import com.lechatong.beakhub.Tools.Streams.LikeStreams;
import com.lechatong.beakhub.WebService.BeakHubService;
import com.like.LikeButton;
import com.like.OnLikeListener;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.Context.MODE_PRIVATE;

public class JobFragment extends Fragment implements ServiceCallback<APIResponse> {

    private Intent intent;

    private Context context;

    private Long job_id, is_author, account_id;

    private static final String ID_JOB = "JOB_ID";

    private static final String IS_AUTHOR = "IS_AUTHOR";

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    private TextView tv_title, tv_description, tv_category, tv_user, tvCreateJob;

    private BhUserLikeJob like;

    private BhJob job;

    private LikeButton likeButton;

    private FloatingActionButton btnEditJob;

    private Disposable disposableLike;

    private Disposable disposableJob;

    private Button btn_deactivatejob;

    public JobFragment() {
    }

    public static JobFragment newInstance() {
        return new JobFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProfileJobActivity activity = (ProfileJobActivity) getActivity();

        assert activity != null;
        job_id = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(ID_JOB,0);
        account_id = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(ID_ACCOUNT, 0);
        is_author = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(IS_AUTHOR,0);

        View view = inflater.inflate(R.layout.fragment_job, container, false);

        tv_title = (TextView) view.findViewById(R.id.tvTitleJob);
        tv_description = (TextView) view.findViewById(R.id.tvDescriptionJob);
        tv_category = (TextView) view.findViewById(R.id.tvCategoryJob);
        tv_user = (TextView) view.findViewById(R.id.tvUSerJob);
        tvCreateJob = (TextView) view.findViewById(R.id.tvCreateJob);
        btn_deactivatejob = view.findViewById(R.id.btn_deactivatejob);
        btnEditJob = view.findViewById(R.id.btn_edit_job);
        //likeButton = view.findViewById(R.id.btn_like);

        this.loadJob();

        btnEditJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), AddJobActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 201 || value.getCODE() == 200){
            System.out.println("It's OK");
        }
    }

    @Override
    public void error(Throwable throwable) {
        System.err.println("It's Not OK");
    }

    private void getLike(BhUserLikeJob bhUserLikeJob){
        like = bhUserLikeJob;
        if(like != null){
            if(like.getIsLike()){
                likeButton.setLiked(true);
            }else{
                likeButton.setLiked(false);
            }
        }else {
            likeButton.setLiked(false);
        }
    }

    private void initJob(BhJob bhJob){
        job = bhJob;
        tv_title.setText(job.getTitle());
        tv_description.setText(job.getDescription());
        tv_category.setText(job.getCategory());
        tv_user.setText(job.getUser());
        tvCreateJob.setText(job.getCreatedAt());
        if(job.getIsActive()){
            btn_deactivatejob.setText(R.string.deactivate);
            btn_deactivatejob.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_custom_min));
            btn_deactivatejob.setTextColor(getResources().getColor(R.color.White));
            btn_deactivatejob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(R.string.deactivatejob);
                    builder.setMessage(R.string.message_of_deactivation_job);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Job job_to_save = new Job();
                            job_to_save.setCategory(job.getCategoryId());
                            job_to_save.setDescription(job.getDescription());
                            job_to_save.setTitle(job.getTitle());
                            job_to_save.setUser(job.getUserId());
                            job_to_save.setIs_active(false);
                            saveJob(job.getId(), job_to_save);
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            });
        }
        else{
            btn_deactivatejob.setText(R.string.activate);
            btn_deactivatejob.setBackground(ContextCompat.getDrawable(context, R.drawable.et_inv_custom));
            btn_deactivatejob.setTextColor(getResources().getColor(R.color.bgBeakHub));
            btn_deactivatejob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Job job_to_save = new Job();
                    job_to_save.setCategory(job.getCategoryId());
                    job_to_save.setDescription(job.getDescription());
                    job_to_save.setTitle(job.getTitle());
                    job_to_save.setUser(job.getUserId());
                    job_to_save.setIs_active(true);
                    saveJob(job.getId(), job_to_save);
                }
            });
        }

        this.configurationEditButton();
    }

    private void saveJob(Long job_id, Job job){
        this.disposableJob = JobStreams.streamEditJob(job_id, job)
                .subscribeWith(new DisposableObserver<APIResponse>() {
                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200)
                            initJob(Deserializer.getJob(response.getDATA()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadJob(){
        this.disposableLike = JobStreams.streamOneJob(job_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200)
                            initJob(Deserializer.getJob(response.getDATA()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadLike(){
        this.disposableLike = LikeStreams.streamOneLike(job_id, account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200){
                            getLike(Deserializer.getUserLikeJob(response.getDATA()));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private  void configurationEditButton(){
        if(account_id == job.getUserId()){
            btnEditJob.setVisibility(View.VISIBLE);
            btn_deactivatejob.setVisibility(View.VISIBLE);
        }else{
            btnEditJob.setVisibility(View.INVISIBLE);
            btn_deactivatejob.setVisibility(View.INVISIBLE);
        }
    }

    private void configurationLikeButton(){

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(like != null){
                    UserLikeJob userLikeJob = new UserLikeJob(
                            like.getId(),
                            like.getJobId(),
                            like.getUserId(),
                            true
                    );

                    BeakHubService.updateLike(newInstance(), like.getId(), userLikeJob);
                    likeButton.setLiked(true);
                }else{
                    UserLikeJob userLikeJob = new UserLikeJob(
                            null,
                            job_id,
                            account_id,
                            true
                    );
                    BeakHubService.addLike(newInstance(), userLikeJob);
                    likeButton.setLiked(true);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                UserLikeJob userLikeJob = new UserLikeJob(
                        like.getId(),
                        like.getJobId(),
                        like.getUserId(),
                        false
                );
                BeakHubService.updateLike(newInstance(), like.getId(), userLikeJob);
                likeButton.setLiked(false);
            }
        });
    }

    private void disposeWhenDestroy(){
        if (this.disposableLike != null && !this.disposableLike.isDisposed()) this.disposableLike.dispose();
        if (this.disposableJob != null && !this.disposableJob.isDisposed()) this.disposableJob.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }
}
