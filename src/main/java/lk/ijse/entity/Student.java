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
public class Student {

    @Id
    private String studentId;
    private String name;
    private String address;
    private Long tel;
    private Date registrationDate;
    private int someInt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "student")
    private List<Enrollment> enrollments = new ArrayList<>();


    public Student(String studentId, String name, String address, Long tel, Date registrationDate) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.registrationDate = registrationDate;
        this.someInt = 0;
        this.enrollments = new ArrayList<>();
    }
}
