package com.lechatong.beakhub.Activities;

/*
 * Author : LeChatong
 * Desc : This Activity is use for show details of Job with the help of
 * fragments JobFragment, AdressFragment, CommentFragment
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lechatong.beakhub.Adapter.TabsProJobAdapter;
import com.lechatong.beakhub.Entities.UserLikeJob;
import com.lechatong.beakhub.Fragments.CommentFragment;
import com.lechatong.beakhub.Models.BhUserLikeJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.LikeStreams;
import com.lechatong.beakhub.WebService.BeakHubService;
import com.like.LikeButton;
import com.like.OnLikeListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ProfileJobActivity extends AppCompatActivity {

    Long job_id;

    Intent intent;

    Context context;

    private Long account_id;

    private Long is_author;

    private static final int REQUEST_CODE = 1;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String ID_JOB = "JOB_ID";

    private static final String IS_AUTHOR = "IS_AUTHOR";

    private static final String PREFS = "PREFS";

    SharedPreferences sharedPreferences;

    private LikeButton likeButton;

    private Disposable disposableLike;

    private BhUserLikeJob like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_job);
        MaterialToolbar toolbar = findViewById(R.id.toolbar_pro_job);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sharedPreferences
                    .edit()
                    .putLong(ID_JOB, extras.getLong("job_id"))
                    .putLong(IS_AUTHOR, extras.getLong("is_author"))
                    .apply();
            job_id = sharedPreferences.getLong(ID_JOB, 0);
            is_author = sharedPreferences.getLong(IS_AUTHOR, 0);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_pro_job);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.job));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.address));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.comments));

        likeButton = findViewById(R.id.btn_like);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager_pro_job);

        TabsProJobAdapter tabsAdapter = new TabsProJobAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(tabsAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.loadLike();

        this.configurationLikeButton();

    }

    public Long getIs_author(){ return is_author; }

    public Long getAccount_id() {
        return account_id;
    }

    public Long getJob_id() {
        return job_id;
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

                    updateLike(like.getId(), userLikeJob);
                    likeButton.setLiked(true);
                }else{
                    UserLikeJob userLikeJob = new UserLikeJob(
                            null,
                            job_id,
                            account_id,
                            true
                    );
                    newLike(userLikeJob);
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
                updateLike(like.getId(), userLikeJob);
                likeButton.setLiked(false);
            }
        });
    }

    private void updateLike(Long id_like, UserLikeJob userLikeJob) {
        this.disposableLike = LikeStreams.streamEditLike(id_like, userLikeJob)
                .subscribeWith(new DisposableObserver<APIResponse>() {
                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200)
                            getLike(Deserializer.getUserLikeJob(response.getDATA()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void newLike(UserLikeJob userLikeJob) {
        this.disposableLike = LikeStreams.streamNewLike(userLikeJob)
                .subscribeWith(new DisposableObserver<APIResponse>() {
                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200)
                            getLike(Deserializer.getUserLikeJob(response.getDATA()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void disposeWhenDestroy(){
        if (this.disposableLike != null && !this.disposableLike.isDisposed()) this.disposableLike.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().remove(ID_JOB).apply();
        this.disposeWhenDestroy();
    }

}
