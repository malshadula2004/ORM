package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentTm {
    private String studentId;
    private String name;
    private String address;
    private Long tel;
    private Date registrationDate;
    private String email;
    private String course;
    private Double amount;
}
