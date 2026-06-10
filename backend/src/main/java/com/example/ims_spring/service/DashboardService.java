package com.example.ims_spring.service;

import com.example.ims_spring.dto.DashboardSummaryResponse;
import com.example.ims_spring.entity.InternStatus;
import com.example.ims_spring.repository.DepartmentRepository;
import com.example.ims_spring.repository.InternRepository;
import com.example.ims_spring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DepartmentRepository departmentRepository;
    private final MentorRepository mentorRepository;
    private final InternRepository internRepository;

    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
                .totalDepartments(departmentRepository.count())
                .totalMentors(mentorRepository.count())
                .totalInterns(internRepository.count())
                .onboardingInterns(internRepository.countByStatus(InternStatus.ONBOARDING))
                .activeInterns(internRepository.countByStatus(InternStatus.ACTIVE))
                .completedInterns(internRepository.countByStatus(InternStatus.COMPLETED))
                .droppedInterns(internRepository.countByStatus(InternStatus.DROPPED))
                .build();
    }
}
