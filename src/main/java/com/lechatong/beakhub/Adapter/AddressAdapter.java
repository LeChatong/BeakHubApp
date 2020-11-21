package com.lechatong.beakhub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lechatong.beakhub.Models.BhAddress;
import com.lechatong.beakhub.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;

    private List<BhAddress> bhAddresses;

    public AddressAdapter(List<BhAddress> bhAddressList) {
        this.bhAddresses = bhAddressList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        BhAddress bhAddress = this.bhAddresses.get(position);
        holder.tv_phone_addressr_1.setText(bhAddress.getPhoneNumber1());
        holder.tv_title.setText(bhAddress.getTitle());

    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(context).inflate(R.layout.card_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return this.bhAddresses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_phone_addressr_1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitleAddress);
            tv_phone_addressr_1 = itemView.findViewById(R.id.tvPhoneNumberAddress1);
        }
    }
}
