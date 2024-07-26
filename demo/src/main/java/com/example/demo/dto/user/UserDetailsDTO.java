package com.example.demo.dto.user;

import com.example.demo.model.user.AppUser;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailsDTO {
    private String username;
    private String email;
    private String biography;
    private String picture;
    private LocalDate birthdate;
    private String sex;

    public UserDetailsDTO(){
    }

    public UserDetailsDTO(AppUser user){
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setBiography(user.getBiography());
        this.setPicture(user.getPicture());
        this.setBirthdate(user.getBirthdate());
        this.setSex(user.getSex().name());
    }
}
