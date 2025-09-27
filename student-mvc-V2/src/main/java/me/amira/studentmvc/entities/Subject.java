package me.amira.studentmvc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String nom;
    @Size(min = 3, max = 50)
    private String description ;
    @Size(min = 3, max = 50)
    private String Professorsname;

    @ElementCollection
    private List<String> chapters;
}
