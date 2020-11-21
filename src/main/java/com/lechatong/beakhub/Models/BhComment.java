package com.lechatong.beakhub.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BhComment {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("commentary")
    @Expose
    private String commentary;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("user_id")
    @Expose
    private Long user_id;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("job_id")
    @Expose
    private Long job_id;
    @SerializedName("is_active")
    @Expose
    private Boolean is_active;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public BhComment(){};

    public BhComment(Long id, String commentary, String user,
                     Long user_id, String job, Long job_id, Boolean is_active, String created_at, String updated_at) {
        this.id = id;
        this.commentary = commentary;
        this.user = user;
        this.user_id = user_id;
        this.job = job;
        this.job_id = job_id;
        this.is_active = is_active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
