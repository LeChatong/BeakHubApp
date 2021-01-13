package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Streams.AccountStreams;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText et_email;

    private Button btn_send_mail;

    private Disposable disposable;

    private String email;

    private Intent intent;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btn_send_mail = findViewById(R.id.btn_send_mail);
        et_email = (EditText)findViewById(R.id.et_email_forgot);



        btn_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    private void getResponse(APIResponse response){
        progressDialog.cancel();
        if(response.getCODE().equals(404L)){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_field)
                    .setMessage(response.getMESSAGE())
                    .setCancelable(true)
                    .setIcon(R.drawable.ic_error_outline_bh_24dp)
                    .show();
        }else
            if(response.getCODE().equals(200L)){
                new AlertDialog.Builder(this)
                        .setTitle(R.string.success)
                        .setMessage(response.getMESSAGE())
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_vector_send)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);*/
                                finish();
                            }
                        })
                        .show();
            }
    }

    private void sendEmail(){
        final String email = et_email.getText().toString();
        progressDialog = ProgressDialog.show(ForgotPasswordActivity.this,
                getString(R.string.ongoing_process), getString(R.string.please_wait), true);
        progressDialog.setCancelable(true);
        this.disposable = AccountStreams.streamInitPassword(email)
                .subscribeWith(new DisposableObserver<APIResponse>() {
                    @Override
                    public void onNext(APIResponse response) {
                        getResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.cancel();
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle(R.string.error_field)
                                .setMessage(e.getMessage())
                                .setCancelable(true)
                                .setIcon(R.drawable.ic_error_outline_bh_24dp)
                                .show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
