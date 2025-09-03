package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.tm.PaymentTM;

import java.util.List;

public class PaymentTableFormController {

    @FXML
    private TableView<PaymentTM> tblPayments;

    @FXML
    private TableColumn<PaymentTM, String> colPaymentId;

    @FXML
    private TableColumn<PaymentTM, String> colStudent;

    @FXML
    private TableColumn<PaymentTM, String> colProgram;

    @FXML
    private TableColumn<PaymentTM, Double> colAmount;

    @FXML
    private TableColumn<PaymentTM, String> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colStatus;

    private final ObservableList<PaymentTM> paymentList = FXCollections.observableArrayList();
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);

    @FXML
    public void initialize() {
        colPaymentId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentId()));
        colStudent.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentName()));
        colProgram.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProgramName()));
        colAmount.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getAmount()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        // Editable Status column
        colStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        colStatus.setOnEditCommit(event -> {
            PaymentTM payment = event.getRowValue();
            payment.setStatus(event.getNewValue());
            try {
                assert paymentBO != null;
                paymentBO.updateStatus(payment.getPaymentId(), event.getNewValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Load DB data
        loadPayments();
    }

    private void loadPayments() {
        paymentList.clear();
        try {
            List<PaymentDTO> dtos = paymentBO.getAllPayments();
            for (PaymentDTO dto : dtos) {
                paymentList.add(new PaymentTM(
                        dto.getPaymentId(),
                        dto.getStudentId(),
                        dto.getProgramId(),
                        dto.getAmount(),
                        dto.getPaymentDate(),
                       dto.getStatus()
                ));
            }
            tblPayments.setItems(paymentList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payments!").show();
        }
    }
}
