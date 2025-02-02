package com.example.demo.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "role", nullable = false)
    private AppUserRole role;

    @Column(name = "last_online")
    private LocalDateTime lastOnline;

    @Column(name = "biography")
    private String biography;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "picture")
    private String picture;

    @Column(name = "sex")
    private AppUserSex sex;

    public boolean equals(AppUser user) {
        return this.getId() == user.getId();
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
