package com.example.demo.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
@Data
public class UserLoginDTO {
    String username;
    String password;

    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
