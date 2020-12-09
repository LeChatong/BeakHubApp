package com.lechatong.beakhub.Activities;

/*
  Author : LeChatong
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.lechatong.beakhub.ForgotPasswordActivity;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class LoginActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    Intent intent;
    Context context;
    EditText etUsername;
    EditText etPassword;
    CoordinatorLayout loginLayout;
    private String responseJSON;
    BhUser user;
    ProgressDialog progressDialog;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";
    private static final String USERNAME = "USERNAME";
    private static final String PREFS = "PREFS";
    SharedPreferences sharedPreferences;


    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        final Button btn_login = (Button) findViewById(R.id.btn_login);
        final TextView txtRegister = (TextView) findViewById(R.id.tv_register);
        final TextView txtForgot = (TextView) findViewById(R.id.tv_forgot);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        loginLayout = (CoordinatorLayout) findViewById(R.id.login_layout);

        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginAttempt();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, RegisterAccountActivity.class);
                startActivity(intent);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void loginAttempt(){
        String error = null;
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (username.isEmpty()) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.username_require)
                    .setMessage(R.string.error_field_required)
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else{
            if (password.isEmpty()){
                new AlertDialog.Builder(context)
                        .setTitle(R.string.password_require)
                        .setMessage(R.string.error_field_required)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_error_outline_bh_24dp)
                        .show();
            }
            else {
                if (!Tools.isOnline(getBaseContext())) {
                    final Snackbar errorSnackBar = Snackbar.make(loginLayout, getString(R.string.not_network), LENGTH_INDEFINITE);
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
                    Connecxion(username, password);
                }
            }
        }
    }

    public void Connecxion(final String username, final String password) {
        progressDialog = ProgressDialog.show(LoginActivity.this,
                getString(R.string.login_process), getString(R.string.please_wait), true);
        progressDialog.setCancelable(true);
        BeakHubService.getAccountConnection(this, username, password);
    }


    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 404){
            progressDialog.cancel();
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(R.string.username_or_password_incorrect)
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else if(value.getCODE() == 200){
            progressDialog.cancel();
            BhAccount account = Deserializer.getAccount(value.getDATA());
            intent = new Intent(context,HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account.getId());

            sharedPreferences
                    .edit()
                    .putLong(ID_ACCOUNT, account.getId())
                    .putString(USERNAME, account.getUsername())
                    .apply();

            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void error(Throwable throwable) {
        progressDialog.cancel();
        new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(throwable.toString())
                .setCancelable(true)
                .setIcon(R.drawable.ic_error_outline_bh_24dp)
                .show();
    }
}
