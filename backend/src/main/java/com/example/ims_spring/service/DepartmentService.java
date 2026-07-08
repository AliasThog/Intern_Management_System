package com.example.ims_spring.service;

import com.example.ims_spring.dto.DepartmentRequest;
import com.example.ims_spring.dto.DepartmentResponse;
import com.example.ims_spring.entity.Department;
import com.example.ims_spring.exception.BadRequestException;
import com.example.ims_spring.exception.ResourceNotFoundException;
import com.example.ims_spring.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartments(String search) {
        List<Department> departments;

        if (search == null || search.trim().isEmpty()) {
            departments = departmentRepository.findAll(Sort.by("id").descending());
        } else {
            departments = departmentRepository.findByNameContainingIgnoreCase(search.trim(), Sort.by("id").descending());
        }
        return departments.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String name = request.getName().trim();

        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Department name already exists");
        }

        Department department = Department.builder()
                .name(name)
                .description(request.getDescription())
                .build();

        Department savedDepartment = departmentRepository.save(department);

        return toResponse(savedDepartment);
    }

    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = getDepartmentEntityById(id);

        String name = request.getName().trim();

        if (!department.getName().equalsIgnoreCase(name)
                && departmentRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("Department name already exists");
        }

        department.setName(name);
        department.setDescription(request.getDescription());

        Department updatedDepartment = departmentRepository.save(department);

        return toResponse(updatedDepartment);
    }

    public void deleteDepartment(Long id) {
        Department department = getDepartmentEntityById(id);
        departmentRepository.delete(department);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentEntityById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + id
                ));
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
