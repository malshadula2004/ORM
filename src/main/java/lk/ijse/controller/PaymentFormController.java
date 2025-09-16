package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.courseDTO;

public class PaymentFormController {

    @FXML private TextField txtPaymentId;
    @FXML private ComboBox<String> cmbStudent;
    @FXML private ComboBox<String> cmbProgram;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpPaymentDate;
    @FXML private TextField txtStatus; // Status field now as TextField
    @FXML private Button btnMakePayment;
    @FXML private Button btnCancel;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);

    @FXML
    public void initialize() {
        txtPaymentId.setText(paymentBO.generatePaymentId());

        ObservableList<String> studentList = FXCollections.observableArrayList();
        paymentBO.getAllStudents().forEach(s -> studentList.add(s.getStudentId()));
        cmbStudent.setItems(studentList);

        ObservableList<String> programList = FXCollections.observableArrayList();
        paymentBO.getAllPrograms().forEach(p -> programList.add(p.getProgramId()));
        cmbProgram.setItems(programList);

        cmbStudent.setOnAction(e -> calculatePaymentAmount());
        cmbProgram.setOnAction(e -> calculatePaymentAmount());
    }

    private void calculatePaymentAmount() {
        String studentId = cmbStudent.getValue();
        String programId = cmbProgram.getValue();

        if (studentId != null && programId != null) {
            StudentDTO student = paymentBO.findStudentById(studentId);
            courseDTO program = paymentBO.findProgramById(programId);

            if (student != null && program != null) {
                double paymentAmount = program.getFee() - (student.getAmount() != null ? student.getAmount() : 0);
                txtAmount.setText(String.valueOf(paymentAmount));

                if (paymentAmount > 0) {
                    txtStatus.setText("PENDING");
                    txtStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    txtStatus.setText("COMPLETED");
                    txtStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                }
            }
        }
    }

    @FXML
    private void btnMakePaymentOnAction() {
        try {
            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    cmbStudent.getValue(),
                    cmbProgram.getValue(),
                    Double.parseDouble(txtAmount.getText()),
                    dpPaymentDate.getValue() != null ? dpPaymentDate.getValue().toString() : null,
                    txtStatus.getText()
            );

            if (paymentBO.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Saved Successfully!").show();
                clearForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Save Payment!").show();
            }
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Invalid Amount!").show();
        }
    }

    @FXML
    private void btnCancelOnAction() {
        clearForm();
    }

    private void clearForm() {
        txtPaymentId.setText(paymentBO.generatePaymentId());
        cmbStudent.getSelectionModel().clearSelection();
        cmbProgram.getSelectionModel().clearSelection();
        txtAmount.clear();
        dpPaymentDate.setValue(null);
        txtStatus.clear();
    }
}
