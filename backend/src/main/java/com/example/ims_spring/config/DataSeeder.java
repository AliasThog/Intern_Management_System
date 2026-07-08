package com.example.ims_spring.config;

import com.example.ims_spring.entity.Department;
import com.example.ims_spring.entity.Intern;
import com.example.ims_spring.entity.InternStatus;
import com.example.ims_spring.entity.Mentor;
import com.example.ims_spring.repository.DepartmentRepository;
import com.example.ims_spring.repository.InternRepository;
import com.example.ims_spring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final MentorRepository mentorRepository;
    private final InternRepository internRepository;

    @Override
    public void run(String... args) {
        seedDepartments();
        seedMentors();
        seedInterns();
    }

    private void seedDepartments() {
        if (departmentRepository.count() > 0) {
            return;
        }

        departmentRepository.save(
                Department.builder()
                        .name("Backend")
                        .description("Java Spring Boot team")
                        .build()
        );

        departmentRepository.save(
                Department.builder()
                        .name("Frontend")
                        .description("React team")
                        .build()
        );

        departmentRepository.save(
                Department.builder()
                        .name("QA")
                        .description("Testing team")
                        .build()
        );

        departmentRepository.save(
                Department.builder()
                        .name("Mobile")
                        .description("Mobile development team")
                        .build()
        );

        departmentRepository.save(
                Department.builder()
                        .name("Data")
                        .description("Data team")
                        .build()
        );
    }

    private void seedMentors() {
        long currentMentorCount = mentorRepository.count();

        if (currentMentorCount >= 1000) {
            return;
        }

        List<Department> departments = departmentRepository.findAll();

        if (departments.isEmpty()) {
            return;
        }

        for (long i = currentMentorCount + 1; i <= 1000; i++) {
            Department department = departments.get(
                    (int) ((i - 1) % departments.size())
            );

            Mentor mentor = Mentor.builder()
                    .fullName("Mentor " + i)
                    .email("mentor" + i + "@company.com")
                    .phone("090000" + String.format("%04d", i))
                    .department(department)
                    .build();

            mentorRepository.save(mentor);
        }
    }

    private void seedInterns() {
        long currentInternCount = internRepository.count();

        if (currentInternCount >= 1000) {
            return;
        }

        List<Mentor> mentors = mentorRepository.findAllWithDepartment();

        if (mentors.isEmpty()) {
            return;
        }

        InternStatus[] statuses = InternStatus.values();

        for (long i = currentInternCount + 1; i <= 1000; i++) {
            Mentor mentor = mentors.get((int) ((i - 1) % mentors.size()));

            Department department = mentor.getDepartment();

            LocalDate startDate = LocalDate.now().minusDays(i % 60);
            LocalDate endDate = startDate.plusMonths(3);

            InternStatus status = statuses[(int) ((i - 1) % statuses.length)];

            Intern intern = Intern.builder()
                    .fullName("Intern " + i)
                    .email("intern" + i + "@company.com")
                    .phone("091000" + String.format("%04d", i))
                    .school("University " + ((i % 10) + 1))
                    .major("Software Engineering")
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(status)
                    .department(department)
                    .mentor(mentor)
                    .build();

            internRepository.save(intern);
        }
    }
}