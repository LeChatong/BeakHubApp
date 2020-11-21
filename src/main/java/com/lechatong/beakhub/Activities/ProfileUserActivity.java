package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.CircleTransform;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.WebService.BeakHubService;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ProfileUserActivity extends AppCompatActivity implements ServiceCallback<APIResponse> {

    private Long account_id;

    private com.google.android.material.floatingactionbutton.FloatingActionButton btnEditUser;

    Intent intent;

    Context context;

    private TextView tv_email_user, tv_last_name_user, tv_first_name_user, tv_phone_number, tv_username, tv_last_connection;

    private ImageView imgProfile;

    private Toolbar toolbar;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        toolbar = findViewById(R.id.toolbar_pro_user);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        account_id = sharedPreferences.getLong(ID_ACCOUNT, 0);

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account_id = extras.getLong("account_id");
        }*/

        tv_email_user = (TextView) findViewById(R.id.tv_email_user);
        tv_last_name_user = (TextView) findViewById(R.id.tv_last_name_user);
        tv_first_name_user = (TextView) findViewById(R.id.tv_first_name_user);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_username= (TextView) findViewById(R.id.tv_username);
        //tv_last_connection = (TextView) findViewById(R.id.tv_last_connection);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, ProfilePictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("account_id", account_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnEditUser = (com.google.android.material.floatingactionbutton.FloatingActionButton)findViewById(R.id.btn_edit_user);

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, EditUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("account_id", account_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        BeakHubService.getUserById(this, account_id);
    }

    private void initUser(BhUser bhUser){
        tv_email_user.setText(bhUser.getEmail());
        tv_last_name_user.setText(bhUser.getLast_name());
        tv_first_name_user.setText(bhUser.getFirst_name());
        tv_phone_number.setText(bhUser.getPhone_number());
        //tv_username.setText(bhUser.getAccount().getUsername());
        //tv_last_connection.setText((new SimpleDateFormat("dd/mm/yyyy hh:mm", Locale.CANADA_FRENCH)).format(bhUser.getAccount().getLastLogin()));
        if (!bhUser.getUrl_picture().isEmpty()){
            Picasso.with(context)
                    .load(bhUser.getUrl_picture())
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

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 200){
            BhUser user = Deserializer.getUser(value.getDATA());
            this.initUser(user);
        }

    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("account_id", account_id);
        setResult(RESULT_OK, intent);
        finish();
    }

}
