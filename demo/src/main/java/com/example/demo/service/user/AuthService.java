package com.example.demo.service.user;

import com.example.demo.configuration.security.JwtTokenProvider;
import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserLoginDTO;
import com.example.demo.dto.user.UserRegisterDTO;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.AppUserRole;
import com.example.demo.model.user.AppUserSex;
import com.example.demo.repository.user.AppUserRepository;
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
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(UserLoginDTO userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        AppUser user = appUserRepository.findAppUserByUsername(username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, user.getRole());
        } catch (AuthenticationException e){
            throw new CustomHttpException("Invalid authentication for username: " + username + " with password: " + password + "!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String email = userRegisterDTO.getEmail();
        String password = userRegisterDTO.getPassword();
        LocalDate birthdate = userRegisterDTO.getBirthdate();
        AppUserSex sex = AppUserSex.valueOf(userRegisterDTO.getSex().toUpperCase());

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(AppUserRole.ROLE_CLIENT);
        user.setCreatedAt(LocalDate.now());
        appUserRepository.save(user);

        return this.authenticate(new UserLoginDTO(username, password));
    }

    public Authentication getAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public AppUser getAuthenticatedAppUser()
            throws RuntimeException {
        Authentication auth = this.getAuthenticated();
        AppUser authenticatedUser = appUserRepository.findAppUserByUsername(auth.getName());

        if(authenticatedUser != null)
            return authenticatedUser;

        throw new CustomHttpException("No account is logged in!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
