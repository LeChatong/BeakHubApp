package com.lechatong.beakhub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.CircleTransform;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ServiceCallback;
import com.lechatong.beakhub.Tools.Streams.UserStream;
import com.lechatong.beakhub.WebService.BeakHubService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfilePictureActivity extends AppCompatActivity implements View.OnClickListener, ServiceCallback<APIResponse> {

    private static final String BASE_URL = "https://lechatonguniverse.herokuapp.com/fr/beakhub/api/";

    private BhUser user;

    Intent intent;

    Context context;

    private String responseJSON;

    private Long account_id;

    private ImageView imgProfile;

    private Button Upload_Btn;

    private Disposable disposable;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        requestStoragePermission();

        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account_id = extras.getLong("account_id");
        }

        imgProfile=(ImageView)findViewById(R.id.ivProfilePic);
        Upload_Btn=(Button)findViewById(R.id.UploadBtn);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        this.loadUser();

        Upload_Btn.setOnClickListener(this);
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposeWhenDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePictureActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    showFileChooser();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(ProfilePictureActivity.this,
                getString(R.string.save_process), getString(R.string.please_wait), true);
        File file = new File(getPath(filePath));

        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ivProfilePic);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multiPartBodyImage = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestBody);
        RequestBody account = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(account_id));
        RequestBody first_name = RequestBody.create(MediaType.parse("text/plain"), user.getFirst_name());
        RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), user.getLast_name());
        RequestBody phone_number = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());

        BeakHubService.editUserPicById(this, account_id, multiPartBodyImage, account, first_name, last_name, phone_number);
    }

    @Override
    public void success(APIResponse value) {
        if(value.getCODE() == 400){
            Toast.makeText(context, value.getMESSAGE(), Toast.LENGTH_LONG).show();
        }else if(value.getCODE() == 200){
            intent = new Intent(this, ProfileUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("account_id", account_id);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void error(Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void getUser(BhUser bhUser){
        user = bhUser;
        if(!user.getUrl_picture().isEmpty()){
            Picasso.with(context)
                    .load(user.getUrl_picture())
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

    private void loadUser(){
        this.disposable = UserStream.streamOneUserById(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse resp) {
                        getUser(Deserializer.getUser(resp.getDATA()));
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
