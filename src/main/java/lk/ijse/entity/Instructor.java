package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String instructorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<course> courses;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}
