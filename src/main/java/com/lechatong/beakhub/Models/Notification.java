package com.lechatong.beakhub.Models;

public class Notification {

    private Long idEvent;

    private Long idJob;

    private Long idSender;

    private Long idReciever;

    private Boolean isView;

    private String message;

    private String time;

    private String url_picture_sender;

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public Long getIdJob() {
        return idJob;
    }

    public void setIdJob(Long idJob) {
        this.idJob = idJob;
    }

    public Long getIdSender() {
        return idSender;
    }

    public void setIdSender(Long idSender) {
        this.idSender = idSender;
    }

    public Long getIdReciever() {
        return idReciever;
    }

    public void setIdReciever(Long idReciever) {
        this.idReciever = idReciever;
    }

    public Boolean getView() {
        return isView;
    }

    public void setView(Boolean view) {
        isView = view;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl_picture_sender() {
        return url_picture_sender;
    }

    public void setUrl_picture_sender(String url_picture_sender) {
        this.url_picture_sender = url_picture_sender;
    }
}
