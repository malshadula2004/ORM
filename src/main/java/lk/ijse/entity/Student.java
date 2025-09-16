package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    private String studentId;

    private String name;
    private String address;
    private Long tel;
    private Date registrationDate;

    @Column(nullable = false, unique = true)
    private String email;

    private int someInt;

    private String course;  // single course
    private Double amount;  // amount associated with student

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();


    public Student(String studentId, String name, String address, Long tel, Date registrationDate,
                   String email, String course, Double amount) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.registrationDate = registrationDate;
        this.email = email;
        this.course = course;
        this.amount = amount;
    }


    public Student(String studentId) {
        this.studentId = studentId;
    }
}
