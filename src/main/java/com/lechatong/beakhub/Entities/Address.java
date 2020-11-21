package com.lechatong.beakhub.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("phone_number_1")
    @Expose
    private String phoneNumber1;
    @SerializedName("phone_number_2")
    @Expose
    private String phoneNumber2;
    private Boolean is_active;
    private Long job_id;

    public Address(){};

    public Address(String title, String country, String town, String street,
                   String website, String phoneNumber1, String phoneNumber2, Boolean is_active, Long job_id) {
        this.title = title;
        this.country = country;
        this.town = town;
        this.street = street;
        this.website = website;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.is_active = is_active;
        this.job_id = job_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
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

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", website='" + website + '\'' +
                ", phone_number_1='" + phoneNumber1 + '\'' +
                ", phone_number_2='" + phoneNumber2 + '\'' +
                ", is_active=" + is_active +
                ", job_id=" + job_id +
                '}';
    }
}
