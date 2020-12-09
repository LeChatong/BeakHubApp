package com.lechatong.beakhub.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.LinkedTreeMap;
import com.lechatong.beakhub.Activities.HomeActivity;
import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Adapter.JobAdapter;
import com.lechatong.beakhub.Activities.AddJobActivity;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ItemClickSupport;
import com.lechatong.beakhub.Tools.Streams.JobStreams;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<BhJob> bhJobList;

    private JobAdapter jobAdapter;

    private Disposable disposable;

    private com.google.android.material.floatingactionbutton.FloatingActionButton btnAddJob;

    Intent intent;

    Context context;

    private Long account_id;

    public HomeFragment(){};

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity activity = (HomeActivity) getActivity();
        assert activity != null;
        account_id = activity.getAccount_id();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                loadRecyclerView();
                return false;
            }
        });

        this.configureRecyclerView();

        this.loadRecyclerView();

        btnAddJob = (com.google.android.material.floatingactionbutton.FloatingActionButton)view.findViewById(R.id.btn_add_job);

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), AddJobActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("account_id", account_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        this.configureOnClickRecyclerView();

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    private void loadRecyclerView(){
        this.disposable = JobStreams.streamJobByUserId(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse bhJobs) {
                        updateListJob(bhJobs);
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
        this.bhJobList = new ArrayList<>();

        this.jobAdapter = new JobAdapter(this.bhJobList);

        this.recyclerView.setAdapter(jobAdapter);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateListJob(APIResponse value){
        List<BhJob> bhJobs = new ArrayList<BhJob>();
        ArrayList<BhJob> bhJobArrayList = (ArrayList<BhJob>) value.getDATA();
        for(Object treeMap : bhJobArrayList){
            bhJobs.add(Deserializer.getJob(treeMap));
        }
        bhJobList.addAll(bhJobs);
        jobAdapter.notifyDataSetChanged();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.card_job)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        BhJob bhJob = jobAdapter.getBhJob(position);
                        intent = new Intent(getActivity(), ProfileJobActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("job_id", bhJob.getId());
                        bundle.putLong("is_author", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
    }
}


