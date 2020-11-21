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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.lechatong.beakhub.Entities.Address;
import com.lechatong.beakhub.Models.BhAddress;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Tools;
import com.lechatong.beakhub.WebService.BeakHubService;

import java.util.Objects;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE;

public class AddAddressActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    private TextView tvTitle, tvCountry, tvStreet, tvTown, tvPhoneNumber1, tvPhoneNumber2;

    private Context context;

    private Toolbar toolbar;

    private Long job_id;

    private Button btnSaveAddress;

    private ProgressDialog progressDialog;

    private RelativeLayout saveAddressLayout;

    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.toolbar_add_address);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job_id = extras.getLong("job_id");
        }

        saveAddressLayout = (RelativeLayout) findViewById(R.id.saveAddressLayout);

        tvTitle = (TextView) findViewById(R.id.et_title_address);
        tvCountry = (TextView) findViewById(R.id.et_country);
        tvStreet = (TextView) findViewById(R.id.et_street);
        tvTown = (TextView) findViewById(R.id.et_town);
        tvPhoneNumber1 = (TextView) findViewById(R.id.et_phone_1);
        tvPhoneNumber2 = (TextView) findViewById(R.id.et_phone_2);

        btnSaveAddress = (Button) findViewById(R.id.btn_register_address);

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(AddAddressActivity.this,
                        getString(R.string.save_process), getString(R.string.please_wait), true);
                progressDialog.setCancelable(true);
                final String title = tvTitle.getText().toString();
                final String country = tvCountry.getText().toString();
                final String town = tvTown.getText().toString();
                final String street = tvStreet.getText().toString();
                final String phone1 = tvPhoneNumber1.getText().toString();
                final String phone2 = tvPhoneNumber2.getText().toString();

                if (title.isEmpty() || phone1.isEmpty()) {
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
                        final Snackbar errorSnackBar = Snackbar.make(saveAddressLayout, getString(R.string.not_network), LENGTH_INDEFINITE);
                        errorSnackBar.setAction(getString(R.string.dismiss), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                errorSnackBar.dismiss();
                            }
                        });
                        errorSnackBar.setActionTextColor(R.color.bhBack);
                        errorSnackBar.show();
                    }else{
                        address = new Address();
                        address.setJob_id(job_id);
                        address.setCountry(country);
                        address.setTitle(title);
                        address.setTown(town);
                        address.setStreet(street);
                        address.setPhoneNumber1(phone1);
                        address.setPhoneNumber2(phone2);
                        address.setIs_active(true);
                        saveAddress(address);
                    }
                }
            }
        });

    }

    private void saveAddress(Address address){
        BeakHubService.addAddress(this, address);
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 201){
            /*Intent intent = new Intent(this, ProfileJobActivity.class);
            intent.putExtra("job_id", job_id);
            startActivity(intent);*/
            finish();
        }else if(value.getCODE() == 400){
            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void error(Throwable throwable) {
        progressDialog.cancel();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}
