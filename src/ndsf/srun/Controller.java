package ndsf.srun;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.Map;

public class Controller {
    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXCheckBox rememberPasswordCheckBox;

    @FXML
    private JFXCheckBox autoConnectCheckBox;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXTextArea logArea;

    @FXML
    private Label billingNameLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label remainBytesLabel;

    @FXML
    private Label allBytesLabel;

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        //Window owner = loginButton.getScene().getWindow();
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty()) {
            // show alert
            log("username is empty");
            return;
        }
        if (password.isEmpty()) {
            // show alert
            log("password is empty");
            return;
        }

        login(username, password);
    }

    private void login(String username, String password) {
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        loginButton.setDisable(true);
        logoutButton.setDisable(false);
        try {
            Map<String, String> map = Srun.login(username, password);
            // update informations
            billingNameLabel.setText(map.get("billing_name"));
            remainBytesLabel.setText("剩余 " + map.get("acount_remain_bytes"));
            allBytesLabel.setText("已用 " + map.get("account_used_bytes" + "acount_all_bytes"));
            log("welcome" + usernameField.getText() + " " + passwordField.getText());
        }
        catch (IOException ex) {
            log("Errored!");
        }
    }

    @FXML
    protected void handleLogoutButtonAction(ActionEvent event) {
        //Window owner = logoutButton.getScene().getWindow();
        String username = usernameField.getText();
        if (username.isEmpty()) {
            // show alert
            log("username is empty");
            return;
        }
        logout(username);
    }

    @FXML
    protected void handleRememberPasswordCheckBox(ActionEvent event) {
        if (rememberPasswordCheckBox.isSelected()) {
            System.out.println("Remember password");
        }
        else {
            System.out.println("Don't remember password");
        }
    }

    @FXML
    protected void handleAutoConnectCheckBox(ActionEvent event) {
        if (autoConnectCheckBox.isSelected()) {
            System.out.println("Auto connect");
        }
        else {
            System.out.println("Don't auto connect");
        }
    }

    private void logout(String username) {
        usernameField.setDisable(false);
        passwordField.setDisable(false);
        loginButton.setDisable(false);
        logoutButton.setDisable(true);
        try {
            Srun.logout(username);
        } catch (IOException ex) {
            log("Errored!");
        }
        log("Bye" + username);
    }

    protected void log(String s) {
        logArea.appendText("\n" + s);
    }
}
