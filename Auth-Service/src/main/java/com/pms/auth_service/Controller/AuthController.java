package com.pms.auth_service.Controller;

import com.pms.auth_service.DTO.LoginRequestDTO;
import com.pms.auth_service.DTO.LoginResponseDTO;
import com.pms.auth_service.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> token = authService.authenticate(loginRequestDTO);

        if (token.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(new LoginResponseDTO(token.get()));
    }

    @Operation(summary = "Validate token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader(value = "Authorization", required = false)
            String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
