package com.lechatong.beakhub.Models;

/**
 * Author: LeChatong
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BhEvent {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("reciever_id")
    @Expose
    private Long recieverId;
    @SerializedName("sender_id")
    @Expose
    private Long senderId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("is_view")
    @Expose
    private Boolean isView;
    @SerializedName("job_id")
    @Expose
    private Long job_id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(Long recieverId) {
        this.recieverId = recieverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getIsView() {
        return isView;
    }

    public void setIsView(Boolean isView) {
        this.isView = isView;
    }

    public Long getJobId() {
        return job_id;
    }

    public void setJobId(Long job_id) {
        this.job_id = job_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
