package com.example.demo.model.misc;

public enum NotificationType {
    SUCCESS, INFO, WARNING, DANGER;

    public String getNotificationType(){
        return name();
    }
}
