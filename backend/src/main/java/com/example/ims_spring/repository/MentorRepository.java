package com.example.ims_spring.repository;

import com.example.ims_spring.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    @Query("""
            SELECT m FROM Mentor m
            JOIN m.department d
            WHERE (:search IS NULL
                OR LOWER(m.fullName) LIKE CONCAT('%', CAST(:search AS string), '%')
                OR LOWER(m.email) LIKE CONCAT('%', CAST(:search AS string), '%'))
            AND (:departmentId IS NULL OR d.id = :departmentId)
            ORDER BY m.id DESC
            """)
    List<Mentor> searchMentors(
            @Param("search") String search,
            @Param("departmentId") Long departmentId
    );

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByDepartmentId(Long departmentId);
}
