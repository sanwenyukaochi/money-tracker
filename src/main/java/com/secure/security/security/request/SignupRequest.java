package com.secure.security.security.request;

import jakarta.validation.constraints.*;

public record SignupRequest(
        @NotBlank
        @Size(min = 3, max = 20)
        String username,

        @NotBlank
        @Size(min = 3, max = 40)
        String password,

        @NotBlank
        @Size(max = 50)
        @Email
        String email
) {
}