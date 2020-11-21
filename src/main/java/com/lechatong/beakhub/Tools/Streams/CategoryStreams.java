package com.lechatong.beakhub.Tools.Streams;

import com.lechatong.beakhub.Models.BhCategory;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoryStreams {

    public static Observable<List<BhCategory>> streamAllCategory(){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getAllCategory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
