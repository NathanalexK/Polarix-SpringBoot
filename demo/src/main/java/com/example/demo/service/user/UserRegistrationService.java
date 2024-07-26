package com.example.demo.service.user;

import com.example.demo.model.user.AppUser;
import com.example.demo.repository.user.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final AppUserRepository appUserRepository;

    public UserRegistrationService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public boolean isUsernameAlreadyExist(String username) {
        AppUser user = appUserRepository.findAppUserByUsername(username);
        return (user != null);
    }
}
