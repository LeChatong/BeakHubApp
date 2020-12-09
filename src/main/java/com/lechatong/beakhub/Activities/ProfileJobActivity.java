package com.lechatong.beakhub.Activities;

/*
 * Author : LeChatong
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lechatong.beakhub.Adapter.TabsProJobAdapter;
import com.lechatong.beakhub.Fragments.CommentFragment;
import com.lechatong.beakhub.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import java.util.Objects;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_job);
        Toolbar toolbar = findViewById(R.id.toolbar_pro_job);
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
        tabLayout.addTab(tabLayout.newTab().setText(R.string.my_address));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.my_job));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.comments));

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

    }

    public Long getIs_author(){ return is_author; }

    public Long getAccount_id() {
        return account_id;
    }

    public Long getJob_id() {
        return job_id;
    }


}
