package com.example.demo.restController.user;

import com.example.demo.dto.user.UserRegisterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Okay");
    }
}
