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
import com.example.demo.service.util.ImageUploaderService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final ImageUploaderService imageUploaderService;

    public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthService authService, ImageUploaderService imageUploaderService) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.imageUploaderService = imageUploaderService;
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

    @Transactional
    public AppUser updateUserDetails(UserDetailsDTO userDetailsDTO){
        AppUser user =  this.createAppUserFromUserDetailsDto(this.getAuthenticatedUserDetails());
        user.setUsername(userDetailsDTO.getUsername());
        user.setEmail(userDetailsDTO.getEmail());
        user.setBiography(userDetailsDTO.getBiography());
        user.setPicture(userDetailsDTO.getPicture());
        user.setBirthdate(LocalDate.parse(userDetailsDTO.getBirthdate()));
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

    @Transactional
    public AppUser updateProfilePicture(MultipartFile mf) throws IOException {
        AppUser user = authService.getAuthenticatedAppUser();
        String urlOfUploadedFile = imageUploaderService.uploadImage(mf, "user/profile");
        user.setPicture(urlOfUploadedFile);
        return appUserRepository.save(user);
    }
}
