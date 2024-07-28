package com.example.demo.dto.friend;

import com.example.demo.model.user.Friend;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FriendRequestListDTO {
    private List<FriendRequestRowDTO> list;

    public FriendRequestListDTO(){
        this.list = new ArrayList<>();
    }

    public FriendRequestListDTO(List<Friend> friends){
        this();
        for(Friend friend : friends){
            this.list.add(new FriendRequestRowDTO(friend));
        }
    }
}
