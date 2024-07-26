package com.example.demo.repository.user;

import com.example.demo.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query("select u from AppUser u where u.username = :username")
    public AppUser findAppUserByUsername(@Param("username") String username);

    @Query("select u from AppUser u where u.username = :username and u.password = :password")
    public AppUser findAppUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
