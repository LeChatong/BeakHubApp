package com.lechatong.beakhub.Tools;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.lechatong.beakhub.Models.BhComment;
import com.lechatong.beakhub.Models.Chat;
import com.lechatong.beakhub.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChatVH extends RecyclerView.ViewHolder {

    TextView tvUserComment;

    TextView tvTime;

    TextView tvComment;

    AppCompatImageView imageView;

    public ChatVH(@NonNull View itemView) {
        super(itemView);

        tvUserComment = (TextView) itemView.findViewById(R.id.tvUserComment);
        tvComment = (TextView) itemView.findViewById(R.id.tvComment);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bind(BhComment bhComment) {
        tvUserComment.setText(bhComment.getUser());
        tvComment.setText(bhComment.getCommentary());
        tvTime.setText(bhComment.getCreated_at().toString());
    }
}
