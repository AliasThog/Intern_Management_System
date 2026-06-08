package com.example.ims_spring.config;

import com.example.ims_spring.entity.Department;
import com.example.ims_spring.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            return;
        }
        departmentRepository.save(Department.builder()
                .name("Backend")
                .description("Java Spring Boot team")
                .build());
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
    }
}
