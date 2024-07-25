package com.example.demo.repository.user;

import com.example.demo.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.BindParam;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query("select u from AppUser u where u.username = :username")
    public AppUser findAppUserByUsername(@BindParam("username") String username);
}
