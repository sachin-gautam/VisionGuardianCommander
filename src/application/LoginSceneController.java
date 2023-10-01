package application;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import application.DatabaseAdminLogin;

public class LoginSceneController {
	
    private MainApplication mainApplication;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private VBox errorMessageBox;
    
    DatabaseAdminLogin checkLogin = new DatabaseAdminLogin();
    

    public void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (validateLogin(username, password)) {
            showMainInterface();
        } else {
            showErrorMessageBox("Invalid username or password. \nPlease try again.");
            //handleLogin();
            //showErrorMessageBox(String message);
        }
    }
    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password)) {
                showMainInterface();
            } else {
                showErrorMessageBox("Invalid username or password. \nPlease try again.");
                //handleLogin();
               // showErrorMessageBox(String message);
                
            }
        }
    }

    @FXML
    private void handleCancel() {
        // Close the application or perform any other necessary action
        usernameField.getScene().getWindow().hide();
    }

    private boolean validateLogin(String username, String password) {
    	checkLogin.init();
    	DatabaseAdminLogin tempCheck = new DatabaseAdminLogin();
    	
    	tempCheck = checkLogin.getUserLoginDetails(username, password);
        // Perform your login validation logic here
        // Return true if the login is successful, false otherwise
        // You can replace this with your own implementation
    	checkLogin.db_close();
        return username.equals(tempCheck.getUsername()) && password.equals(tempCheck.getPassword());
    }
    
    private void showErrorMessageBox(String message) {
        // Clear existing error messages
        errorMessageBox.getChildren().clear();

        Label errorMessageLabel = new Label(message);
        errorMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");

        Button dismissButton = new Button("Dismiss");
        dismissButton.getStyleClass().add("btn"); // Add the "btn" style class to dismissButton
        dismissButton.setPrefWidth(150); // Set the preferred width to 150 pixels
        dismissButton.setOnAction(event -> {
            errorMessageBox.getChildren().clear(); // Clear the error messages on dismiss
        });


        errorMessageBox.getChildren().addAll(errorMessageLabel, dismissButton);
    }


    


    private void showMainInterface() {
        try {
        	//System.out.println("Potato");
            mainApplication.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}