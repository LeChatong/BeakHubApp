package com.lechatong.beakhub.Tools.Streams;

import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: LeChatong
 * Desc: This class provides a method who allow to have a notifications according to parameters
 */

public class NotificationStreams {

    /**
     *
     * @param idUser
     * @return Observable<APIResponse>
     */
    public static Observable<APIResponse> streamNotificationByUserId(Long idUser){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getAllEventsByUser(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamNotificationNotSeeByUserId(Long idUser){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getAllEventsNotSeeByUser(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamNotificationById(Long id){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getOneEvent(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamNotificationTrue(Long id){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.setTrueEvent(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
