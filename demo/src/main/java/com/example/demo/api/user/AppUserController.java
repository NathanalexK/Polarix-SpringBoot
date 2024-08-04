package com.example.demo.api.user;

import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserPasswordDTO;
import com.example.demo.model.user.AppUser;
import com.example.demo.service.user.AppUserService;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsDTO> getUserDetails(){
        UserDetailsDTO userDetailsDTO = this.appUserService.getAuthenticatedUserDetails();
        return ResponseEntity.ok(userDetailsDTO);
    }

    @PutMapping("/details")
    public ResponseEntity<UserDetailsDTO> updateUserInformation(@NotNull @RequestBody UserDetailsDTO user){
        AppUser updatedUser = appUserService.updateUserDetails(user);
        return ResponseEntity.ok(new UserDetailsDTO(updatedUser));
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateUserPassword(@NotNull @RequestBody UserPasswordDTO userPasswordDTO){
        String token = appUserService.updateUserPassword(userPasswordDTO);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<UserDetailsDTO> updateUserProfilePicture(@RequestParam("file")MultipartFile file)
            throws IOException {
        AppUser updatedUser = appUserService.updateProfilePicture(file);
        return ResponseEntity.ok(new UserDetailsDTO(updatedUser));
    }
}
