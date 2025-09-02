package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "culinary_programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class course {

    @Id
    @Column(length = 10)
    private String programId;

    @Column(nullable = false)
    private String programName;

    @Column(nullable = false)
    private int duration; // in months

    @Column(nullable = false)
    private double fee;


    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;


    public course(String programId, String programName, int duration, double fee) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }
}
