package com.lechatong.beakhub.Activities;

/*
 * Author : LeChatong
 * Desc : Main Activity
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.gson.internal.LinkedTreeMap;
import com.lechatong.beakhub.Fragments.HomeFragment;
import com.lechatong.beakhub.Fragments.MyJobsFragment;
import com.lechatong.beakhub.Fragments.NotificationFragment;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Fragments.SearchFragment;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.CircleTransform;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.WebService.BeakHubService;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener, ServiceCallback<APIResponse> {

    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView tvFullName;
    private TextView tvEmail;
    private ImageView imgProfile;

    private Fragment fragmentHome, fragmentMyJob, fragmentSearch, fragmentNotification;

    private static final int FRAGMENT_HOME = 0;

    private static final int FRAGMENT_MY_JOB = 1;

    private static final int FRAGMENT_SEARCH = 2;

    private static final int FRAGMENT_NOTIFICATION = 3;

    private Disposable disposable;

    private Long account_id;

    private Intent intent;

    private Context context;

    private static final int REQUEST_CODE = 1;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String USERNAME = "USERNAME";

    private static final String PREFS = "PREFS";

    SharedPreferences sharedPreferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_home);
                    showFragment(FRAGMENT_HOME);
                    return true;
                case R.id.navigation_my_job:
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_my_job);
                    showFragment(FRAGMENT_MY_JOB);
                    return true;
                case R.id.navigation_search:
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.search);
                    showFragment(FRAGMENT_SEARCH);
                    return true;
                case R.id.navigation_notification:
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.page_notifications);
                    showFragment(FRAGMENT_NOTIFICATION);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        context = this;

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        getIntent();

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.configureBottomNavigationView();

        Bundle extras = getIntent().getExtras();

        /*if (extras != null) {
            account_id = extras.getLong("account_id");
        }*/

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        BeakHubService.getUserById(this, account_id);

        this.showFirstFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View v = bottomNavigationView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.custom_badge, itemView, true);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.about)
                    .setView(R.layout.about)
                    .setCancelable(true)
                    .setIcon(R.drawable.beak_solo)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            intent = new Intent(this, ProfileUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account_id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (id == R.id.nav_favorite){
            intent = new Intent(this, FavoriteActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account_id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.disconnection)
                    .setMessage(R.string.disconnection_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().clear().apply();
                            intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("ResourceAsColor")
    private void configureToolBar(){
        this.toolbar = (MaterialToolbar) findViewById(R.id.toolbarHome);
        this.toolbar.setTitleTextColor(R.color.White);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View vNavHeaderMain = navigationView.getHeaderView(0);

        tvFullName = (TextView) vNavHeaderMain.findViewById(R.id.tvLongNameNH);
        tvEmail = (TextView) vNavHeaderMain.findViewById(R.id.tvEmailNH);
        imgProfile = (ImageView) vNavHeaderMain.findViewById(R.id.ivProfilePicNH);
    }

    private void configureBottomNavigationView(){
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav_view);
    }

    @Override
    public void success(APIResponse value) {

        if(value.getCODE() == 204){
            /**
             * Si le compte n'existe pas
             */
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(value.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else{

            if(value.getCODE() == 404){
                /**
                 * Si le compte existe mais n'a pas d'utilisateur affecté
                 */
                intent = new Intent(context, RegisterUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("account_id", account_id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }else if(value.getCODE() == 200){
                /**
                 * Si l'utilisateur existe on ouvre le HomeActivity
                 */
                BhUser user = Deserializer.getUser(value.getDATA());
                tvFullName.setText(String.format("%s %s", user.getFirst_name(), user.getLast_name()));
                tvEmail.setText(user.getEmail());
                if (!user.getUrl_picture().isEmpty()){
                    Picasso.with(context)
                            .load(user.getUrl_picture())
                            .centerCrop()
                            .transform(new CircleTransform(50,0))
                            .fit()
                            .into(imgProfile);
                }else{
                    Picasso.with(context)
                            .load("https://lechatonguniverse.herokuapp.com/media/photo_user/lechatong.jpg")
                            .centerCrop()
                            .transform(new CircleTransform(50,0))
                            .fit()
                            .into(imgProfile);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void error(Throwable throwable) {
        tvFullName.setText("LeChatong");
        tvEmail.setText("ulrich.tchatong@gmail.com");
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(throwable.toString())
                .setCancelable(true)
                .setIcon(R.drawable.ic_error_outline_bh_24dp)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private Observable<String> getObservable(){
        return Observable.just("LeChatong");
    }

    private DisposableObserver<String> getSubscriber(){
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete");
            }
        };
    }

    public Long getAccount_id() {
        return account_id;
    }

    private void streamShow(){
        this.disposable = this.getObservable().subscribeWith(getSubscriber());
    }

    private void disposeWhenDestroy(){
        if(this.disposable != null && !this.disposable.isDisposed())
            this.disposable.dispose();
    }

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_HOME :
                this.showHomeFragment();
                break;
            case FRAGMENT_MY_JOB :
                this.showMyJobFragment();
                break;
            case FRAGMENT_SEARCH:
                this.showSearchFragment();
                break;
            case FRAGMENT_NOTIFICATION:
                this.showNotificationFragment();
                break;
            default:
                break;
        }
    }

    private void showHomeFragment(){
        if (this.fragmentHome == null) this.fragmentHome = HomeFragment.newInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_home));

        this.startTransactionFragment(this.fragmentHome);
    }

    private void showMyJobFragment(){
        if (this.fragmentMyJob == null) this.fragmentMyJob = MyJobsFragment.newInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_my_job));

        this.startTransactionFragment(this.fragmentMyJob);
    }

    private void showSearchFragment(){
        if (this.fragmentSearch == null) this.fragmentSearch = SearchFragment.newInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_search));

        this.startTransactionFragment(this.fragmentSearch);
    }

    private void showNotificationFragment(){
        if (this.fragmentNotification == null) this.fragmentNotification = NotificationFragment.newInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_notifications));

        this.startTransactionFragment(this.fragmentNotification);
    }

    private void showFirstFragment(){
        Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(R.id.frame_main_layout);

        if (fragmentVisible == null) {
            this.showHomeFragment();
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    public void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_main_layout, fragment).commit();
        }
    }

}
