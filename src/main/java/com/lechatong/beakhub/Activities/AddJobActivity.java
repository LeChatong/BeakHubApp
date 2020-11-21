package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.lechatong.beakhub.Adapter.CategoryAdapter;
import com.lechatong.beakhub.Entities.Job;
import com.lechatong.beakhub.Models.BhCategory;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Streams.CategoryStreams;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class AddJobActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    private Intent intent;

    private Context context;

    private Long account_id;

    private Long category_id;

    private Disposable disposable;

    private EditText etTitleJob;

    private EditText etDescJob;

    private TextView tvCategory;

    private CoordinatorLayout saveJoblayout;

    private ProgressDialog progressDialog;

    private List<BhCategory> categoryList;

    private CategoryAdapter categoryAdapter;

    private Job bhJob;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_job);

        toolbar = findViewById(R.id.toolbar_add_job);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;

        bhJob = new Job();

        final Button btn_register = (Button) findViewById(R.id.btn_register_job);
        etTitleJob = (EditText) findViewById(R.id.et_title_job);
        etDescJob = (EditText) findViewById(R.id.et_desc_job);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        saveJoblayout = (CoordinatorLayout) findViewById(R.id.register_job_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account_id = extras.getLong("account_id");
        }

        this.loadCategory();

        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtainCategory();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(AddJobActivity.this,
                        getString(R.string.save_process), getString(R.string.please_wait), true);
                progressDialog.setCancelable(true);
                final String title = etTitleJob.getText().toString();
                final String description = etDescJob.getText().toString();

                if (title.isEmpty() || description.isEmpty()) {
                    progressDialog.cancel();
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.error_field)
                            .setMessage(R.string.error_field_required)
                            .setCancelable(true)
                            .setIcon(R.drawable.ic_error_outline_bh_24dp)
                            .show();
                }else{
                    if (!Tools.isOnline(getBaseContext())) {
                        progressDialog.cancel();
                        final Snackbar errorSnackBar = Snackbar.make(saveJoblayout, getString(R.string.not_network), LENGTH_INDEFINITE);
                        errorSnackBar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorSnackBar.dismiss();
                            }
                        });
                        errorSnackBar.setActionTextColor(R.color.bhBack);
                        errorSnackBar.show();
                    }else{
                        bhJob.setUser(account_id);
                        bhJob.setTitle(title);
                        bhJob.setDescription(description);
                        bhJob.setCategory(category_id);
                        bhJob.setIs_active(true);
                        saveJob(bhJob);
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    public void saveJob(Job bhJob){
        BeakHubService.addJob(this, bhJob);
    }

    @Override
    public void success(APIResponse value) {
        progressDialog.cancel();
        if(value.getCODE() != 201){
            /*intent = new Intent(context,HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account_id);
            intent.putExtras(bundle);
            startActivity(intent);*/
            finish();
        }else if(value.getCODE() != 400){
            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(Throwable throwable) {
        progressDialog.cancel();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void loadCategory(){
        this.disposable = CategoryStreams.streamAllCategory()
                .subscribeWith(new DisposableObserver<List<BhCategory>>() {

                    @Override
                    public void onNext(List<BhCategory> bhCategories) {
                        updateCategoryAdapter(bhCategories);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateCategoryAdapter(List<BhCategory> bhCategories){
        this.categoryList = new ArrayList<>();
        categoryList.addAll(bhCategories);
        this.categoryAdapter = new CategoryAdapter(this,R.layout.spin_category,categoryList);
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    private void obtainCategory(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.category_prompt);

        builder.setSingleChoiceItems(categoryAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BhCategory bhCategory = categoryAdapter.getItem(i);
                tvCategory.setText(bhCategory.getTitle());
                category_id = bhCategory.getId();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
