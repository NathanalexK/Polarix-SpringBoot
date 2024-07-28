package com.example.demo.dto.notif;

import com.example.demo.model.misc.Notification;
import lombok.Data;

@Data
public class NotificationDTO {
    private String type;
    private String title;
    private String content;

    public NotificationDTO(){
    }

    public NotificationDTO(Notification notification){
        this.setType(notification.getType().getNotificationType());
        this.setType(notification.getTitle());
        this.setContent(notification.getContent());
    }
}
