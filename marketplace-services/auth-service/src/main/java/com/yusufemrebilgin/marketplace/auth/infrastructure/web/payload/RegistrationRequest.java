package com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(

        @Size(max = 50, message = "First name must be at most 50 characters long.")
        @NotBlank(message = "First name is required. Please provide a valid first name.")
        String firstName,

        @Size(max = 50, message = "Last name must be at most 50 characters long.")
        @NotBlank(message = "Last name is required. Please provide a valid last name.")
        String lastName,

        @Size(max = 50, message = "Username must be at most 50 characters long.")
        @NotBlank(message = "Username is required. Please provide a valid username.")
        String username,

        @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters long.")
        @NotBlank(message = "Password is required. Please provide a valid password.")
        String password,

        @Email(message = "Please provide a valid email address.")
        @NotBlank(message = "Email is required. Please provide a valid email.")
        String email,

        @Pattern(regexp = "\\d{11}", message = "Identity number must be exactly 11 digits.")
        @NotBlank(message = "Identity number is required. Please provide a valid identity number.")
        String identityNumber

) {}
