package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.lechatong.beakhub.Entities.User;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Streams.UserStream;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class EditUserActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    BhUser user;

    Intent intent;

    Context context;

    private String responseJSON;

    Long account_id;

    private EditText et_last_name, et_first_name, et_email, et_phone_number;

    private CoordinatorLayout registerLayout;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar_add_profile = (Toolbar) findViewById(R.id.toolbar_edit_user);
        setSupportActionBar(toolbar_add_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account_id = extras.getLong("account_id");
        }

        final Button btn_register = (Button) findViewById(R.id.btn_edit_user);
        et_last_name = (EditText) findViewById(R.id.et_last_name_edit);
        et_first_name = (EditText) findViewById(R.id.et_first_name_edit);
        et_email = (EditText) findViewById(R.id.et_email_edit);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number_edit);
        registerLayout  = (CoordinatorLayout) findViewById(R.id.register_edit_user_layout);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

        this.loadUser();
    }

    @SuppressLint("ResourceAsColor")
    private void saveUser(){
        final ProgressDialog progressDialog = ProgressDialog.show(EditUserActivity.this,
                getString(R.string.save_process), getString(R.string.please_wait), true);
        progressDialog.setCancelable(true);

        final String first_name = et_first_name.getText().toString();
        final String last_name = et_last_name.getText().toString();
        final String email = et_email.getText().toString();
        final String phone_number = et_phone_number.getText().toString();

        if (first_name.isEmpty() || phone_number.isEmpty() || last_name.isEmpty()) {
            progressDialog.cancel();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_field)
                    .setMessage(R.string.error_field_required)
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else{
            if (!Tools.isOnline(getBaseContext())) {
                progressDialog.cancel();
                final Snackbar errorSnackBar = Snackbar.make(registerLayout, getString(R.string.not_network), LENGTH_INDEFINITE);
                errorSnackBar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        errorSnackBar.dismiss();
                    }
                });
                errorSnackBar.setActionTextColor(R.color.bhBack);
                errorSnackBar.show();
            }
            else {
                User bhUser = new User();
                bhUser.setAccount(account_id);
                bhUser.setFirst_name(first_name);
                bhUser.setLast_name(last_name);
                bhUser.setEmail(email);
                bhUser.setPhone_number(phone_number);

                BeakHubService.editUserById(this, account_id, bhUser);
            }
        }
    }

    private void initField(BhUser bhUser){
        et_email.setText(bhUser.getEmail());
        et_last_name.setText(bhUser.getLast_name());
        et_first_name.setText(bhUser.getFirst_name());
        et_phone_number.setText(bhUser.getPhone_number());
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 200){
            Intent intent = new Intent();
            intent.putExtra("account_id", account_id);
            setResult(RESULT_OK, intent);
            finish();
        }else if(value.getCODE() == 400){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(value.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }
    }

    @Override
    public void error(Throwable throwable) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(throwable.getMessage())
                .setCancelable(true)
                .setIcon(R.drawable.ic_error_outline_bh_24dp)
                .show();
    }

    private void getUser(BhUser bhUser){
        user = bhUser;
        initField(user);
    }

    private void loadUser(){
        this.disposable = UserStream.streamOneUserById(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        getUser(Deserializer.getUser(response.getDATA()));
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
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }
}
