package com.example.demo.service.user;

import com.example.demo.configuration.security.JwtTokenProvider;
import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserLoginDTO;
import com.example.demo.dto.user.UserPasswordDTO;
import com.example.demo.dto.user.UserRegisterDTO;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.AppUserRole;
import com.example.demo.model.user.AppUserSex;
import com.example.demo.repository.user.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthService authService) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    private AppUser createAppUserFromUserDetailsDto(UserDetailsDTO userDto){
        AppUser user = appUserRepository.findAppUserByUsername(userDto.getUsername());

        if(user == null) throw new CustomHttpException("Utilisateur introuvable!", HttpStatus.BAD_REQUEST);

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setBiography(userDto.getBiography());
        user.setPicture(userDto.getPicture());
        return user;
    }

    public AppUser updateUserDetails(UserDetailsDTO userDetailsDTO){
        AppUser user =  this.createAppUserFromUserDetailsDto(this.getAuthenticatedUserDetails());
        user.setUsername(userDetailsDTO.getUsername());
        user.setEmail(userDetailsDTO.getEmail());
        user.setBiography(userDetailsDTO.getBiography());
        user.setPicture(userDetailsDTO.getPicture());
        user.setBirthdate(userDetailsDTO.getBirthdate());
        user.setSex(AppUserSex.valueOf(userDetailsDTO.getSex()));
        return appUserRepository.save(user);
    }

    public UserDetailsDTO getAuthenticatedUserDetails(){
        return new UserDetailsDTO(this.authService.getAuthenticatedAppUser());
    }


    @Transactional
    public String updateUserPassword(UserPasswordDTO userPasswordDTO){
        AppUser appUser = this.authService.getAuthenticatedAppUser();

        if(passwordEncoder.matches(userPasswordDTO.getCurrentPassword(), appUser.getPassword())){
            appUser.setPassword(passwordEncoder.encode(userPasswordDTO.getNewPassword()));
            appUserRepository.save(appUser);
            return this.authService.authenticate(new UserLoginDTO(appUser.getUsername(), userPasswordDTO.getNewPassword()));
        }

        throw new CustomHttpException("Invalid Password!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
