package me.amira.studentmvc.repositories;

import me.amira.studentmvc.entities.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Size;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    @Query("SELECT s FROM Subject s WHERE " +
            "LOWER(s.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.Professorsname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Subject> searchSubjects(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Subject s LEFT JOIN FETCH s.chapters WHERE s.id = :id")
    Optional<Subject> findByIdWithChapters(@Param("id") Long id);
}

