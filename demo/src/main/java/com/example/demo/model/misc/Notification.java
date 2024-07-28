package com.example.demo.model.misc;

import lombok.Data;

@Data
public class Notification {
    private NotificationType type;
    private String title;
    private String content;

    public Notification() {
    }

    public Notification(NotificationType type, String title, String content) {
        this.setType(type);
        this.setTitle(title);
        this.setContent(content);
    }

    public Notification(String notificationType, String title, String content){
        this.setType(NotificationType.valueOf(notificationType.toUpperCase()));
        this.setTitle(title);
        this.setContent(content);
    }

    public static Notification success(String title, String content){
        return new Notification(NotificationType.SUCCESS, title, content);
    }

    public static Notification info(String title, String content){
        return new Notification(NotificationType.INFO, title, content);
    }

    public static Notification warning(String title, String content){
        return new Notification(NotificationType.WARNING, title, content);
    }

    public static Notification danger(String title, String content){
        return new Notification(NotificationType.DANGER, title, content);
    }
}
