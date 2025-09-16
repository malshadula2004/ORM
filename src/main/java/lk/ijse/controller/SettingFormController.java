package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.SettingBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.util.PasswordStorage;

public class SettingFormController {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    private SettingBO settingBO = (SettingBO) BOFactory.getBO(BOFactory.BOType.SETTING);

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String username = txtUserName.getText().trim();
        String currentPassword = txtPassword.getText().trim();
        String newPassword = txtNewPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if(username.isEmpty() || currentPassword.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username and Current Password required!").show();
            return;
        }

        // Check current password
        if(!PasswordStorage.checkPassword(currentPassword, LoginFormController.userDTO.getPassword())) {
            new Alert(Alert.AlertType.ERROR, "Incorrect Current Password!").show();
            return;
        }

        // If changing password, check new password matches confirm
        String finalPassword = LoginFormController.userDTO.getPassword(); // default
        if(!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if(!newPassword.equals(confirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "New Password and Confirm Password do not match!").show();
                return;
            }
            finalPassword = PasswordStorage.hashPassword(newPassword);
        }

        try {
            UserDTO updatedUser = new UserDTO(
                    LoginFormController.userDTO.getUserId(),
                    username,
                    finalPassword,
                    LoginFormController.userDTO.getRole()
            );

            settingBO.updateUser(updatedUser);

            // Update login info
            LoginFormController.userDTO.setUserName(username);
            LoginFormController.userDTO.setPassword(finalPassword);

            txtPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();

            new Alert(Alert.AlertType.INFORMATION, "Username and Password updated successfully!").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Update failed!").show();
        }
    }
}
