package com.example.demo.restController.user;

import com.example.demo.dto.user.UserLoginDTO;
import com.example.demo.dto.user.UserRegisterDTO;
import com.example.demo.service.user.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDto){
        String token = authService.register(userRegisterDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO){
        String token = authService.authenticate(userLoginDTO);
        return ResponseEntity.ok(token);
    }
}
