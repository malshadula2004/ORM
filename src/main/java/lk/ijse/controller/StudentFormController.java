package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.CourseBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.courseDTO;
import lk.ijse.tdm.StudentTm;
import lk.ijse.util.Regex;

import java.sql.Date;
import java.util.List;

public class StudentFormController {

    @FXML
    public ComboBox<String> cmbCourse; // ComboBox to show course IDs
    @FXML
    public TextField txtAmount;

    @FXML
    private TextField txtSearch, txtId, txtName, txtAddress, txtTel, txtEmail;

    @FXML
    private DatePicker registerDatePicker;

    @FXML
    private AnchorPane studentForm;

    @FXML
    private TableView<StudentTm> tblStudent;

    @FXML
    private TableColumn<StudentTm, String> colId, colName, colAddress, colEmail;

    @FXML
    private TableColumn<StudentTm, Long> colTel;

    @FXML
    private TableColumn<StudentTm, Date> colRegisterDate;

    @FXML
    private Button btnSave, btnUpdate, btnDelete, btnClear;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.COURSE); // Course BO

    @FXML
    public void initialize() {
        setCellValueFactory();
        loadAllStudent();
        loadCourseIds(); // load course IDs to ComboBox
        generateStudentId();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllStudent() {
        List<StudentDTO> allStudent = studentBO.getAllStudent();
        ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();

        for (StudentDTO studentDTO : allStudent) {
            studentTms.add(new StudentTm(
                    studentDTO.getStudentId(),
                    studentDTO.getName(),
                    studentDTO.getAddress(),
                    studentDTO.getTel(),
                    studentDTO.getRegistrationDate(),
                    studentDTO.getEmail(),
                    studentDTO.getCourse(),
                    studentDTO.getAmount()
            ));
        }
        tblStudent.setItems(studentTms);
    }

    // Load course IDs into ComboBox
    private void loadCourseIds() {
        try {
            List<courseDTO> allCourses = courseBO.getAllCulinaryProgram();
            ObservableList<String> courseIds = FXCollections.observableArrayList();
            for (courseDTO course : allCourses) {
                courseIds.add(course.getProgramId()); // Add only Course IDs
            }
            cmbCourse.setItems(courseIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StudentDTO getStudentFromFields() {
        double amount = 0;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount!");
        }

        return new StudentDTO(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Long.parseLong(txtTel.getText()),
                Date.valueOf(registerDatePicker.getValue()),
                txtEmail.getText(),
                cmbCourse.getValue() != null ? cmbCourse.getValue() : null, // ComboBox value
                amount
        );
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (isValidStudent()) {
            // Save student first
            studentBO.saveStudent(getStudentFromFields());
            clearFields();
            loadAllStudent();
            showAlert(Alert.AlertType.INFORMATION, "Student Saved!");

            // Open Payment Form
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/paymentForm.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Payment Form");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Cannot open Payment Form!");
            }
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (isValidStudent()) {
            studentBO.updateStudent(getStudentFromFields());
            clearFields();
            loadAllStudent();
            showAlert(Alert.AlertType.INFORMATION, "Student Updated!");
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (isValidStudent()) {
            studentBO.deleteStudent(getStudentFromFields());
            clearFields();
            loadAllStudent();
            showAlert(Alert.AlertType.INFORMATION, "Student Deleted!");
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void tblStudentOnClickAction(MouseEvent event) {
        StudentTm selected = tblStudent.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtId.setText(selected.getStudentId());
            txtName.setText(selected.getName());
            txtAddress.setText(selected.getAddress());
            txtTel.setText(String.valueOf(selected.getTel()));
            registerDatePicker.setValue(selected.getRegistrationDate().toLocalDate());
            txtEmail.setText(selected.getEmail());
            cmbCourse.setValue(selected.getCourse());
            txtAmount.setText(String.valueOf(selected.getAmount()));
        }
    }

    @FXML
    void txtEmailKeyAction(KeyEvent event) {
        Regex.setTextColor(Regex.FieldType.EMAIL, txtEmail);
    }

    @FXML
    void txtSearchKeyReleased(KeyEvent event) {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<StudentTm> filteredList = FXCollections.observableArrayList();

        for (StudentTm student : tblStudent.getItems()) {
            if (student.getName().toLowerCase().contains(searchText) ||
                    student.getStudentId().toLowerCase().contains(searchText)) {
                filteredList.add(student);
            }
        }
        tblStudent.setItems(filteredList);
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtTel.clear();
        txtEmail.clear();
        txtAmount.clear();
        cmbCourse.getSelectionModel().clearSelection();
        registerDatePicker.setValue(null);
        generateStudentId();
    }

    private void generateStudentId() {
        String newId = studentBO.generateNewId();
        txtId.setText(newId);
        txtId.setEditable(false);
    }

    private boolean isValidStudent() {
        if (!Regex.setTextColor(Regex.FieldType.STUDENTID, txtId)) return false;
        if (!Regex.setTextColor(Regex.FieldType.NAME, txtName)) return false;
        if (!Regex.setTextColor(Regex.FieldType.ADDRESS, txtAddress)) return false;
        if (!Regex.setTextColor(Regex.FieldType.TEL, txtTel)) return false;
        if (!Regex.setTextColor(Regex.FieldType.EMAIL, txtEmail)) return false;
        if (txtId.getText().isEmpty() || registerDatePicker.getValue() == null) return false;
        if (txtAmount.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Amount cannot be empty!");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    public void txtIdOnAction(ActionEvent actionEvent) {}
    public void txtIdKeyAction(KeyEvent keyEvent) {}
    public void txtNameOnAction(ActionEvent actionEvent) {}
    public void txtNameKeyAction(KeyEvent keyEvent) {}
    public void txtAddressOnAction(ActionEvent actionEvent) {}
    public void txtAddressKeyAction(KeyEvent keyEvent) {}
    public void txtTelKeyAction(KeyEvent keyEvent) {
        Regex.setTextColor(Regex.FieldType.TEL, txtTel);
    }
}
