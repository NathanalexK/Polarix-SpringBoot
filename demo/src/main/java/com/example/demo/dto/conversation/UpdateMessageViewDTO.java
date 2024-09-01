package com.example.demo.dto.conversation;

import com.example.demo.model.message.Message;
import lombok.Data;

@Data
public class UpdateMessageViewDTO {
    private MessageDTO oldMessage;
    private MessageDTO newMessage;

    public UpdateMessageViewDTO(Message oldMessage, Message newMessage, Integer idCurrentUser) {
        this.setOldMessage(new MessageDTO(oldMessage, idCurrentUser));
        this.setNewMessage(new MessageDTO(newMessage, idCurrentUser));
    }
}
