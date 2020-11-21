package com.lechatong.beakhub.Tools;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {

    @SerializedName("MESSAGE")
    @Expose
    private String mESSAGE;
    @SerializedName("DATA")
    @Expose
    private Object dATA = null;
    @SerializedName("CODE")
    @Expose
    private Long cODE;

    public APIResponse() {
    }

    public APIResponse(String mESSAGE, Object dATA, Long cODE) {
        this.mESSAGE = mESSAGE;
        this.dATA = dATA;
        this.cODE = cODE;
    }

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public Object getDATA() {
        return dATA;
    }

    public void setDATA(Object dATA) {
        this.dATA = dATA;
    }

    public Long getCODE() {
        return cODE;
    }

    public void setCODE(Long cODE) {
        this.cODE = cODE;
    }

}
