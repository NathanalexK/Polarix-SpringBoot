package com.example.demo.service.util;

import com.example.demo.dto.notif.NotificationDTO;
import com.example.demo.model.misc.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(final SimpMessagingTemplate simpMessagingTemplate){
        this.messagingTemplate = simpMessagingTemplate;
    }

    public void send(String destination, Object objectToSend){
        messagingTemplate.convertAndSend(destination, objectToSend);
    }

    public void sendNotification(String destination, Notification notification){
        this.send(destination, new NotificationDTO(notification));
    }

    public void sendNotificationToUser(String username, Notification notification) {
        this.sendNotification("/topic/user/" + username, notification);
    }
}
