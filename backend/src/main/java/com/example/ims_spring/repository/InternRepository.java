package com.example.ims_spring.repository;

import com.example.ims_spring.entity.Intern;
import com.example.ims_spring.entity.InternStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {

    @Query("""
            SELECT i FROM Intern i
            JOIN i.department d
            JOIN i.mentor m
            WHERE (:search IS NULL
                OR LOWER(i.fullName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                OR LOWER(i.email) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
            AND (:departmentId IS NULL OR d.id = :departmentId)
            AND (:mentorId IS NULL OR m.id = :mentorId)
            AND (:status IS NULL OR i.status = :status)
            ORDER BY i.id DESC
            """)
    List<Intern> searchInterns(
            @Param("search") String search,
            @Param("departmentId") Long departmentId,
            @Param("mentorId") Long mentorId,
            @Param("status") InternStatus status
    );

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByDepartmentId(Long departmentId);

    boolean existsByMentorId(Long mentorId);
    long countByStatus(InternStatus status);
}
