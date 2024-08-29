package com.example.demo.dto.conversation;

import com.example.demo.model.message.Message;
import lombok.Data;

@Data
public class MessageDTO {
    private Integer id;
    private String username;
    private String name;
    private String userPicture;
    private String content;
    private String createdAt;
    private Boolean isCurrentUser;

    public MessageDTO(){
    }

    public MessageDTO(Message message, Integer idCurrentUser) {
        this.setId(message.getId());
        this.setUsername(message.getSender().getUsername());
        this.setName(message.getSender().getName());
        this.setUserPicture(message.getSender().getPicture());
        this.setContent(message.getContent());
        this.setCreatedAt(String.valueOf(message.getCreatedAt()));
        this.setIsCurrentUser(message.getSender().getId() == idCurrentUser);
    }
}
