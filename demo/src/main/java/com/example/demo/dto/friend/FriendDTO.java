package com.example.demo.dto.friend;

import com.example.demo.model.user.Friend;
import lombok.Data;

@Data
public class FriendDTO {
    private String sender;
    private String receiver;
    private String dateSend;
    private String dateConfirm;

    public FriendDTO(){
    }

    public FriendDTO(Friend friend){
        this.setSender(friend.getSender().getUsername());
        this.setReceiver(friend.getReceiver().getUsername());
        this.setDateSend(String.valueOf(friend.getDateSend()));
        this.setDateConfirm(String.valueOf(friend.getDateConfirm()));
    }
}
