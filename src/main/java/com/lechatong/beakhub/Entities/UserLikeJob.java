package com.lechatong.beakhub.Entities;

/**
 * Author : LeChatong
 */

public class UserLikeJob {
    private Long id;
    private Long job_id;
    private Long user_id;
    private Boolean is_like;

    public UserLikeJob(Long id, Long job_id, Long user_id, Boolean is_like) {
        this.id = id;
        this.job_id = job_id;
        this.user_id = user_id;
        this.is_like = is_like;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Boolean getIs_like() {
        return is_like;
    }

    public void setIs_like(Boolean is_like) {
        this.is_like = is_like;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", job_id=" + job_id +
                ", user_id=" + user_id +
                ", is_like=" + is_like +
                '}';
    }

}
