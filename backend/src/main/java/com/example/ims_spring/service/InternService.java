package com.example.ims_spring.service;

import com.example.ims_spring.dto.InternRequest;
import com.example.ims_spring.dto.InternResponse;
import com.example.ims_spring.entity.Department;
import com.example.ims_spring.entity.Intern;
import com.example.ims_spring.entity.InternStatus;
import com.example.ims_spring.entity.Mentor;
import com.example.ims_spring.exception.BadRequestException;
import com.example.ims_spring.exception.ResourceNotFoundException;
import com.example.ims_spring.repository.InternRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InternService {

    private final InternRepository internRepository;
    private final DepartmentService departmentService;
    private final MentorService mentorService;

    @Transactional(readOnly = true)
    public List<InternResponse> getInterns(
            String search,
            Long departmentId,
            Long mentorId,
            InternStatus status
    ) {
        String cleanSearch = null;

        if (search != null && !search.trim().isEmpty()) {
            cleanSearch = search.trim();
        }

        return internRepository.searchInterns(
                cleanSearch,
                departmentId,
                mentorId,
                status
        )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InternResponse createIntern(InternRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        String email = request.getEmail().trim();

        if (internRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Intern email already exists");
        }

        Department department = departmentService.getDepartmentEntityById(
                request.getDepartmentId()
        );

        Mentor mentor = null;

        if (request.getMentorId() != null) {
            mentor = mentorService.getMentorEntityById(request.getMentorId());
            validateMentorBelongsToDepartment(mentor, department);
        }

        Intern intern = Intern.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .phone(request.getPhone())
                .school(request.getSchool())
                .major(request.getMajor())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .department(department)
                .mentor(mentor)
                .build();

        Intern savedIntern = internRepository.save(intern);

        return toResponse(savedIntern);
    }

    public InternResponse updateIntern(Long id, InternRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Intern intern = getInternEntityById(id);

        String email = request.getEmail().trim();

        if (!intern.getEmail().equalsIgnoreCase(email)
                && internRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Intern email already exists");
        }

        Department department = departmentService.getDepartmentEntityById(
                request.getDepartmentId()
        );

        Mentor mentor = null;

        if (request.getMentorId() != null) {
            mentor = mentorService.getMentorEntityById(request.getMentorId());
            validateMentorBelongsToDepartment(mentor, department);
        }

        intern.setFullName(request.getFullName().trim());
        intern.setEmail(email);
        intern.setPhone(request.getPhone());
        intern.setSchool(request.getSchool());
        intern.setMajor(request.getMajor());
        intern.setStartDate(request.getStartDate());
        intern.setEndDate(request.getEndDate());
        intern.setStatus(request.getStatus());
        intern.setDepartment(department);
        intern.setMentor(mentor);

        Intern updatedIntern = internRepository.save(intern);

        return toResponse(updatedIntern);
    }

    public void deleteIntern(Long id) {
        Intern intern = getInternEntityById(id);
        internRepository.delete(intern);
    }

    @Transactional(readOnly = true)
    public Intern getInternEntityById(Long id) {
        return internRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Intern not found with id: " + id
                ));
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BadRequestException("End date must be after start date");
        }
    }

    private void validateMentorBelongsToDepartment(
            Mentor mentor,
            Department department
    ) {
        Long mentorDepartmentId = mentor.getDepartment().getId();
        Long internDepartmentId = department.getId();

        if (!mentorDepartmentId.equals(internDepartmentId)) {
            throw new BadRequestException(
                    "Mentor does not belong to selected department"
            );
        }
    }

    private InternResponse toResponse(Intern intern) {
        Long mentorId = null;
        String mentorName = null;

        if (intern.getMentor() != null) {
            mentorId = intern.getMentor().getId();
            mentorName = intern.getMentor().getFullName();
        }

        return InternResponse.builder()
                .id(intern.getId())
                .fullName(intern.getFullName())
                .email(intern.getEmail())
                .phone(intern.getPhone())
                .school(intern.getSchool())
                .major(intern.getMajor())
                .startDate(intern.getStartDate())
                .endDate(intern.getEndDate())
                .status(intern.getStatus())
                .departmentId(intern.getDepartment().getId())
                .departmentName(intern.getDepartment().getName())
                .mentorId(mentorId)
                .mentorName(mentorName)
                .build();
    }
}
