package lk.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTM {
    private String paymentId;
    private String studentName;
    private String programName;
    private double amount;
    private String date;
    private String status;
}
