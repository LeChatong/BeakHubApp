package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.lechatong.beakhub.Entities.User;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import java.util.Objects;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class RegisterUserActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    Intent intent;
    Context context;

    EditText etFirstname;
    EditText etLastname;
    EditText etEmail;
    EditText etPhone;
    RelativeLayout registerLayout;
    Long account_id;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        context = this;

        final Button btn_register = (Button) findViewById(R.id.btn_save_user);
        etFirstname = (EditText) findViewById(R.id.et_first_name);
        etLastname = (EditText) findViewById(R.id.et_last_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone_number);
        registerLayout  = (RelativeLayout) findViewById(R.id.register_user_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account_id = extras.getLong("account_id");
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(RegisterUserActivity.this,
                        getString(R.string.save_process), getString(R.string.please_wait), true);
                progressDialog.setCancelable(true);

                saveUser();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void saveUser(){

        final String first_name = etFirstname.getText().toString();
        final String last_name = etLastname.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone_number = etPhone.getText().toString();

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

                BeakHubService.addUser(this, bhUser);
            }
        }
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 400){
            progressDialog.cancel();
            new AlertDialog.Builder(context)
                    .setTitle(R.string.error)
                    .setMessage(value.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else if(value.getCODE() == 201){
            progressDialog.cancel();
            BhUser user = Deserializer.getUser(value.getDATA());
            intent = new Intent(context, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", user.getId());
            intent.putExtras(bundle);
            progressDialog.cancel();
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void error(Throwable throwable) {
        progressDialog.cancel();
        final Snackbar errorSnackBar = Snackbar.make(registerLayout, Objects.requireNonNull(throwable.getMessage()), LENGTH_INDEFINITE);
        errorSnackBar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorSnackBar.dismiss();
            }
        });
        errorSnackBar.setActionTextColor(R.color.White);
        errorSnackBar.setBackgroundTint(R.color.White);
        errorSnackBar.show();
    }
}
