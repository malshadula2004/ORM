package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.util.Regex;

public class InstructorFormController {

    public TextField txtSearch;
    @FXML
    private TextField txtInstructorId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSpecialization;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTel;
    @FXML
    private TableView<InstructorDTO> tblInstructor;
    @FXML
    private TableColumn<InstructorDTO, String> colId;
    @FXML
    private TableColumn<InstructorDTO, String> colName;
    @FXML
    private TableColumn<InstructorDTO, String> colSpecialization;
    @FXML
    private TableColumn<InstructorDTO, String> colEmail;
    @FXML
    private TableColumn<InstructorDTO, String> colTel;
    @FXML
    private JFXButton btnSave, btnUpdate, btnDelete, btnClear;

    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBO(BOFactory.BOType.INSTRUCTOR);

    @FXML
    public void initialize() {
        // TableColumn binding
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInstructorId()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSpecialization.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialization()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colTel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTel()));

        // Load all instructors
        loadAllInstructors();

        // Auto-generate next ID
        txtInstructorId.setText(instructorBO.generateNewId());
        txtInstructorId.setEditable(false); // ID field not editable
    }

    private void loadAllInstructors() {
        ObservableList<InstructorDTO> list = FXCollections.observableArrayList(instructorBO.getAllInstructors());
        tblInstructor.setItems(list);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidTel(String tel) {
        return tel.matches("^\\d{10}$"); // only 10 digit numbers
    }

    @FXML
    private void btnSaveOnAction() {
        if (txtName.getText().isEmpty() || txtSpecialization.getText().isEmpty() || txtEmail.getText().isEmpty() || txtTel.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields must be filled!").show();
            return;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email format!").show();
            return;
        }

        if (!isValidTel(txtTel.getText())) {
            new Alert(Alert.AlertType.WARNING, "Telephone must be 10 digits!").show();
            return;
        }

        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText(), txtEmail.getText(), txtTel.getText());
        if (instructorBO.saveInstructor(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor saved successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnUpdateOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an Instructor to update!").show();
            return;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Invalid Email format!").show();
            return;
        }

        if (!isValidTel(txtTel.getText())) {
            new Alert(Alert.AlertType.WARNING, "Telephone must be 10 digits!").show();
            return;
        }

        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText(), txtEmail.getText(), txtTel.getText());
        if (instructorBO.updateInstructor(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor updated successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnDeleteOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an Instructor to delete!").show();
            return;
        }

        if (instructorBO.deleteInstructor(txtInstructorId.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor deleted successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnClearOnAction() {
        clearFields();
    }

    @FXML
    private void tblInstructorOnClickAction() {
        InstructorDTO dto = tblInstructor.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtInstructorId.setText(dto.getInstructorId());
            txtName.setText(dto.getName());
            txtSpecialization.setText(dto.getSpecialization());
            txtEmail.setText(dto.getEmail());
            txtTel.setText(dto.getTel());
        }
    }

    private void clearFields() {
        txtName.clear();
        txtSpecialization.clear();
        txtEmail.clear();
        txtTel.clear();
        txtInstructorId.setText(instructorBO.generateNewId());
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase(); // search text
        ObservableList<InstructorDTO> allInstructors = FXCollections.observableArrayList(instructorBO.getAllInstructors());
        ObservableList<InstructorDTO> filteredList = FXCollections.observableArrayList();

        for (InstructorDTO dto : allInstructors) {
            if (dto.getInstructorId().toLowerCase().contains(searchText) ||
                    dto.getName().toLowerCase().contains(searchText) ||
                    dto.getEmail().toLowerCase().contains(searchText) ||
                    dto.getTel().toLowerCase().contains(searchText)) {
                filteredList.add(dto);
            }
        }
        tblInstructor.setItems(filteredList);
    }

    public void txtTelKeyAction(KeyEvent keyEvent) {
        Regex.setTextColor(Regex.FieldType.TEL, txtTel);
    }

    public void txtEmaileyAction(KeyEvent keyEvent) {
        Regex.setTextColor(Regex.FieldType.EMAIL, txtEmail);
    }
}
