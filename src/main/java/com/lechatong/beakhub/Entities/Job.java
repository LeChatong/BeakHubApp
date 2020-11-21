package com.lechatong.beakhub.Entities;

public class Job {
    private String title;
    private String description;
    private Boolean is_active;
    private Long user_id;
    private Long category_id;

    public Job() {
    }

    public Job(String title, String description, Boolean is_active, Long user_id, Long category_id) {
        this.title = title;
        this.description = description;
        this.is_active = is_active;
        this.user_id = user_id;
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Long getUser() {
        return user_id;
    }

    public void setUser(Long user) {
        this.user_id = user;
    }

    public Long getCategory() {
        return category_id;
    }

    public void setCategory(Long category) {
        this.category_id = category;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", is_active=" + is_active +
                ", user_id=" + user_id +
                ", category_id=" + category_id +
                '}';
    }
}
