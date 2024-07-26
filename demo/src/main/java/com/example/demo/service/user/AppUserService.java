package com.example.demo.service.user;

import com.example.demo.configuration.security.JwtTokenProvider;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.AppUserRole;
import com.example.demo.repository.user.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password){
        System.out.println(username);
//        System.out.println(passwordEncoder.encode(password));
        AppUser user = appUserRepository.findAppUserByUsername(username);
        System.out.println(user);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, user.getRole());
        } catch (AuthenticationException e){
            throw new CustomHttpException("Invalid authentication for username: " + username + " with password: " + password + "!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Transactional
    public String register(String username, String email, String password) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(AppUserRole.ROLE_CLIENT);
        user.setCreatedAt(LocalDate.now());
        appUserRepository.save(user);
        return this.authenticate(username, password);
    }
}
