package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class RegisterAccountActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    Intent intent;
    Context context;

    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    LinearLayout registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        context = this;

        final Button btn_register = (Button) findViewById(R.id.btn_register);
        etUsername = (EditText) findViewById(R.id.et_username_reg);
        etPassword = (EditText) findViewById(R.id.et_password_reg);
        etConfirmPassword = (EditText) findViewById(R.id.et_repassword_reg);
        registerLayout  = (LinearLayout) findViewById(R.id.register_layout);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAccount();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void saveAccount(){

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String repassword = etConfirmPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_field)
                    .setMessage(R.string.error_field_required)
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else {
            if (!password.equals(repassword)){
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error_field)
                        .setMessage(R.string.error_password)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_error_outline_bh_24dp)
                        .show();
            }else{
                if (!Tools.isOnline(getBaseContext())) {
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

                    BhAccount bhAccount = new BhAccount();
                    bhAccount.setUsername(username);
                    bhAccount.setPassword(password);

                    BeakHubService.addAccount(this,bhAccount);

                }
            }
        }
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 208){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(value.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else if(value.getCODE() == 201){
            BhAccount account = Deserializer.getAccount(value.getDATA());
            intent = new Intent(context, RegisterUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account.getId());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void error(Throwable throwable) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_field)
                .setMessage(throwable.getMessage())
                .setCancelable(true)
                .setIcon(R.drawable.ic_error_outline_bh_24dp)
                .show();
    }
}
