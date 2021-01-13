package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.lechatong.beakhub.Entities.Account;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.AccountStreams;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ChangePasswordActivity extends AppCompatActivity {

    private Intent intent;

    private Context context;

    private MaterialToolbar toolbar;

    private EditText et_old_pass, et_new_pass, et_confirm_pass;

    private Button btn_save_change;

    private Disposable disposable;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    private SharedPreferences sharedPreferences;

    private Long account_id;

    private ProgressDialog progressDialog;

    private BhAccount bhAccount;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        account = new Account();

        context = this;

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        toolbar = findViewById(R.id.toolbar_change_pass);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        et_old_pass = findViewById(R.id.et_old_pass);
        et_new_pass = findViewById(R.id.et_new_pass);
        et_confirm_pass = findViewById(R.id.et_confirm_pass);
        btn_save_change = findViewById(R.id.btn_save_change);

        btn_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(context,
                        getString(R.string.ongoing_process), getString(R.string.please_wait), true);
                progressDialog.setCancelable(true);
                onChangePassword();
            }
        });
    }

    private void onChangePassword(){
        this.disposable = AccountStreams.streamAccountByIdAndPassword(account_id, et_old_pass.getText().toString())
                .subscribeWith(new DisposableObserver<APIResponse>(){
                    @Override
                    public void onNext(APIResponse response) {
                        verify_old_password(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void verify_old_password(APIResponse response){
        if(response.getCODE().equals(200L)){
            if(et_new_pass.getText().toString().equals(et_confirm_pass.getText().toString())){
                bhAccount = Deserializer.getAccount(response.getDATA());
                account.setUsername(bhAccount.getUsername());
                account.setPassword(et_new_pass.getText().toString());
                update_password();
            }
            else{
                progressDialog.cancel();
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error_field)
                        .setMessage(R.string.password_not_conform)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_error_outline_bh_24dp)
                        .show();
            }
        }else{
            progressDialog.cancel();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_field)
                    .setMessage(response.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }
    }

    private void update_password(){
        this.disposable = AccountStreams.streamChangePassword(account_id, account)
                .subscribeWith(new DisposableObserver<APIResponse>(){
                    @Override
                    public void onNext(APIResponse response) {
                        getResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getResponse(APIResponse response){
        progressDialog.cancel();
        if(response.getCODE().equals(204L)){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_field)
                    .setMessage(response.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else
        if(response.getCODE().equals(200L)){
            sharedPreferences.edit().clear().apply();
            new AlertDialog.Builder(this)
                    .setTitle(R.string.success)
                    .setMessage(response.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_vector_send)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            intent = new Intent(context, LoginActivity.class);
                            finishAffinity();
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }
    }
}
