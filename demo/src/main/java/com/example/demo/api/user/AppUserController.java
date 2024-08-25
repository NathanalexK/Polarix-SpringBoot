package com.example.demo.api.user;

import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserPasswordDTO;
import com.example.demo.dto.user.UserProfileDTO;
import com.example.demo.model.user.AppUser;
import com.example.demo.service.user.AppUserService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/search/{name}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<UserDetailsDTO>> searchByName(
            @PathVariable("name") String name,
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize
    ) {
        return ResponseEntity.ok(appUserService.findAllByName(name, pageNumber, pageSize));
    }

    @GetMapping("/list/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<UserDetailsDTO>> listPageable(
            @PathVariable("pageNumber") Integer pageNumber,
            @PathVariable("pageSize") Integer pageSize
    ) {
        return ResponseEntity.ok(appUserService.findAll(pageNumber, pageSize));
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsDTO> getUserDetails() {
        UserDetailsDTO userDetailsDTO = this.appUserService.getAuthenticatedUserDetails();
        return ResponseEntity.ok(userDetailsDTO);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileDTO> getUserProfile(
            @NotNull @PathVariable("username") String username
    ) {
        UserProfileDTO userProfileDTO = appUserService.getUserProfileByUsername(username);
        return ResponseEntity.ok(userProfileDTO);
    }

    @PutMapping("/details")
    public ResponseEntity<UserDetailsDTO> updateUserInformation(@NotNull @RequestBody UserDetailsDTO user) {
        AppUser updatedUser = appUserService.updateUserDetails(user);
        return ResponseEntity.ok(new UserDetailsDTO(updatedUser));
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateUserPassword(@NotNull @RequestBody UserPasswordDTO userPasswordDTO) {
        String token = appUserService.updateUserPassword(userPasswordDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<UserDetailsDTO> updateUserProfilePicture(@RequestParam("file") MultipartFile file)
            throws IOException {
        AppUser updatedUser = appUserService.updateProfilePicture(file);
        return ResponseEntity.ok(new UserDetailsDTO(updatedUser));
    }
}
