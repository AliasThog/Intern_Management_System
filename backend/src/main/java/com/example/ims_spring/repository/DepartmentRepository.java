package com.example.ims_spring.repository;

import com.example.ims_spring.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findByNameContainingIgnoreCase(String name, Sort sort);

    boolean existsByNameIgnoreCase(String name);
}
