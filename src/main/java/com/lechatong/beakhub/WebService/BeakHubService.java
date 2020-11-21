package com.lechatong.beakhub.WebService;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lechatong.beakhub.Entities.Address;
import com.lechatong.beakhub.Entities.Comment;
import com.lechatong.beakhub.Entities.Job;
import com.lechatong.beakhub.Entities.User;
import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhAddress;
import com.lechatong.beakhub.Models.BhCategory;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.ServiceCallback;

import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeakHubService {

    public static void getAccountConnection(ServiceCallback<APIResponse> callbacks, String username, String password){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.getAccountByUsernameAndPassword(username, password);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void addAccount(ServiceCallback<APIResponse> callback, BhAccount bhAccount){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callback);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.createAccount(bhAccount);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void getUserById(ServiceCallback<APIResponse> callbacks, Long id){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.getUserById(id);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().error(t);
                }
                Log.e("TAG", t.getMessage());
            }
        });
    }

    public static void addUser(ServiceCallback<APIResponse> callback, User bhUser){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callback);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService. createUser(bhUser);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void editUserById(ServiceCallback<APIResponse> callbacks, Long id, User bhUser){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.updateUser(id, bhUser);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().error(t);
                }
                Log.e("TAG", t.getMessage());
            }
        });
    }

    public static void editUserPicById(ServiceCallback<APIResponse> callback, Long account_id, MultipartBody.Part multiPartBodyImage, RequestBody account,
                                       RequestBody first_name, RequestBody last_name,
                                       RequestBody phone_number){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callback);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.uploadFile(account_id, multiPartBodyImage, account, first_name, last_name, phone_number);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());

                Log.e("TAG", response.body().toString());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null){
                    callbacksWeakReference.get().error(t);
                }
                Log.e("TAG", t.getMessage());
            }
        });
    }

    public static void addJob(ServiceCallback<APIResponse> callbacks, Job job){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.createJob(job);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });

    }

    public static void addAddress(ServiceCallback<APIResponse> callbacks, Address address){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.createAddress(address);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void getJobById(ServiceCallback<APIResponse> callbacks, Long id){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.getJobById(id);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void getAddressById(ServiceCallback<APIResponse> callbacks, Long id){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.getAddressById(id);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

    public static void addCommentary(ServiceCallback<APIResponse> callbacks, Comment comment){
        final WeakReference<ServiceCallback<APIResponse>> callbacksWeakReference = new WeakReference<ServiceCallback<APIResponse>>(callbacks);

        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        Call<APIResponse> call = iBeakHubService.addComment(comment);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().success(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                if(callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().error(t);
            }
        });
    }

}
