package com.example.demo.restController.user;

import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserPasswordDTO;
import com.example.demo.model.user.AppUser;
import com.example.demo.service.user.AppUserService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsDTO> getUserDetails(){
        System.out.println("ddddd");
        UserDetailsDTO userDetailsDTO = this.appUserService.getAuthenticatedUserDetails();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDetailsDTO);
    }

    @PutMapping("/details")
    public ResponseEntity<UserDetailsDTO> updateUserInformation(@NotNull @RequestBody UserDetailsDTO user){
        AppUser updatedUser = appUserService.updateUserDetails(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserDetailsDTO(updatedUser));
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateUserPassword(@NotNull @RequestBody UserPasswordDTO userPasswordDTO){
        String token = appUserService.updateUserPassword(userPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }
}
