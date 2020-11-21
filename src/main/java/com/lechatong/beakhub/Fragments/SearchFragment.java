package com.lechatong.beakhub.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Adapter.JobAdapter;
import com.lechatong.beakhub.Adapter.JobSearchAdapter;
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
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<BhJob> bhJobList;

    private JobSearchAdapter jobSearchAdapter;

    private Disposable disposable;

    private ListView listView;

    private androidx.appcompat.widget.SearchView searchView;

    private Intent intent;

    private Context context;

    public SearchFragment(){};

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchView);

        recyclerView = view.findViewById(R.id.recycler_view_search);

        this.configureRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadRecyclerView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadRecyclerView(newText);
                return false;
            }
        });

        this.configureOnClickRecyclerView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void configureRecyclerView(){
        this.bhJobList = new ArrayList<>();

        this.jobSearchAdapter = new JobSearchAdapter(this.bhJobList);

        this.recyclerView.setAdapter(jobSearchAdapter);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadRecyclerView(String search){
        this.disposable = JobStreams.streamJobSearch(search)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        updateListJob(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateListJob(APIResponse value){
        bhJobList.clear();
        List<BhJob> bhJobs = new ArrayList<BhJob>();
        ArrayList<BhJob> bhJobArrayList = (ArrayList<BhJob>) value.getDATA();
        for(Object treeMap : bhJobArrayList){
            bhJobs.add(Deserializer.getJob(treeMap));
        }
        bhJobList.addAll(bhJobs);
        jobSearchAdapter.notifyDataSetChanged();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.card_job_search)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        BhJob bhJob = jobSearchAdapter.getBhJob(position);
                        intent = new Intent(getActivity(), ProfileJobActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("job_id", bhJob.getId());
                        bundle.putLong("is_author", 0);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
    }
}
