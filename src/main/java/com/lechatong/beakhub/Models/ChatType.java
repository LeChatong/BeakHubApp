package com.lechatong.beakhub.Models;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ChatType.SEND, ChatType.RECEIVE, ChatType.UNDEFINED})

public @interface ChatType {
    int UNDEFINED = -1;
    int SEND = 0;
    int RECEIVE = 1;
}
