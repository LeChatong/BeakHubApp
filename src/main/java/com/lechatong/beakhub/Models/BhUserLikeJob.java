package com.lechatong.beakhub.Models;

/**
 * Author : LeChatong
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BhUserLikeJob {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("is_like")
    @Expose
    private Boolean isLike;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("job_id")
    @Expose
    private Long jobId;
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

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
