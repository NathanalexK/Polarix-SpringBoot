package com.example.demo.dto.user;

import com.example.demo.model.user.AppUser;
import lombok.Data;

@Data
public class UserDetailsDTO {
    private Integer id;
    private String username;
    private String email;
    private String biography;
    private String picture;

    public UserDetailsDTO(){
    }

    public UserDetailsDTO(AppUser user){
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setBiography(user.getBiography());
        this.setPicture(user.getPicture());
    }
}
