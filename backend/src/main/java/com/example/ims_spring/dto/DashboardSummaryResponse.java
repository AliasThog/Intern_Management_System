package com.example.ims_spring.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardSummaryResponse {

    private long totalDepartments;
    private long totalMentors;
    private long totalInterns;
    private long onboardingInterns;
    private long activeInterns;
    private long completedInterns;
    private long droppedInterns;
}
