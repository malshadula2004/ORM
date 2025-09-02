package lk.ijse.dto;

import lk.ijse.entity.course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lk.ijse.entity.Payment;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class courseDTO extends course {
    private String programId;
    private String programName;
    private int duration;
    private double fee;
    private List<Payment> enrollments;
}
