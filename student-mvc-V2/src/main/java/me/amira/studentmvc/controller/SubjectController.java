package me.amira.studentmvc.controller;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.amira.studentmvc.entities.Subject;
import me.amira.studentmvc.repositories.SubjectRepository;
import me.amira.studentmvc.security.repositories.AppRoleRepository;
import me.amira.studentmvc.security.repositories.AppUserRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.List;

@Controller
@Data
@AllArgsConstructor
public class SubjectController {

    private SubjectRepository subjectRepository;
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private final String UPLOAD_DIR = "uploads/";


    @GetMapping(path = "/user/subjects")
    public String subjects(Model model,
                           @RequestParam(name="page", defaultValue = "0") int page,
                           @RequestParam(name="size", defaultValue = "6") int size,
                           @RequestParam(name="keyword", defaultValue = "") String keyword) {
        Page<Subject> subjectPage = subjectRepository.searchSubjects(keyword, PageRequest.of(page, size));
        model.addAttribute("listSubjects", subjectPage.getContent());
        model.addAttribute("pages", new int[subjectPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "subjects";
    }

    @GetMapping("/admin/delete-subject")
    public String delete(Long id, String keyword, int page) {
        subjectRepository.deleteById(id);
        return "redirect:/user/subjects?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/subject/home")
    public String home() {
        return "subjectHome";
    }


    @GetMapping("/user/listSubject")
    public String listSubject(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Subject subject = subjectRepository.findByIdWithChapters(id).get();
        model.addAttribute("subject", subject);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "listSubject";
    }

    @GetMapping("/admin/formSubjects")
    public String formSubject(Model model) {
        model.addAttribute("subject", new Subject());
        return "formSubjects";
    }


    @PostMapping("/admin/save-Subject")
    public String save(Model model, @Valid Subject subject, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors())
            return "formSubjects";
        subjectRepository.save(subject);
        return "redirect:/user/subjects?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/editSubject")
    public String editSubject(Model model, Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null) throw new RuntimeException("Subject introuvable!!!");
        model.addAttribute("subject", subject);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "editSubject";
    }


    @PostMapping("/chapter/upload/{id}")
    @Transactional
    public String uploadChapter(@PathVariable Long id, MultipartFile file, Model model) {


        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        try {
            String fileName = file.getOriginalFilename();
            String uploadDir = "C:/Users/KHAIRI/Desktop/amira/Student-MVC-Spring-main/Student-MVC-Spring-main/Student-MVC-Spring-main/student-mvc-V2/uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }



            file.transferTo(new File(uploadDir + fileName));

            List<String> chapters = subject.getChapters();
            chapters.add(fileName);
            subject.setChapters(chapters);

            subjectRepository.save(subject);

        } catch (IOException e) {
            e.printStackTrace(); // Logs the full stack trace for debugging

            model.addAttribute("error", "File upload failed. Please try again.");
            return "redirect:/user/listSubject?id="+id + "&keyword=&page=0";
        }

        return "redirect:/user/subjects";
    }

    @GetMapping("/{id}/chapters")
    public List<String> getChapters(@PathVariable Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        return subject.getChapters();
    }
}
