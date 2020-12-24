package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.lechatong.beakhub.Adapter.JobSearchAdapter;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ItemClickSupport;
import com.lechatong.beakhub.Tools.Streams.JobStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<BhJob> bhJobList;

    private JobSearchAdapter jobAdapter;

    private Disposable disposable;

    private MaterialToolbar toolbar;

    Intent intent;

    Context context;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    SharedPreferences sharedPreferences;

    private Long account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        context = this;

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        toolbar = findViewById(R.id.toolbar_fav);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_fav);

        this.configureRecyclerView();

        this.loadRecyclerView();

        this.configureOnClickRecyclerView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void loadRecyclerView(){
        this.disposable = JobStreams.streamJobsFavByUser(account_id)
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

        this.jobAdapter = new JobSearchAdapter(this.bhJobList);

        this.recyclerView.setAdapter(jobAdapter);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        ItemClickSupport.addTo(recyclerView, R.layout.card_job_search)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        BhJob bhJob = jobAdapter.getBhJob(position);
                        intent = new Intent(context, ProfileJobActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("job_id", bhJob.getId());
                        bundle.putLong("is_author", 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
    }
}
