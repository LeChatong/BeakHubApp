package com.lechatong.beakhub.Tools.Streams;

/**
 * Author: LeChatong
 * Desc: This class provides a method who allow to have a jobs according to parameters
 */

import com.lechatong.beakhub.Entities.Job;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.WebService.IBeakHubService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JobStreams {

    /**
     * Get the Job by a User
     * @param idUser
     * @return Observable<APIResponse>
     */
    public static Observable<APIResponse> streamJobByUserId(Long idUser){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getJobByIdUser(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    /**
     * Desc: Get Job by Category
     * @param idCategory
     * @return
     */
    public static Observable<APIResponse> streamJobByCategoryId(Long idCategory){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getJobByIdCategory(idCategory).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    /**
     * Get one Job
     * @param idJob
     * @return Observable<APIResponse>
     */
    public static Observable<APIResponse> streamOneJob(Long idJob){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getOneJob(idJob).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    /**
     * Get the Jobs with a research (the research is made on the attribute 'title' for the time being
     * @param search
     * @return Observable<APIResponse>
     */
    public static Observable<APIResponse> streamJobSearch(String search){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getJobSearch(search).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamEditJob(Long job_id, Job job){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.editJob(job_id, job).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamJobsMostSollicited(){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getJobsMostSollicited().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }

    public static Observable<APIResponse> streamJobsFavByUser(Long user_id){
        IBeakHubService iBeakHubService = IBeakHubService.retrofit.create(IBeakHubService.class);

        return iBeakHubService.getJobsFavByUser(user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(20, TimeUnit.SECONDS);
    }
}
