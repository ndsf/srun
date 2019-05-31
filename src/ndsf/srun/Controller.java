package ndsf.srun;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    private static final long PERIOD = 1000 * 60 * 30;

    Timer timer;
    TimerTask timerTask;

    // settings from file
    // private Settings settings = new Settings();

    public void init() {
        // fill the username field
        usernameField.setText(Settings.getInstance().getUsername());

        // fill the checkboxes
        rememberPasswordCheckBox.setSelected(Settings.getInstance().isRememberPassword());
        autoConnectCheckBox.setSelected(Settings.getInstance().isAutoConnect());

        // fill the password field if remembering password is enabled
        if (Settings.getInstance().isRememberPassword())
            passwordField.setText(Settings.getInstance().getPassword());

        // login if auto connect is enabled
        if (Settings.getInstance().isAutoConnect())
            handleLoginButtonAction(null);
    }

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

    public JFXTextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(JFXTextField usernameField) {
        this.usernameField = usernameField;
    }

    public JFXPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JFXPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JFXCheckBox getRememberPasswordCheckBox() {
        return rememberPasswordCheckBox;
    }

    public void setRememberPasswordCheckBox(JFXCheckBox rememberPasswordCheckBox) {
        this.rememberPasswordCheckBox = rememberPasswordCheckBox;
    }

    public JFXCheckBox getAutoConnectCheckBox() {
        return autoConnectCheckBox;
    }

    public void setAutoConnectCheckBox(JFXCheckBox autoConnectCheckBox) {
        this.autoConnectCheckBox = autoConnectCheckBox;
    }

    public JFXButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JFXButton loginButton) {
        this.loginButton = loginButton;
    }

    public JFXButton getLogoutButton() {
        return logoutButton;
    }

    public void setLogoutButton(JFXButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    public JFXTextArea getLogArea() {
        return logArea;
    }

    public void setLogArea(JFXTextArea logArea) {
        this.logArea = logArea;
    }

    public Label getBillingNameLabel() {
        return billingNameLabel;
    }

    public void setBillingNameLabel(Label billingNameLabel) {
        this.billingNameLabel = billingNameLabel;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Label getRemainBytesLabel() {
        return remainBytesLabel;
    }

    public void setRemainBytesLabel(Label remainBytesLabel) {
        this.remainBytesLabel = remainBytesLabel;
    }

    public Label getAllBytesLabel() {
        return allBytesLabel;
    }

    public void setAllBytesLabel(Label allBytesLabel) {
        this.allBytesLabel = allBytesLabel;
    }

    @FXML
    private Label allBytesLabel;

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        //Window owner = loginButton.getScene().getWindow();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            return;
        }

        login(username, password);

        // login every 30 minutes
        timer = new Timer(true);
        timerTask = new LoginTimerTask(this);
        timer.scheduleAtFixedRate(timerTask, 0, PERIOD);
    }

    public void login(String username, String password) {
        // remember username
        Settings.getInstance().setUsername(usernameField.getText());

        // remember password if it's enabled
        if (rememberPasswordCheckBox.isSelected())
            Settings.getInstance().setPassword(passwordField.getText());

        // disable the fields
        usernameField.setDisable(true);
        passwordField.setDisable(true);

        // disable login button, and enable logout button
        loginButton.setDisable(true);
        logoutButton.setDisable(false);

        try {
            Map<String, String> map = Srun.login(username, password);

            if (map == null) {
                billingNameLabel.setText("登录失败");
                return;
            }

            // update informations

            billingNameLabel.setText(map.get("billing_name"));
            remainBytesLabel.setText("剩余 " + map.get("acount_remain_bytes"));
            allBytesLabel.setText("已用 " + map.get("acount_used_bytes") + " / " + map.get("acount_all_bytes"));

            // get percentage

            String remainBytesString = map.get("acount_remain_bytes");
            remainBytesString = remainBytesString.substring(0, remainBytesString.length() - 1);
            double remainBytes = Double.parseDouble(remainBytesString);

            String allBytesString = map.get("acount_all_bytes");
            allBytesString = allBytesString.substring(0, allBytesString.length() - 1);
            double allBytes = Double.parseDouble(allBytesString);

            if (allBytes != 0) {
                double percentage = remainBytes / allBytes;
                if (percentage >= 0 && percentage <= 1) {
                    progressBar.setProgress(percentage);
                }
            }

            log("Login as " + usernameField.getText() + ".");
        } catch (IOException ex) {
            log("Errored!");
        }
    }

    @FXML
    protected void handleLogoutButtonAction(ActionEvent event) {
        //Window owner = logoutButton.getScene().getWindow();
        String username = usernameField.getText();
        if (username.isEmpty()) {
            return;
        }
        logout(username);

        // destroy the timer
        timer.cancel();
        timer.purge();
        timer = null;

        // cancle the task
        timerTask.cancel();
        timerTask = null;
    }

    @FXML
    protected void handleRememberPasswordCheckBox(ActionEvent event) {
        setRememberPassword(rememberPasswordCheckBox.isSelected());
        /*if (rememberPasswordCheckBox.isSelected()) {
            System.out.println("Remember password");
            settings.setRememberPassword(true);
        } else {
            System.out.println("Don't remember password");
            settings.setRememberPassword(false);
        }*/
    }

    @FXML
    protected void handleAutoConnectCheckBox(ActionEvent event) {
        setAutoConnect(autoConnectCheckBox.isSelected());
        /*if (autoConnectCheckBox.isSelected()) {
            System.out.println("Auto connect");
            settings.setRememberPassword(true);
            settings.setAutoConnect(true);


        } else {
            System.out.println("Don't auto connect");
            settings.setAutoConnect(false);
        }*/
    }

    private void setRememberPassword(boolean option) {
        // refuse if auto connect is enabled

        if (Settings.getInstance().isAutoConnect() && option)
            return;

        // enable remember password in settings
        Settings.getInstance().setRememberPassword(option);
    }

    private void setAutoConnect(boolean option) {
        // enable auto connect in settings

        Settings.getInstance().setAutoConnect(option);

        if (option) {
            // also set remember password if option is true

            Settings.getInstance().setRememberPassword(true);
            rememberPasswordCheckBox.setSelected(true);
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
        log("Logout as " + username + ".");
    }

    private void log(String s) {
        logArea.appendText("\n" + s);
    }
}