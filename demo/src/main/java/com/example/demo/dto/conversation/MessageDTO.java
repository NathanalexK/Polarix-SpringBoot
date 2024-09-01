package com.example.demo.dto.conversation;

import com.example.demo.dto.user.UserSimpleDetailsDTO;
import com.example.demo.model.message.ConversationMember;
import com.example.demo.model.message.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class MessageDTO {
    private Integer id;
    private String username;
    private String name;
    private String userPicture;
    private String content;
    private String createdAt;
    private Boolean isCurrentUser;
    private List<UserSimpleDetailsDTO> users = new ArrayList<>();

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

//        System.out.println("===============================================");
//        message.getConversation().getConversationMembers().forEach(System.out::println);

        for (ConversationMember conversationMember : message.getConversation().getConversationMembers()){
            if(conversationMember.getLastSeenMessage() == null) continue;
//            System.out.println("*************************************************");
//            System.out.println(conversationMember.getLastSeenMessage().getId() + " == " + message.getId());

            if(Objects.equals(conversationMember.getLastSeenMessage().getId(), message.getId())){
                this.users.add(new UserSimpleDetailsDTO(conversationMember.getUser()));
            }
        }
    }
}
