package me.amira.studentmvc.repositories;

import me.amira.studentmvc.entities.Student;
import me.amira.studentmvc.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    @Query("SELECT s FROM Teacher s WHERE " +
            "LOWER(s.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Teacher> searchTeachers(@Param("keyword") String keyword, Pageable pageable);
}

