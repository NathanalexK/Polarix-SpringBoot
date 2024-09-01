package com.example.demo.dto.user;

import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.FriendType;
import lombok.Data;

@Data
public class UserSimpleDetailsDTO {
    String name;
    String username;
    String userPicture;
    String friendType;

    public UserSimpleDetailsDTO(AppUser user, String friendType) {
        this.setName(user.getName());
        this.setUsername(user.getUsername());
        this.setUserPicture(user.getPicture());
        this.setFriendType(FriendType.valueOf(friendType).name());
    }

    public UserSimpleDetailsDTO(AppUser user) {
        this.setName(user.getName());
        this.setUsername(user.getUsername());
        this.setUserPicture(user.getPicture());
        this.setFriendType(FriendType.FRIEND.name());
    }


}
