package me.amira.studentmvc.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.amira.studentmvc.entities.Teacher;
import me.amira.studentmvc.repositories.TeacherRepository;
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
import java.util.List;

@Controller
@Data
@AllArgsConstructor
public class TeacherController {

    private TeacherRepository TeacherRepository;
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;

    @GetMapping(path = "/user/teachers")
    public String teachers(Model model,
                           @RequestParam(name="page", defaultValue = "0") int page,
                           @RequestParam(name="size", defaultValue = "6") int size,
                           @RequestParam(name="keyword", defaultValue = "") String keyword){
        Page<Teacher> teacherPage = TeacherRepository.searchTeachers(keyword, PageRequest.of(page,size));
        model.addAttribute("listTeachers",teacherPage.getContent());
        model.addAttribute("pages",new int[teacherPage.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "teachers";
    }

    @GetMapping("/admin/delete-teacher")
    public String delete(Long id, String keyword, int page){
        TeacherRepository.deleteById(id);
        return "redirect:/user/teachers?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/user/teachers/list")
    @ResponseBody
    public List<Teacher> listTeachers(){
        return TeacherRepository.findAll();
    }

    @GetMapping("/admin/formTeacher")
    public String formTeachers(Model model){
        model.addAttribute("teacher", new Teacher());
        return "formTeacher";
    }

    @GetMapping("/user/teacher/profile")
    public String profile(Model model, String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        model.addAttribute("user", appUser);
        return "profile";
    }

    @PostMapping("/admin/save-teacher")
    public String save(Model model, @Valid Teacher teacher, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors())
            return "formTeachers";
        TeacherRepository.save(teacher);
        return "redirect:/user/teachers?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/user/listTeacher")
    public String listTeacher(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Teacher teacher = TeacherRepository.findById(id).get();
        model.addAttribute("teacher", teacher);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "listTeacher";
    }

    @GetMapping("/admin/editTeacher")
    public String editTeacher(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page){
        Teacher teacher = TeacherRepository.findById(id).orElse(null);
        if (teacher == null) throw new RuntimeException("Teacher introuvable!!!");
        model.addAttribute("teacher", teacher);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editTeacher";
    }
}
