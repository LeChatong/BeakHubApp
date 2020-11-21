package com.lechatong.beakhub.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Adapter.ChatAdapter;
import com.lechatong.beakhub.Adapter.JobAdapter;
import com.lechatong.beakhub.Entities.Comment;
import com.lechatong.beakhub.Models.BhComment;
import com.lechatong.beakhub.Models.Chat;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Streams.CommentStreams;
import com.lechatong.beakhub.WebService.BeakHubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class CommentFragment extends Fragment implements ServiceCallback<APIResponse> {

    private RecyclerView recyclerCommentView;

    private Random random;

    private ChatAdapter adapter = null;

    private EditText editMessage;

    private AppCompatImageView imageSend;

    private Context context;

    private Disposable disposable;

    private Long job_id;

    private Long account_id;

    private List<BhComment> bhComments;

    public CommentFragment() {
    }

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        ProfileJobActivity activity = (ProfileJobActivity) getActivity();

        assert activity != null;
        job_id = activity.getJob_id();
        account_id = activity.getAccount_id();

        this.onApplyViews(view);

        return view;
    }

    @SuppressLint("WrongConstant")
    private void onApplyViews(View view) {

        editMessage = (EditText) view.findViewById(R.id.edit_message);

        recyclerCommentView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);

        this.configureRecyclerView();

        this.loadRecyclerView();

        imageSend = (AppCompatImageView) view.findViewById(R.id.image_send);

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });

    }

    private void onSend() {
        String chatContent = editMessage.getText().toString();
        if(!chatContent.isEmpty()){
            Comment comment = new Comment();
            comment.setCommentary(chatContent);
            comment.setJob_id(job_id);
            comment.setUser_id(account_id);
            BeakHubService.addCommentary(this, comment);
        }else{
            Toast.makeText(context, R.string.not_text, Toast.LENGTH_LONG).show();
        }

        editMessage.setText(null);
    }

    private void loadRecyclerView(){
        this.disposable = CommentStreams.streamCommentByJobId(job_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse bhJobs) {
                        updateListCommment(bhJobs);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void configureRecyclerView(){
        this.bhComments = new ArrayList<>();

        this.adapter = new ChatAdapter(this.bhComments, account_id);

        this.recyclerCommentView.setAdapter(adapter);

        this.recyclerCommentView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateListCommment(APIResponse value){
        List<BhComment> bhCommentList = new ArrayList<BhComment>();
        ArrayList<BhComment> bhCommentArrayList = (ArrayList<BhComment>) value.getDATA();
        for(Object treeMap : bhCommentArrayList){
            bhCommentList.add(Deserializer.getComment(treeMap));
        }
        bhComments.addAll(bhCommentList);
        adapter.notifyDataSetChanged();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 201){
            BhComment bhComment = (BhComment) Deserializer.getComment(value.getDATA());
            adapter.add(bhComment);
            recyclerCommentView.smoothScrollToPosition(adapter.getItemCount());
        }else {
            Toast.makeText(context, value.getMESSAGE(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
