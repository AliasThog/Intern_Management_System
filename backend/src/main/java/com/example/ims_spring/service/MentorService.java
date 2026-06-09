package com.example.ims_spring.service;

import com.example.ims_spring.dto.MentorRequest;
import com.example.ims_spring.dto.MentorResponse;
import com.example.ims_spring.entity.Department;
import com.example.ims_spring.entity.Mentor;
import com.example.ims_spring.exception.BadRequestException;
import com.example.ims_spring.exception.ResourceNotFoundException;
import com.example.ims_spring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MentorService {
    private final MentorRepository mentorRepository;
    private final DepartmentService departmentService;

    @Transactional(readOnly = true)
    public List<MentorResponse> getMentors(String search, Long departmentId) {
        String cleanSearch = null;

        if (search != null && !search.trim().isEmpty()) {
            cleanSearch = search.trim().toLowerCase();
        }

        return mentorRepository.searchMentors(cleanSearch, departmentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MentorResponse> getAllMentors() {
        return mentorRepository.findAll(Sort.by("fullName").ascending())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MentorResponse createMentor(MentorRequest request) {
        String email = request.getEmail().trim();

        if (mentorRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Mentor email already exists");
        }

        Department department = departmentService.getDepartmentEntityById(
                request.getDepartmentId()
        );

        Mentor mentor = Mentor.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .phone(request.getPhone())
                .department(department)
                .build();

        Mentor savedMentor = mentorRepository.save(mentor);

        return toResponse(savedMentor);
    }

    public MentorResponse updateMentor(Long id, MentorRequest request) {
        Mentor mentor = getMentorEntityById(id);

        String email = request.getEmail().trim();

        if (!mentor.getEmail().equalsIgnoreCase(email)
                && mentorRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Mentor email already exists");
        }

        Department department = departmentService.getDepartmentEntityById(
                request.getDepartmentId()
        );

        mentor.setFullName(request.getFullName().trim());
        mentor.setEmail(email);
        mentor.setPhone(request.getPhone());
        mentor.setDepartment(department);

        Mentor updatedMentor = mentorRepository.save(mentor);

        return toResponse(updatedMentor);
    }

    public void deleteMentor(Long id) {
        Mentor mentor = getMentorEntityById(id);
        mentorRepository.delete(mentor);
    }

    @Transactional(readOnly = true)
    public Mentor getMentorEntityById(Long id) {
        return mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mentor not found with id: " + id
                ));
    }

    private MentorResponse toResponse(Mentor mentor) {
        return MentorResponse.builder()
                .id(mentor.getId())
                .fullName(mentor.getFullName())
                .email(mentor.getEmail())
                .phone(mentor.getPhone())
                .departmentId(mentor.getDepartment().getId())
                .departmentName(mentor.getDepartment().getName())
                .build();
    }

}