package com.example.ims_spring.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MentorResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String title;
    private Long departmentId;
    private String departmentName;
}
