package com.example.demo.dto.conversation;

import com.example.demo.dto.user.UserSimpleDetailsDTO;
import lombok.Data;

@Data
public class TypingMessageDTO {
//    private Integer idConversation;
    private UserSimpleDetailsDTO user;
    private Boolean isTyping;
}
