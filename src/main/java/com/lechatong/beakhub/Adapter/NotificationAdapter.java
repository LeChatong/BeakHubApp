package com.lechatong.beakhub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lechatong.beakhub.Activities.HomeActivity;
import com.lechatong.beakhub.Models.BhEvent;
import com.lechatong.beakhub.Models.BhJob;
import com.lechatong.beakhub.Models.BhUser;
import com.lechatong.beakhub.Models.Notification;
import com.lechatong.beakhub.R;
import com.lechatong.beakhub.Tools.APIResponse;
import com.lechatong.beakhub.Tools.CircleTransform;
import com.lechatong.beakhub.Tools.Deserializer;
import com.lechatong.beakhub.Tools.Streams.JobStreams;
import com.lechatong.beakhub.Tools.Streams.UserStreams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: LeChatong
 * Desc: This Adapter format the Notification to show in NotificationFragment
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;

    private List<BhEvent> bhEvents;

    public NotificationAdapter(List<BhEvent> bhEventList){ this.bhEvents = bhEventList; }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View v = LayoutInflater.from(context).inflate(R.layout.card_notification, parent, false);
        return new NotificationAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BhEvent bhEvent = this.bhEvents.get(position);
        if(!bhEvent.getIsView())
            holder.tvNotifText.setTypeface(null, Typeface.BOLD);

        if(bhEvent.getAction().equals("LIKE")) {
            holder.tvNotifText.setText(HtmlCompat.fromHtml("<b>" + bhEvent.getName_sender() + "</b>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY) + " " +
                    context.getString(R.string.notif_message_1) + " " + bhEvent.getJob_title());
        } else if(bhEvent.getAction().equals("COMMENT")){
            holder.tvNotifText.setText(bhEvent.getName_sender() + " " +
                    context.getString(R.string.notif_message_2) + " " + bhEvent.getJob_title());
        }

        holder.tvTime.setText(bhEvent.getHow_hours()+ " h");


        if (!bhEvent.getUrl_picture_sender().isEmpty()){
            Picasso.with(context)
                    .load(bhEvent.getUrl_picture_sender())
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(holder.circularImageView);
        }else{
            Picasso.with(context)
                    .load("https://lechatonguniverse.herokuapp.com/media/photo_user/lechatong.jpg")
                    .centerCrop()
                    .transform(new CircleTransform(50,0))
                    .fit()
                    .into(holder.circularImageView);
        }

        /*holder.tvNotifText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return this.bhEvents.size();
    }

    public BhEvent getEvent(int position){
        return this.bhEvents.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView circularImageView;
        TextView tvNotifText;
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImageView = itemView.findViewById(R.id.ivNotifPP);
            tvNotifText = itemView.findViewById(R.id.tvNotifText);
            tvTime = itemView.findViewById(R.id.tvDuring);
        }
    }
}
