package com.example.demo.configuration.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenInterceptorConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenInterceptorConfigurer(JwtTokenProvider jtp){
        this.jwtTokenProvider = jtp;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtTokenInterceptor customJti = new JwtTokenInterceptor(this.jwtTokenProvider);
    }
}
