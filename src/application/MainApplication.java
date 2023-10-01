package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.Label;

import javafx.animation.FadeTransition;

import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import javafx.scene.media.MediaView;



public class MainApplication extends Application {
	public static Stage primaryStage;
	
    private LoginSceneController loginController;
    
    public static MainViewController mainController;
    
    public static AdminViewController adminController;
    
    public static Parent mainViewRoot;

	
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        showLoginView();
    }
  
    
	public void showLoginView() throws Exception {
        primaryStage.setTitle("Visual Guardian Commander");

        FXMLLoader loginLoader = new FXMLLoader(MainApplication.class.getResource("loginScene.fxml"));
        StackPane stackPane = loginLoader.load();

        // Get the LoginViewController instance and set the mainApplication reference
        loginController = loginLoader.getController();
        loginController.setMainApplication(this);

        StackPane root = new StackPane();


        root.getChildren().add(stackPane); // Add gridPane as a child of root

        Scene scene = new Scene(root, 1440, 750);
       
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public void showMainView() throws Exception {
	
        FXMLLoader mainLoader = new FXMLLoader(MainApplication.class.getResource("mainView.fxml"));
        mainViewRoot = mainLoader.load();
       // BorderPane mainPane = mainLoader.load();

        // Get the MainViewController instance and set any necessary references
        mainController = mainLoader.getController();
        // You might need to pass some data or perform setup in mainController

        Scene scene = primaryStage.getScene();
        //scene.getStylesheets().add(getClass().getResource("button.css").toExternalForm());
       // scene.getStylesheets().add("button.css"); // Add the CSS file
        scene.setRoot(mainViewRoot);
        
        //changeSceneWithFadeInTransition(mainViewRoot);
        
        primaryStage.setTitle("Main View Title"); // Set title for the new scene
        //primaryStage.show();
    
	
	   
	   // primaryStage.getIcons().add(new Image("resources/assets/images/logo.png"));
	}
	
	
	// for scene transition effects.
	
	

	public static void main(String[] args) {
		launch(args);
		
		
	}
}
