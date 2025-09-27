package me.amira.studentmvc.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.amira.studentmvc.entities.Student;
import me.amira.studentmvc.repositories.StudentRepository;
import me.amira.studentmvc.security.entities.AppUser;
import me.amira.studentmvc.security.repositories.AppRoleRepository;
import me.amira.studentmvc.security.repositories.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Data
@AllArgsConstructor
public class StudentController {

    private StudentRepository studentRepository;
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;

    @GetMapping(path = "/user/students")
    public String students(Model model,
                           @RequestParam(name="page", defaultValue = "0") int page,
                           @RequestParam(name="size", defaultValue = "6") int size,
                           @RequestParam(name="keyword", defaultValue = "") String keyword) {
        Page<Student> studentPage = studentRepository.searchStudents(keyword, PageRequest.of(page, size));
        model.addAttribute("listStudents", studentPage.getContent());
        model.addAttribute("pages", new int[studentPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "students";
    }

    @GetMapping("/admin/delete")
    public String delete(Long id, String keyword, int page) {
        studentRepository.deleteById(id);
        return "redirect:/user/students?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/student/home")
    public String home() {
        return "home";
    }


    @GetMapping("/user/listStudent")
    public String listStudent(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Student student = studentRepository.findById(id).get();
        model.addAttribute("student", student);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "listStudent";
    }

    @GetMapping("/admin/formStudents")
    public String formStudents(Model model) {
        model.addAttribute("student", new Student());
        return "formStudents";
    }

    @GetMapping("/user/profile")
    public String profile(Model model, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        model.addAttribute("user", appUser);
        return "profile";
    }

    @PostMapping("/admin/save")
    public String save(Model model, @Valid Student student, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors())
            return "formStudents";
        studentRepository.save(student);
        return "redirect:/user/students?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/editStudent")
    public String editStudent(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) throw new RuntimeException("Student introuvable!!!");
        model.addAttribute("student", student);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editStudent";
    }
}
