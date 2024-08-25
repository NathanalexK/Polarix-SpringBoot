package com.example.demo.service.user;

import com.example.demo.configuration.security.JwtTokenProvider;
import com.example.demo.dto.user.*;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.user.*;
import com.example.demo.repository.user.AppUserRepository;
import com.example.demo.repository.user.FriendRepository;
import com.example.demo.service.util.ImageUploaderService;
import com.example.demo.util.Pagination;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final ImageUploaderService imageUploaderService;
    private final FriendRepository friendRepository;

    public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, AuthService authService, ImageUploaderService imageUploaderService, FriendRepository friendRepository) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.imageUploaderService = imageUploaderService;
        this.friendRepository = friendRepository;
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

    public Pagination<UserDetailsDTO> findAll(Integer pageSize, Integer pageNumber){
        Pageable pageable = PageRequest.of(pageSize, pageNumber);
        return new Pagination<>(appUserRepository.findAllAppUser(pageable));
    }

    public Pagination<UserDetailsDTO> findAllByName(String name, Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new Pagination<>(appUserRepository.searchAppUserByName(name, pageable));
    }

    public UserProfileDTO getUserProfileByUsername(String username) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        AppUser viewerUser = appUserRepository.findAppUserByUsername(username);
        UserProfileDTO profileDTO = appUserRepository.findUserProfileByIdUser(viewerUser.getId(), currentUser.getId());
        Friend friend = friendRepository.findFriendByTwoUsers(currentUser.getUsername(), viewerUser.getUsername());
        FriendType friendType = FriendType.NONE;

        if(friend != null) {
            if(friend.getDateConfirm() != null) friendType = FriendType.FRIEND;
            else {
                if(friend.getSender().getId() == currentUser.getId()) friendType = FriendType.SEND;
                else friendType = FriendType.CONFIRM;
            }
        }
        profileDTO.setFriendType(friendType.name());
        return profileDTO;
    }

    public Pagination<UserSimpleDetailsDTO> getFriendByUsername(String username, Integer pageNumber, Integer pageSize){
        AppUser user = appUserRepository.findAppUserByUsername(username);
        if(user == null) throw new CustomHttpException("User not found for username: " + username, HttpStatus.INTERNAL_SERVER_ERROR);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserSimpleDetailsDTO> page = appUserRepository.findAllFriendByIdUserPageable(user.getId(), pageable);
        return new Pagination<UserSimpleDetailsDTO>(page);
    }

    public Pagination<UserSimpleDetailsDTO> getFriendRequestByUsername(String username, Integer pageNumber, Integer pageSize){
        AppUser user = appUserRepository.findAppUserByUsername(username);
        if(user == null) throw new CustomHttpException("User not found for username: " + username, HttpStatus.INTERNAL_SERVER_ERROR);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<UserSimpleDetailsDTO> page = appUserRepository.findAllFriendRequestByIdUserPageable(user.getId(), pageable);
        return new Pagination<UserSimpleDetailsDTO>(page);
    }
}
