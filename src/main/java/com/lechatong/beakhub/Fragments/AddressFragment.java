package com.lechatong.beakhub.Fragments;
/**
 * Author: LeChatong
 * Desc: Fragment show all address on a job
 */

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lechatong.beakhub.Activities.AddAddressActivity;
import com.lechatong.beakhub.Activities.ProfileJobActivity;
import com.lechatong.beakhub.Adapter.AddressAdapter;
import com.lechatong.beakhub.Models.BhAddress;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.ItemClickSupport;
import com.lechatong.beakhub.Tools.Streams.AddressStreams;
import com.lechatong.beakhub.Tools.Streams.JobStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.Context.MODE_PRIVATE;

public class AddressFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton btn_add_address;

    private List<BhAddress> addressList;

    private AddressAdapter addressAdapter;

    private Disposable disposable;

    private Disposable disposableJob;

    Intent intent;

    Context context;

    private Long job_id;

    private BhJob job;

    private Long is_author, account_id;

    private static final String ID_JOB = "JOB_ID";

    private static final String IS_AUTHOR = "IS_AUTHOR";

    private static final String ID_ACCOUNT = "ID_ACCOUNT";

    private static final String PREFS = "PREFS";

    public AddressFragment(){};

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ProfileJobActivity activity = (ProfileJobActivity) getActivity();

        assert activity != null;
        job_id = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(ID_JOB,0);
        is_author = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(IS_AUTHOR,0);
        account_id = activity.getSharedPreferences(PREFS, MODE_PRIVATE).getLong(ID_ACCOUNT, 0);

        View view = inflater.inflate(R.layout.fragment_address, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_address);

        btn_add_address = view.findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("job_id", job_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        this.loadRecyclerView();

        this.configureRecyclerView();

        this.configureOnClickRecyclerView();

        this.loadJob();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    private void loadRecyclerView(){
        this.disposable = AddressStreams.streamAddressByJobId(job_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse apiResponse) {
                        updateListAddress(apiResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void configureRecyclerView(){
        this.addressList = new ArrayList<>();

        this.addressAdapter = new AddressAdapter(this.addressList);

        this.recyclerView.setAdapter(addressAdapter);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void updateListAddress(APIResponse value){
        List<BhAddress> bhAddresses = new ArrayList<BhAddress>();
        ArrayList<BhAddress> bhAddressArrayList = (ArrayList<BhAddress>) value.getDATA();
        for(Object treeMap : bhAddressArrayList){
            bhAddresses.add(Deserializer.getAddress(treeMap));
        }
        addressList.addAll(bhAddresses);
        addressAdapter.notifyDataSetChanged();
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
        if (this.disposableJob != null && !this.disposableJob.isDisposed()) this.disposableJob.dispose();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.card_address)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        /**/
                    }
                })
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                        final CharSequence[] menus = {
                                getString(R.string.edit_address) ,
                                getString(R.string.show_address),
                                getString(R.string.cancel)
                        };

                        new AlertDialog.Builder(context)
                                .setItems(menus, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (menus[i].equals(getString(R.string.cancel))){
                                            dialogInterface.dismiss();
                                        }else if (menus[i].equals(getString(R.string.edit_address))){

                                        }else if (menus[i].equals(getString(R.string.show_address))){

                                        }
                                    }
                                })
                                .setTitle(R.string.menu)
                                .show();
                        return false;
                    }
                });
    }

    private void loadJob(){
        this.disposableJob = JobStreams.streamOneJob(job_id)
                .subscribeWith(new DisposableObserver<APIResponse>(){

                    @Override
                    public void onNext(APIResponse response) {
                        if(response.getCODE() == 200)
                            initJob(Deserializer.getJob(response.getDATA()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initJob(BhJob bhJob){
        job = bhJob;
        if(Objects.equals(job.getUserId(), account_id)){
            btn_add_address.setVisibility(View.VISIBLE);
        }else{
            btn_add_address.setVisibility(View.INVISIBLE);
        }
    }
}
