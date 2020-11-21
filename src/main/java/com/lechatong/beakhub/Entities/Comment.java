package com.lechatong.beakhub.Entities;

public class Comment {
    private Long id;
    private String commentary;
    private Long job_id;
    private Long user_id;

    public Comment(){};

    public Comment(Long id, String commentary, Long job_id, Long user_id) {
        this.id = id;
        this.commentary = commentary;
        this.job_id = job_id;
        this.user_id = user_id;
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
}
