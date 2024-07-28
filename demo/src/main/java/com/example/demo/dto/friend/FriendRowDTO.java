package com.example.demo.dto.friend;

import com.example.demo.model.user.AppUser;
import lombok.Data;

@Data
public class FriendRowDTO {
    private String username;
    private String lastOnline;
    private String picture;

    public FriendRowDTO(){
    }

    public FriendRowDTO(AppUser user){
        this.setUsername(user.getUsername());
        this.setLastOnline(user.getLastOnline().toString());
        this.setPicture(user.getPicture());
    }
}
