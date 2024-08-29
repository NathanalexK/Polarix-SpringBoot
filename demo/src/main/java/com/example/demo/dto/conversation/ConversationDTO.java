package com.example.demo.dto.conversation;

import com.example.demo.model.message.Conversation;
import com.example.demo.model.message.ConversationMember;
import com.example.demo.model.message.ConversationType;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ConversationDTO {
    private Integer id;
    private String name;
    private String picture;
    private String lastSenderName;
    private String lastSenderUsername;
    private String lastMessage;
    private String updatedAt;
    private Boolean isSeen;
    private String conversationType;

    public ConversationDTO() {
    }


    public ConversationDTO(ConversationMember member) {
        Conversation conversation = member.getConversation();
        this.setId(conversation.getId());
        List<ConversationMember> otherMembers = conversation.getConversationMembers();

        if(conversation.getConversationType() == ConversationType.ONE_TO_MANY){
            this.setName(conversation.getName() != null ? conversation.getName() : "No name");
            this.setPicture(null);

        } else {
            if(otherMembers.isEmpty()) throw new RuntimeException("No other member in ONE to ONE conversation!! " + otherMembers);

            ConversationMember otherMember = otherMembers.stream()
                .filter(om -> om.getUser().getId() != member.getUser().getId())
                .findFirst()
                .orElseThrow();

            this.setName(otherMember.getUser() != null ? otherMember.getUser().getName() : null);
            this.setPicture(otherMember.getUser() != null ? otherMember.getUser().getPicture() : null);
        }

        this.setLastSenderName(conversation.getLastUser() != null ? conversation.getLastUser().getName() : null);
        this.setLastSenderUsername(conversation.getLastUser() != null ? conversation.getLastUser().getName() : null);
        this.setUpdatedAt(String.valueOf(conversation.getUpdatedAt()));
        this.setLastMessage(
            conversation.getLastMessage() != null ?
            conversation.getLastMessage().getContent() :
            "Polaire: Say Hello!"
        );
        this.setIsSeen(
            member.getLastSeenMessage() != null ?
            member.getLastSeenMessage().getId().equals(conversation.getLastMessage().getId()):
            false
        );
        this.setConversationType(conversation.getConversationType().name());
    }
}
