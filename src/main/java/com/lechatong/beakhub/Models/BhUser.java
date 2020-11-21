package com.lechatong.beakhub.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lechatong.beakhub.Entities.Account;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class BhUser implements Serializable {

    @SerializedName("account_id")
    @Expose
    private Long accountId;

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

    @SerializedName("url_picture")
    @Expose
    private String url_picture;

    @SerializedName("date_of_birth")
    @Expose
    private Date date_of_birth;

    @SerializedName("account")
    @Expose
    private Account account;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    private Date updatdedAt;

    public BhUser() {
    }

    public BhUser(Long accountId, String first_name, String last_name, String email, String whatsapp_phone, String phone_number,
                  String url_picture, Date date_of_birth, Date createdAt, Date updatdedAt) {
        this.accountId = accountId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.whatsapp_phone = whatsapp_phone;
        this.phone_number = phone_number;
        this.url_picture = url_picture;
        this.date_of_birth = date_of_birth;
        this.createdAt = createdAt;
        this.updatdedAt = updatdedAt;
    }

    public Long getId() {
        return accountId;
    }

    public void setId(Long id) {
        this.accountId = id;
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

    public String getUrl_picture() {
        return url_picture;
    }

    public void setProfile_picture(String url_picture) {
        this.url_picture = url_picture;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + accountId +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", whatsapp_phone='" + whatsapp_phone + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", url_picture='" + url_picture + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", createdAt=" + createdAt +
                ", updatdedAt=" + updatdedAt +
                '}';
    }
}
