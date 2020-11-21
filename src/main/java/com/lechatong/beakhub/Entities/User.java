package com.lechatong.beakhub.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    @SerializedName("account")
    @Expose
    private Long account;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("whatsapp_phone")
    @Expose
    private String whatsapp_phone;

    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    @SerializedName("profile_picture")
    @Expose
    private File profile_picture;

    @SerializedName("date_of_birth")
    @Expose
    private Date date_of_birth;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    private Date updatdedAt;

    public User() {
    }

    public User(Long account, String first_name, String last_name, String email, String whatsapp_phone, String phone_number,
                  File profile_picture, Date date_of_birth, Date createdAt, Date updatdedAt) {
        this.account = account;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.whatsapp_phone = whatsapp_phone;
        this.phone_number = phone_number;
        this.profile_picture = profile_picture;
        this.date_of_birth = date_of_birth;
        this.createdAt = createdAt;
        this.updatdedAt = updatdedAt;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp_phone() {
        return whatsapp_phone;
    }

    public void setWhatsapp_phone(String whatsapp_phone) {
        this.whatsapp_phone = whatsapp_phone;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatdedAt() {
        return updatdedAt;
    }

    public void setUpdatdedAt(Date updatdedAt) {
        this.updatdedAt = updatdedAt;
    }

    public void setProfile_picture(File profile_picture) {
        this.profile_picture = profile_picture;
    }
}
