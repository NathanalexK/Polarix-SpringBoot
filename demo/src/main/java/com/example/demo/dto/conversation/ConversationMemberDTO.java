package com.example.demo.dto.conversation;

import lombok.Data;

@Data
public class ConversationMemberDTO {
    private String username;
    private String name;
    private String userPicture;
    private Boolean isTyping;
}
