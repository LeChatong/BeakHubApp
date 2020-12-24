package com.lechatong.beakhub.Fragments;

/**
 * Author: LeChatong
 * Desc: This fragment show all notifications of the user connected
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lechatong.beakhub.Activities.HomeActivity;
import com.lechatong.beakhub.Adapter.NotificationAdapter;
import com.lechatong.beakhub.Models.BhEvent;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.Models.Notification;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ItemClickSupport;
import com.lechatong.beakhub.Tools.Streams.JobStreams;
import com.lechatong.beakhub.Tools.Streams.NotificationStreams;
import com.lechatong.beakhub.Tools.Streams.UserStreams;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;

    private NotificationAdapter notificationAdapter;

    private Intent intent;

    private Context context;

    private Long account_id;

    private List<BhEvent> bhEventList;

    private List<Notification> notificationList;

    private Disposable disposable;

    private Disposable disposableSender;

    private Disposable disposableJob;

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance (){return new NotificationFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeActivity activity = (HomeActivity) getActivity();
        assert activity != null;
        account_id = activity.getAccount_id();

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_notif);

        this.configureRecyclerView();

        this.loadRecyclerView();

        this.configureOnClickRecyclerView();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void configureRecyclerView(){
        this.bhEventList = new ArrayList<>();

        this.notificationAdapter = new NotificationAdapter(this.bhEventList);

        this.recyclerView.setAdapter(notificationAdapter);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void loadRecyclerView(){
        this.disposable = NotificationStreams.streamNotificationByUserId(account_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        initNotif(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initNotif(APIResponse value){
        List<BhEvent> bhEvents = new ArrayList<BhEvent>();
        ArrayList<BhEvent> bhEventArrayList = (ArrayList<BhEvent>) value.getDATA();

        for(Object treeMap : bhEventArrayList){
            bhEvents.add(Deserializer.getEvent(treeMap)) ;
        }

        bhEventList.addAll(bhEvents);
        notificationAdapter.notifyDataSetChanged();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
        if (this.disposableSender != null && !this.disposableSender.isDisposed()) this.disposableSender.dispose();
        if (this.disposableJob != null && !this.disposableJob.isDisposed()) this.disposableJob.dispose();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.card_notification)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        BhEvent bhEvent = notificationAdapter.getEvent(position);
                        HomeActivity activity = (HomeActivity) getActivity();
                        assert activity != null;

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", bhEvent.getId());
                        bundle.putLong("job_id", bhEvent.getJobId());
                        DetailNotificationFragment fragmentDetailsNotif = new DetailNotificationFragment();
                        fragmentDetailsNotif.setArguments(bundle);
                        activity.startTransactionFragment(fragmentDetailsNotif);

                    }
                });
    }
}
