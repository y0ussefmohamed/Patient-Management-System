package com.pms.auth_service.Service;

import com.pms.auth_service.DTO.LoginRequestDTO;
import java.util.Optional;

import com.pms.auth_service.Repository.UserRepository;
import com.pms.auth_service.Util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,  JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userRepository
                .findByEmail(loginRequestDTO.getEmail())
                .filter(userEntity -> passwordEncoder.matches(
                        loginRequestDTO.getPassword(),
                        userEntity.getPassword()
                ))
                .map(userEntity -> jwtUtil.generateToken(userEntity.getEmail(), userEntity.getRole()));

        return token;
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}