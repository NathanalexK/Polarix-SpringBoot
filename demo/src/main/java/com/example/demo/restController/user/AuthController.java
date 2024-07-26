package com.example.demo.restController.user;

import com.example.demo.dto.user.UserLoginDTO;
import com.example.demo.dto.user.UserRegisterDTO;
import com.example.demo.model.user.AppUser;
import com.example.demo.repository.user.AppUserRepository;
import com.example.demo.service.user.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDto){
        String token = appUserService.register(userRegisterDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO){
        String token = appUserService.authenticate(userLoginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }
}
