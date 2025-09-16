package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    private String instructorId;  // String ID, manually assign

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, unique = true)
    private String email;

    private String tel;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    // One arg constructor
    public Instructor(String instructorId) {
        this.instructorId = instructorId;
        this.name = "";
        this.specialization = "";
        this.email = "";
        this.tel = "";
    }


    public Instructor(String instructorId, String name, String specialization, String email, String tel) {
        this.instructorId = instructorId;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.tel = tel;
    }
}
