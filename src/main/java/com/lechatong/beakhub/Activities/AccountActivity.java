package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lechatong.beakhub.Entities.Account;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.AccountStreams;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class AccountActivity extends AppCompatActivity {

    Intent intent;

    Context context;

    private TextView tv_username, tv_last_connection, tv_register_at;

    private Button btn_change_password, btn_deactivateaccount;

    private Disposable disposable;

    private Toolbar toolbar;

    private Long account_id;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    private BhAccount bhAccount;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        tv_username = findViewById(R.id.tv_account_name);
        tv_last_connection = findViewById(R.id.tv_last_connection);
        tv_register_at = findViewById(R.id.tv_register_at);

        btn_change_password = findViewById(R.id.btn_change_password);
        btn_deactivateaccount = findViewById(R.id.btn_deactivateaccount);

        btn_deactivateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDeactivateAccount();
            }
        });

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("account_id", account_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        this.loadAccount();
    }

    private void initAccount(Object response){
        bhAccount = Deserializer.getAccount(response);
        tv_username.setText(bhAccount.getUsername());
        tv_last_connection.setText(bhAccount.getLast_login());
        tv_register_at.setText(bhAccount.getCreated_at());
    }

    private void loadAccount(){
        this.disposable = AccountStreams.streamOneUserById(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){
                    @Override
                    public void onNext(APIResponse response) {
                        initAccount(response.getDATA());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showConfirmDeactivateAccount(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.deactivateaccount);
        builder.setMessage(R.string.message_of_deactivation);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editAccount(bhAccount);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void editAccount(BhAccount bhAccount){
        Account account = new Account();

        account.setId(bhAccount.getId());
        account.setUsername(bhAccount.getUsername());
        account.setPassword(bhAccount.getPassword());
        account.setIsActive(false);
        this.disposable = AccountStreams.streamUpdateAccount(bhAccount.getId(), bhAccount)
                .subscribeWith(new DisposableObserver<APIResponse>() {
                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200){
                            sharedPreferences.edit().clear().apply();
                            intent = new Intent(AccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
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
}
