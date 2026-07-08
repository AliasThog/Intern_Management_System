package com.example.ims_spring.dto;

import com.example.ims_spring.entity.InternStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternRequest {
    @NotBlank(message = "Intern full name is required")
    private String fullName;

    @NotBlank(message = "Intern email is required")
    @Email(message = "Email is invalid")
    private String email;

    private String phone;

    private String school;

    private String major;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Status is required")
    private InternStatus status;

    @NotNull(message = "Department is required")
    private Long departmentId;

    private Long mentorId;
}
