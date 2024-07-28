package com.example.demo.dto.friend;

import com.example.demo.model.user.Friend;
import lombok.Data;

@Data
public class FriendRequestRowDTO {
    private String username;
    private String picture;
    private String dateSend;

    public FriendRequestRowDTO(){
    }

    public FriendRequestRowDTO(Friend friend) {
        this.setUsername(friend.getSender().getUsername());
        this.setPicture(friend.getSender().getPicture());
        this.setDateSend(String.valueOf(friend.getDateSend()));
    }
}
