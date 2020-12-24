package com.lechatong.beakhub.Tools.Streams;

/**
 * Author : LeChatong
 * Desc: This class provides a method who allow to have a user according to parameters
 */

import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserStreams {

    public static Observable<APIResponse> streamOneUserById(Long idUser){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getOneUserById(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
