package com.lechatong.beakhub.Fragments;

/**
 * Author: LeChatong
 * Desc: This fragment show the notification details
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lechatong.beakhub.Models.BhEvent;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.CircleTransform;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.JobStreams;
import com.lechatong.beakhub.Tools.Streams.NotificationStreams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailNotificationFragment extends Fragment {

    private Bundle bundle;

    private Long id;

    private Long job_id;

    private Disposable disposableEvent, disposableJob;

    private TextView tvDetailNotif, tvDetailComment, tvDetailDuring, tvDetailTitleJob,
            tvDetailCategory, tvDetailDescJob, tv_detail_nb_like, tv_detail_nb_comment;

    CircularImageView circularImageView;

    private BhEvent bhEvent;

    private BhJob bhJob;

    public DetailNotificationFragment() {

    }

    private Context context;

    public static DetailNotificationFragment newInstance(){ return new DetailNotificationFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_notification, container, false);

        tvDetailNotif = view.findViewById(R.id.tvDetailNotif);
        tvDetailComment = view.findViewById(R.id.tvDetailComment);
        tvDetailDuring = view.findViewById(R.id.tvDetailDuring);
        tvDetailTitleJob = view.findViewById(R.id.tvDetailTitleJob);
        tvDetailCategory = view.findViewById(R.id.tvDetailCategory);
        tvDetailDescJob = view.findViewById(R.id.tvDetailDescJob);
        tv_detail_nb_like = view.findViewById(R.id.tv_detail_nb_like);
        tv_detail_nb_comment = view.findViewById(R.id.tv_detail_nb_comment);
        circularImageView = view.findViewById(R.id.ivDetailPP);

        bundle = getArguments();

        if(bundle != null){
            id = bundle.getLong("id");
            job_id = bundle.getLong("job_id");
        }

        getNotification(id);
        getJob(job_id);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @SuppressLint("SetTextI18n")
    private void initNotification(APIResponse response){
        bhEvent = Deserializer.getEvent(response.getDATA());

        if(bhEvent.getAction().equals("COMMENT")){
            tvDetailNotif.setText(bhEvent.getName_sender() + " " +
                    context.getString(R.string.notif_message_2) + " " + bhEvent.getJob_title());
            tvDetailComment.setEnabled(true);
            //tvDetailComment.setText();
        }else {
            tvDetailNotif.setText(bhEvent.getName_sender() + " " +
                    context.getString(R.string.notif_message_1) + " " + bhEvent.getJob_title());
            tvDetailComment.setEnabled(true);
        }

        if (!bhEvent.getUrl_picture_sender().isEmpty()){
            Picasso.with(context)
                    .load(bhEvent.getUrl_picture_sender())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(circularImageView);
        }else{
            Picasso.with(context)
                    .load("https://lechatonguniverse.herokuapp.com/media/photo_user/lechatong.jpg")
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(circularImageView);
        }

        tvDetailDuring.setText(bhEvent.getCreatedAt());

    }

    @SuppressLint("SetTextI18n")
    private void initJob(APIResponse response){
        bhJob = Deserializer.getJob(response.getDATA());

        tvDetailTitleJob.setText(bhJob.getTitle());
        tvDetailCategory.setText(bhJob.getCategory());
        tvDetailDescJob.setText(bhJob.getDescription());
        tv_detail_nb_like.setText(getString(R.string.like)  + " "+bhJob.getNumber_like());
        tv_detail_nb_comment.setText(getString(R.string.comments)  + " "+ bhJob.getNumber_comment());
    }

    private void getNotification(Long id){
        this.disposableEvent = NotificationStreams.streamNotificationTrue(id)
                .subscribeWith(new DisposableObserver<APIResponse>(){
                    @Override
                    public void onNext(APIResponse response) {
                        initNotification(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getJob(Long job_id){
        this.disposableEvent = JobStreams.streamOneJob(job_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){
                    @Override
                    public void onNext(APIResponse response) {
                        initJob(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
