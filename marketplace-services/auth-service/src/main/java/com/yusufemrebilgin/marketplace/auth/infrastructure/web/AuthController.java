package com.yusufemrebilgin.marketplace.auth.infrastructure.web;

import com.yusufemrebilgin.marketplace.auth.application.port.in.LoginCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.in.RegisterCommand;
import com.yusufemrebilgin.marketplace.auth.application.service.AuthService;
import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;
import com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload.LoginRequest;
import com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload.RegistrationRequest;
import com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload.TokenResponse;
import com.yusufemrebilgin.marketplace.common.domain.model.Email;
import com.yusufemrebilgin.marketplace.common.domain.model.IdentityNumber;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {

        TokenPair tokenPair = authService.login(new LoginCommand(
                new Username(request.username()),
                new Password(request.password())
        ));

        return ResponseEntity.ok(TokenResponse.from(tokenPair));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationRequest request) {

        RegisterCommand cmd = new RegisterCommand(
                request.firstName(),
                request.lastName(),
                new Username(request.username()),
                new Password(request.password()),
                new Email(request.email()),
                new IdentityNumber(request.identityNumber())
        );

        authService.register(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
