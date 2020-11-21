package com.lechatong.beakhub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lechatong.beakhub.Models.BhAccount;
import com.lechatong.beakhub.Models.BhComment;
import com.lechatong.beakhub.Models.Chat;
import com.lechatong.beakhub.Models.ChatType;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.ChatVH;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatVH> {

    private Context mContext;

    private List<Chat<String>> mList = null;

    private List<BhComment> bhComments;

    private Long account_id;


    public ChatAdapter(List<BhComment> bhComments, Long account_id) {
        this.bhComments = bhComments;
        this.account_id = account_id;
        this.mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View itemLayoutView = null;

        switch (viewType) {
            case ChatType.RECEIVE:
                itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_recierver, parent, false);
                break;
            case ChatType.SEND:
                itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_sender, parent, false);
                break;
            case ChatType.UNDEFINED:
                break;
        }

        itemLayoutView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ChatVH(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        holder.bind(bhComments.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (this.bhComments == null)
            return ChatType.UNDEFINED;
        if(account_id.equals(this.bhComments.get(position).getUser_id())){
            return ChatType.SEND;
        }else {
            return ChatType.RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        if (this.bhComments == null)
            return 0;
        return this.bhComments.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void add(BhComment bhComment) {
        if (this.bhComments == null)
            this.bhComments = new ArrayList<>();
        this.bhComments.add(bhComment);
        notifyItemInserted(this.bhComments.size() - 1);
    }
}
