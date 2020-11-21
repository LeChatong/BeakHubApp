package com.lechatong.beakhub.Models;

import java.io.Serializable;

public class Chat<T> implements Serializable {

    private String mAvatar;
    private T mData;
    private int mType;


    public Chat(int type) {
        this.mType = type;
    }

    public Chat(T data, int type) {
        this.mData = data;
        this.mType = type;
    }

    public Chat(String avatar, T data, int type) {
        this.mAvatar = avatar;
        this.mData = data;
        this.mType = type;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
    }

    public
    @ChatType
    int getType() {
        return this.mType;
    }
}
