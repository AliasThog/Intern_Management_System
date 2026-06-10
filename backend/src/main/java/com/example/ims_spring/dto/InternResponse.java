package com.example.ims_spring.dto;

import com.example.ims_spring.entity.InternStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternResponse {
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String school;

    private String major;

    private LocalDate startDate;

    private LocalDate endDate;

    private InternStatus status;

    private Long departmentId;

    private String departmentName;

    private Long mentorId;

    private String mentorName;
}
