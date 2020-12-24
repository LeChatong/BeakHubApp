package com.lechatong.beakhub.Tools.Streams;

/*
 * Author : LeChatong
 */

import com.lechatong.beakhub.Entities.UserLikeJob;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;

public class LikeStreams {

    public static Observable<APIResponse> streamOneLike(Long job_id, Long user_id){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getOneLikeByIdJobAndIdUser(job_id, user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamEditLike(Long like_id, UserLikeJob userLikeJob){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.editLike(like_id, userLikeJob).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamNewLike(UserLikeJob userLikeJob){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.newLike(userLikeJob).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
