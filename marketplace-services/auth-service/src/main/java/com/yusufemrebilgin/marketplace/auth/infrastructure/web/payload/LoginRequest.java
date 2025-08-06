package com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Size(max = 50, message = "Username must be at most 50 characters long")
        @NotBlank(message = "Username is required. Please provide a valid username")
        String username,

        @Size(min = 6, max = 60, message = "Password must be between 6 and 60 characters long")
        @NotBlank(message = "Password is required. Please provide a valid password")
        String password

) {}
