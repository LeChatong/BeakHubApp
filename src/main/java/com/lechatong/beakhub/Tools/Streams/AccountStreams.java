package com.lechatong.beakhub.Tools.Streams;

import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: LeChatong
 * Desc: This class provides a method who allow to have a account according to parameters
 */

public class AccountStreams {

    public static Observable<APIResponse> streamOneUserById(Long idUser){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getAccount(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamUpdateAccount(Long idAccount, BhAccount bhAccount){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.updateAccount(bhAccount, idAccount).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
