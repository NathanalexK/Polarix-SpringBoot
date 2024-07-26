package com.example.demo.dto.user;

import lombok.Data;

@Data
public class UserPasswordDTO {
    public String currentPassword;
    public String newPassword;

    @Override
    public String toString() {
        return "UserPasswordDTO{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
