package com.example.ims_spring.dto;

import com.example.ims_spring.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;
}
