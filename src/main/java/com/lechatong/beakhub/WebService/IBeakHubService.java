package com.lechatong.beakhub.WebService;

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

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBeakHubService {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lechatonguniverse.herokuapp.com/fr/beakhub/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


    @GET("accounts/login")
    Call<APIResponse> getAccountByUsernameAndPassword(@Query("username") String username, @Query("password") String password);

    @POST("accounts")
    Call<APIResponse> createAccount(@Body BhAccount bhAccount);

    @PUT("accounts/{id}")
    Call<APIResponse> updateAccount(@Body BhAccount bhAccount);

    @POST("users")
    Call<APIResponse> createUser(@Body User bhUser);

    @GET("users/{id}")
    Call<APIResponse> getUserById(@Path("id") Long id);

    @GET("users/{id}")
    Observable<APIResponse> getOneUserById(@Path("id") Long id);

    @PUT("users/{id}")
    Call<APIResponse> updateUser(@Path("id") Long id, @Body User bhUser);

    @Multipart
    @PUT("users/{id}")
    Call<APIResponse>  uploadFile(@Path("id") Long id, @Part MultipartBody.Part file, @Part("account")RequestBody account
            , @Part("first_name")RequestBody first_name, @Part("last_name")RequestBody last_name
            , @Part("phone_number")RequestBody phone_number);


    @DELETE("users/{id}")
    Call<APIResponse> deleteUser(@Path("id") Integer id);

    @POST("jobs")
    Call<APIResponse> createJob(@Body Job job);

    @PUT("jobs/{job_id}")
    Call<APIResponse> updateJob(@Path("job_id") Long job_id, @Body Job job);

    @GET("categories/{id}")
    Call<BhCategory> getCategoryById(@Path("id") int id);

    @GET("jobs/user/{id_user}")
    Observable<APIResponse> getJobByIdUser(@Path("id_user") Long id_user);

    @GET("jobs/search")
    Observable<APIResponse> getJobSearch(@Query("query") String search);

    @GET("categories")
    Observable<List<BhCategory>> getAllCategory();

    @GET("jobs/{job_id}")
    Call<APIResponse> getJobById(@Path("job_id") Long job_id);

    @GET("address/job/{id_job}")
    Observable<APIResponse> getAddressByIdJob(@Path("id_job") Long id_job);

    @GET("address/{id_address}")
    Call<APIResponse> getAddressById(@Path("id_address") Long id_address);

    @POST("address")
    Call<APIResponse> createAddress(@Body Address address);

    @PUT("address/{id_address}")
    Call<APIResponse> updateAddress(@Path("id_address") Long id_address, @Body Address address);

    @POST("comments")
    Call<APIResponse> addComment(@Body Comment comment);

    @GET("comments/job/{id_job}")
    Observable<APIResponse> getAllCommentByJob(@Path("id_job") Long id_job);

}
