package com.example.ims_spring.dto;

import com.example.ims_spring.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private UserRole role;
}