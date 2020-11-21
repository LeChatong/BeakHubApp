package com.lechatong.beakhub.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BhCategory {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    String title;

    public BhCategory() {
    }

    public BhCategory(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BhCategory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
