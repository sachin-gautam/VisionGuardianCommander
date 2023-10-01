package application;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminViewController {

    @FXML
    private Button addNewFaceButton;
    
    @FXML
    private Button displayFacesRecordButton;

    @FXML
    private Button deleteFacesRecordButton;

    @FXML
    private Button backToMainButton;
    
    
    @FXML
    private ImageView feedWindow;
    
    @FXML
    private ImageView displayPhotoViewer;
    
    @FXML
    private ImageView updateScreenViewer;
    
    @FXML
    private ImageView deleteScreenViewer;
   


    @FXML
    private Button startCameraButton; 
    
    
    @FXML
    private Button savePhotoButton;

    @FXML
    private Button stopCameraButton;
    
    @FXML
    private Button recordFormSaveButton;
    
    @FXML
    private Button saveFromFolderButton;
    
    @FXML
    private Button takeSnapButton;
    
    @FXML
    private Button rightArrowButton;
    
    @FXML
    private Button leftArrowButton;
    
    @FXML
    private Button updateSearchButton;

    @FXML
    private Button updateFacesRecordButton;
    
    @FXML
    private Button deleteRecordButton;
    
    @FXML
    private Button deleteRejectButton;
    
    @FXML
    private Button deleteConfirmButton;
    
    @FXML
    private Button deleteSearchButton;
    
    
    
    @FXML
    private Button adminLoginCredentialsButton;
    
    @FXML
    private Button loginUserSaveButton;
    
    
    
    @FXML
    private TitledPane recordFormButton;
    
    @FXML
    private TitledPane addDisplayScreen;
    
    @FXML
    private TitledPane showDisplayScreen;
    
    @FXML
    private TitledPane updateDisplayScreen;
    
    @FXML
    private TitledPane deleteDisplayScreen;

    @FXML
    private TitledPane updateScreenRecordForm;
    
    @FXML
    private TitledPane loginUserAddButton;
    
    
    @FXML
    private TextField ageInfoField;
    
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField formLogDisplayField;
    
    @FXML
    private TextField lastNameField;

    @FXML
    private TextField registrationNumberField;
   
    @FXML
    private TextField sexInfoField;

    @FXML
    private TextField specialCodeField;
    
    
    @FXML
    private TextField updateAgeInfoField;
    
    @FXML
    private TextField updateFirstNameField;

    @FXML
    private TextField updateFormLogDisplayField;
    
    @FXML
    private TextField updateLastNameField;

    @FXML
    private TextField updateRegistrationNumberField;
   
    @FXML
    private TextField updateSexInfoField;

    @FXML
    private TextField updateSpecialCodeField;
    
    
    @FXML
    private TextField updateSearchTextField;
    
    @FXML
    private TextField deleteSearchTextField;
    
    
    @FXML
    private TextField usernameLoginField;
    
    @FXML
    private TextField passwordLoginField;
    
  

    @FXML
    private Label textTagOne;

    @FXML
    private Label textTagTwo;
    
    @FXML
    private Label textTagThree;
    
    @FXML
    private Label textTagFour;
   
    @FXML
    private Label textTagFive;

    @FXML
    private Label textTagSix;
    
    @FXML
    private Label savedLabel;
    
    
    
    @FXML
    private Label displayPhotoLabel;
    
    @FXML
    private Label displayScreenLabelName;
    
    
    @FXML
    private Label displayScreenRegistrationNo;

    @FXML
    private Label displayScreenSexInfo;
    
    @FXML
    private Label displayScreenAgeInfo;
    
    @FXML
    private Label displayScreenPhotoPath;
    
    
    
    @FXML
    private Label updateScreenLabelUniqueCode;
    
    @FXML
    private Label updateScreenLabelFullName;
    
    @FXML
    private Label updateScreenLabelRegistrationNo;
    
    @FXML
    private Label updateScreenLabelSexInfo;
    
    @FXML
    private Label updateScreenLabelAgeInfo;
    
    @FXML
    private Label updateScreenLabelPhotoPath;
    
    @FXML
    private Label updateFormLogDisplayLabel;
    
    
    @FXML
    private Label deleteScreenPhotoLabel;
    
    @FXML
    private Label deleteScreenLabelName;
    
    @FXML
    private Label deleteScreenLabelRegistrationNo;
    
    @FXML
    private Label deleteScreenLabelAgeInfo;
    
    @FXML
    private Label deleteScreenLabelSexInfo;
    
    @FXML
    private Label deleteScreenLabelPhotoPath;
    
    
    @FXML
    private Label adminLoginAddStatus;
    
    
    private int cameraOffFlag;
    private int takeSnapFlag;
    
    public String deleteUniqueCode;
    public String updateUniqueCode;

   
   
    
    FaceDetector faceDetector = new FaceDetector();
    Database userDatabase = new Database();
    MainApplication mainApplication = new MainApplication();
    List<Database> users;
	int currentIndex;

	
	
	
	void addNewFaceInit() {
		addDisplayScreen.setVisible(true);
		addDisplayScreen.setExpanded(true);
		
		startCameraButton.setVisible(true);
	    savePhotoButton.setVisible(true);
	    saveFromFolderButton.setVisible(true);
	    stopCameraButton.setVisible(true);
	    feedWindow.setVisible(true);
	    recordFormButton.setVisible(true);
		
	}
	
	void addNewFaceDisable() {
		try {
    		if(cameraOffFlag==1) {
    			faceDetector.stop();
    		}
    	}
		catch(Exception e){
			e.printStackTrace();
		}
		startCameraButton.setVisible(false);
		startCameraButton.setVisible(false);
	    savePhotoButton.setVisible(false);
	    saveFromFolderButton.setVisible(false);
	    stopCameraButton.setVisible(false);
	    feedWindow.setVisible(false);
	    recordFormButton.setVisible(false);
	    
		addDisplayScreen.setExpanded(false);
		addDisplayScreen.setVisible(false);
		
		
	}
	
	
	void displayFacesRecordInit() {
		showDisplayScreen.setVisible(true);
		showDisplayScreen.setExpanded(true);
		
		displayPhotoViewer.setVisible(true);
    	displayPhotoLabel.setVisible(true);
		//leftArrowButton.setVisible(true);
    	//rightArrowButton.setVisible(true);
    	
	}
	
	
	
	
	
	void displayFacesRecordDisable() {
		showDisplayScreen.setExpanded(false);
		showDisplayScreen.setVisible(false);
		
		displayPhotoViewer.setVisible(false);
    	displayPhotoLabel.setVisible(false);
    	
		//leftArrowButton.setVisible(false);
    	//rightArrowButton.setVisible(false);
    	
	}
	
	@FXML
	void updateSearch(ActionEvent event) {
		showSearchResultUpdate();
		updateFacesRecordInit();
		updateScreenRecordForm.setExpanded(true);
	}
	
	
	void updateFacesRecordInit() {
		updateDisplayScreen.setVisible(true);
		updateDisplayScreen.setExpanded(true);
		updateScreenRecordForm.setVisible(true);
		
		
		
    	//photoDetailsLabel1.setVisible(true);
    	//leftArrowButton.setVisible(true);
    	//rightArrowButton.setVisible(true);
    	
	}
	
	public void showSearchResultUpdate() {
		String uniqueId = updateSearchTextField.getText();
		
		System.out.println(uniqueId);
		
		Database searchUpdateQuery = new Database();
		
		searchUpdateQuery.init();
		
		Database searchUpdate = searchUpdateQuery.getUserAsDatabase(uniqueId);
		//Boolean activeStatusCheck = searchUpdate.getActiveStatus();
		System.out.println(searchUpdate.getActiveStatus());
		//if(activeStatusCheck == true) {
			updateUniqueCode = searchUpdate.getSpecialCode();
		
			Font largerFont = Font.font(updateScreenLabelUniqueCode.getFont().getFamily(), FontWeight.BOLD, 18); // You can adjust the size (16 in this example)
        
			updateScreenLabelUniqueCode.setFont(largerFont);
			updateScreenLabelFullName.setFont(largerFont);
			updateScreenLabelRegistrationNo.setFont(largerFont);
			updateScreenLabelSexInfo.setFont(largerFont);
			updateScreenLabelAgeInfo.setFont(largerFont);
			updateScreenLabelPhotoPath.setFont(largerFont);
        	// Apply the larger font to the label
        //displayPhotoLabel.setFont(largerFont);
			updateScreenLabelUniqueCode.setText(searchUpdate.getSpecialCode());
			updateScreenLabelFullName.setText(searchUpdate.getFirstName()+" "+ searchUpdate.getLastName());
			updateScreenLabelRegistrationNo.setText(Integer.toString(searchUpdate.getRegistrationId()));
			updateScreenLabelSexInfo.setText(searchUpdate.getSexInfo());
			updateScreenLabelAgeInfo.setText(Integer.toString(searchUpdate.getAgeInfo()));
       // String directoryTemp = searchDelete.getPhotoDirectoryPath();
			updateScreenLabelPhotoPath.setText("/facesRecord");
        

        // Load and display the first photo from the directory
			String photoDirectoryPath = searchUpdate.getPhotoDirectoryPath();
			File directory = new File(photoDirectoryPath);

	        if (directory.exists() && directory.isDirectory()) {
	            File[] files = directory.listFiles();
	
	            if (files != null && files.length > 0) {
	                // Assuming the first file in the directory is the photo you want to display
	                File firstPhoto = files[0];
	                Image image = new Image(firstPhoto.toURI().toString());
	                updateScreenViewer.setImage(image);
	            }
	            else {
	            	Image imageNull = new Image("file:C:/Users/iceman44/Desktop/ExoVisix-master/assets/icons/no-feed.png");
	            	updateScreenViewer.setImage(imageNull);
	            	}
	        	}
		}
	
	
	
	@FXML
    void updateFormSave(ActionEvent event) {
		
		Boolean updateFlag = updateFormInit();
		Font largerFont = Font.font(updateScreenLabelUniqueCode.getFont().getFamily(), FontWeight.BOLD, 18); // You can adjust the size (16 in this example)
        
		updateFormLogDisplayLabel.setFont(largerFont);
		if(updateFlag) {
		   
		   updateFormLogDisplayLabel.setText("Updated!");
		}
		else {
			
			updateFormLogDisplayLabel.setText("Unsuccessful!");
		}
		   
		   
		    
		

    }
	
	public Boolean updateFormInit() {
	    
	    Database updateFormRecord = new Database();
	    updateFormRecord.init();
	    
	
		if(updateFirstNameField.getText().trim().isEmpty() || updateSpecialCodeField.getText().trim().isEmpty() || updateRegistrationNumberField.getText().trim().isEmpty()) {
	    		
				updateScreenRecordForm.setExpanded(false);
				//updateFormRecord.db_close();
	    		return false;
	
		} 
	    else {
	    	
	    	
	    		
	    	try {
	    		updateFormRecord.setSpecialCode(updateSpecialCodeField.getText());
	    		updateFormRecord.setFirstName(updateFirstNameField.getText());
	    		updateFormRecord.setLastName(updateLastNameField.getText());
	    		updateFormRecord.setRegistrationId(Integer.parseInt(updateRegistrationNumberField.getText()));
	    		updateFormRecord.setAgeInfo(Integer.parseInt(updateAgeInfoField.getText()));
	    		updateFormRecord.setSexInfo(updateSexInfoField.getText());
	    		updateFormRecord.setPhotoDirectoryPath("./facesRecord/" + updateFormRecord.getSpecialCode() + "/");
	    		updateFormRecord.setActiveStatus(true);
	    		
		    	
		 
		    	updateFormRecord.update(updateFormRecord.specialCode, updateFormRecord.firstName, updateFormRecord.lastName, updateFormRecord.registrationId,
		    			updateFormRecord.ageInfo, updateFormRecord.sexInfo, updateFormRecord.photoDirectoryPath, updateFormRecord.activeStatus, updateUniqueCode);
		    	
	    	}
	    	catch(Exception e) {
	    		e.printStackTrace();
	    	}
		
	
	    	}
		updateFormRecord.db_close();
		return true;
	   }
	
	
	
	void updateFacesRecordDisable() {
		
		updateDisplayScreen.setExpanded(false);
		updateDisplayScreen.setVisible(false);
		updateScreenRecordForm.setVisible(false);
		
		//photoImageView1.setVisible(false);
    	//photoDetailsLabel1.setVisible(false);
    	//leftArrowButton.setVisible(false);
    	//rightArrowButton.setVisible(false);
    	
	}
	
	@FXML
	void searchToDelete(ActionEvent event){
		showSearchResultDelete();
		
	}
	
	public void showSearchResultDelete() {
		String uniqueId = deleteSearchTextField.getText();
		System.out.println(uniqueId);
		Database searchDeleteQuery = new Database();
		searchDeleteQuery.init();
		Database searchDelete = searchDeleteQuery.getUserAsDatabase(uniqueId);
		    
		
		if(searchDelete.getActiveStatus()) {
			deleteUniqueCode = searchDelete.getSpecialCode();
		
		Font largerFont = Font.font(deleteScreenPhotoLabel.getFont().getFamily(), FontWeight.BOLD, 18); // You can adjust the size (16 in this example)
        
		deleteScreenPhotoLabel.setFont(largerFont);
		deleteScreenLabelName.setFont(largerFont);
        deleteScreenLabelRegistrationNo.setFont(largerFont);
        deleteScreenLabelAgeInfo.setFont(largerFont);
        deleteScreenLabelSexInfo.setFont(largerFont);
        deleteScreenLabelPhotoPath.setFont(largerFont);
        // Apply the larger font to the label
        //displayPhotoLabel.setFont(largerFont);
        deleteScreenPhotoLabel.setText(searchDelete.getSpecialCode());
        deleteScreenLabelName.setText(searchDelete.getFirstName()+" "+ searchDelete.getLastName());
        deleteScreenLabelRegistrationNo.setText(Integer.toString(searchDelete.getRegistrationId()));
        deleteScreenLabelAgeInfo.setText(searchDelete.getSexInfo());
        deleteScreenLabelSexInfo.setText(searchDelete.getSexInfo());
       // String directoryTemp = searchDelete.getPhotoDirectoryPath();
        deleteScreenLabelPhotoPath.setText("/facesRecord");
        

        // Load and display the first photo from the directory
        String photoDirectoryPath = searchDelete.getPhotoDirectoryPath();
        File directory = new File(photoDirectoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                // Assuming the first file in the directory is the photo you want to display
                File firstPhoto = files[0];
                Image image = new Image(firstPhoto.toURI().toString());
                deleteScreenViewer.setImage(image);
            }
            else {
            	Image imageNull = new Image("file:C:/Users/iceman44/Desktop/ExoVisix-master/assets/icons/no-feed.png");
            	deleteScreenViewer.setImage(imageNull);
            	}
        	}
		}
	}
	
	
	@FXML
	void deleteRecord(ActionEvent event) {
		deleteConfirmButton.setVisible(true);
		deleteRejectButton.setVisible(true);
		
	}
	
	@FXML
	void deleteConfirm(ActionEvent event) {
		Database deleteConfirm = new Database();
		Database tempQuery = new Database();
		deleteConfirm.init();
		tempQuery.init();
		tempQuery = deleteConfirm.getUserAsDatabase(deleteUniqueCode);
		deleteConfirm.update(tempQuery.getSpecialCode(),tempQuery.getFirstName(),tempQuery.getLastName(),tempQuery.getRegistrationId(),tempQuery.getAgeInfo(),
				tempQuery.getSexInfo(),tempQuery.getPhotoDirectoryPath(), false, tempQuery.getSpecialCode() );
		///tempQuery.setActiveStatus(false); need to make update query of database.
		deleteConfirm.db_close();
		tempQuery.db_close();
		
	}
	
	@FXML
	void deleteReject(ActionEvent event) {
		deleteConfirmButton.setVisible(false);
		deleteRejectButton.setVisible(false);
		
	}
	
	
	void deleteFacesRecordInit() {
		
		deleteDisplayScreen.setVisible(true);
		deleteDisplayScreen.setExpanded(true);
		//deleteRecordButton.setVisible(false);
		//photoImageView1.setVisible(true);
    	//photoDetailsLabel1.setVisible(true);
    	//leftArrowButton.setVisible(true);
    	//rightArrowButton.setVisible(true);
    	
	}
	
	void deleteFacesRecordDisable() {
		
		deleteDisplayScreen.setExpanded(false);
		deleteDisplayScreen.setVisible(false);
		
		//photoImageView1.setVisible(false);
    	//photoDetailsLabel1.setVisible(false);
    	//leftArrowButton.setVisible(false);
    	//rightArrowButton.setVisible(false);
    	
	}
	
    @FXML
    void addNewFace(ActionEvent event) {
    	deleteFacesRecordDisable();
    	updateFacesRecordDisable();
    	displayFacesRecordDisable();
    	//displayFacesRecordDisable();
    	// Show/hide buttons and controls when "Add New Face" is clicked
    	addNewFaceInit();
     // Show the feedWindow (ImageView) to display the camera feed
        

    }
    
    @FXML
    void displayFacesRecord(ActionEvent event) {
    	
    	deleteFacesRecordDisable();
    	updateFacesRecordDisable();
    	addNewFaceDisable();
    	
    	displayFacesRecordInit();
    	
    	userDatabase.init();
    	users = userDatabase.getUsers();
    	currentIndex=0;
    	displayCurrentUser();
    	
    }
    
 // Handle navigation to the next user.
    @FXML
    public void leftArrowButton() {
    	previousUser();
    }
    
    @FXML
    public void rightArrowButton() {
    	nextUser();
    }
    
    public void nextUser() {
        if (currentIndex < users.size() - 1) {
            currentIndex++;
            displayCurrentUser();
        }
    }

    // Handle navigation to the previous user.
    public void previousUser() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentUser();
        }
    }
    
    
    @FXML
    public void openFile() {
    	/*
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(initialDirectory);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        */
      
    }
    
    @FXML
    public void searchUser() {
      
    }
    
    public void displayCurrentUser() {
    	
        if (currentIndex >= 0 && currentIndex < users.size()) {
        	Database user = users.get(currentIndex);
        	
        	if(user.getActiveStatus()) {
            // Display user details in your UI components (labels, images, etc.).
         // Display user details in your UserDetails label
         // Create a font with a larger size
            Font largerFont = Font.font(displayPhotoLabel.getFont().getFamily(), FontWeight.BOLD, 18); // You can adjust the size (16 in this example)
            
            displayPhotoLabel.setFont(largerFont);
            displayScreenLabelName.setFont(largerFont);
            displayScreenRegistrationNo.setFont(largerFont);
            displayScreenSexInfo.setFont(largerFont);
            displayScreenAgeInfo.setFont(largerFont);
            displayScreenPhotoPath.setFont(largerFont);
            // Apply the larger font to the label
            //displayPhotoLabel.setFont(largerFont);
            displayPhotoLabel.setText(user.getSpecialCode());
            displayScreenLabelName.setText(user.getFirstName()+" "+ user.getLastName());
            displayScreenRegistrationNo.setText(Integer.toString(user.getRegistrationId()));
            displayScreenAgeInfo.setText(Integer.toString(user.getAgeInfo()));
            displayScreenSexInfo.setText(user.getSexInfo());
           // String directoryTemp = user.getPhotoDirectoryPath();
            displayScreenPhotoPath.setText("/facesRecord");
            

            // Load and display the first photo from the directory
            String photoDirectoryPath = user.getPhotoDirectoryPath();
            File directory = new File(photoDirectoryPath);

            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null && files.length > 0) {
                    // Assuming the first file in the directory is the photo you want to display
                    File firstPhoto = files[0];
                    Image image = new Image(firstPhoto.toURI().toString());
                    displayPhotoViewer.setImage(image);
                	}
                else {
                	Image imageNull = new Image("file:C:/Users/iceman44/Desktop/ExoVisix-master/assets/icons/no-feed.png");
                	displayPhotoViewer.setImage(imageNull);
                }
            	}
        	}
        }
    
    }

    @FXML
    void backToMain(ActionEvent event) {
    	try {
    		addNewFaceDisable();
        	displayFacesRecordDisable();
        	deleteFacesRecordDisable();
        	updateFacesRecordDisable();
        	
    		userDatabase.db_close();
    		mainApplication.showMainView();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

    }

    @FXML
    void deleteFacesRecord(ActionEvent event) {
    	addNewFaceDisable();
    	displayFacesRecordDisable();
    	updateFacesRecordDisable();
    	
    	deleteFacesRecordInit();

    }
    
    @FXML
    void takeSnap(ActionEvent event) {
    	
    	IplImage currentFrame = faceDetector.getCurrentFrame();
    	
        // Check if the current frame is not null
        if (currentFrame != null) {
            // Call saveFrame to save the current frame
            faceDetector.saveFrame(currentFrame, userDatabase.specialCode);
        } 
        else {
            // Handle the case when there is no current frame to save
            System.out.println("No frame to save.");
        }

    }
    
    @FXML
    void openFolder(ActionEvent event) {
    	if(!recordFormButton.isExpanded()) {
    		recordFormButton.setExpanded(true);
    	}
    	takeSnapFlag=0;
    	userDatabase.init();
    	// Create a JavaFX FileChooser
        
        
        FileChooser fileChooser = new FileChooser();
        
        // Set initial directory (optional)
        //fileChooser.setInitialDirectory(new File(System.getProperty("C:/Users/iceman44/Desktop/ExoVisix-master/facesRecord/")));
        
     // Set the initial directory to your desired path
        File initialDirectory = new File(".\\facesRecord");
        fileChooser.setInitialDirectory(initialDirectory);
        
        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        
        // Check if a file was selected
        if (selectedFile != null) {
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("...");
        }

    }

    @FXML
    void beginSave(ActionEvent event) {
    	
    	
    	
    	if (userDatabase.init()){
    		
    		takeSnapFlag = 1;
    		
    		recordFormButton.setExpanded(true);
    		return;

		} 
    	else {
    		
    		recordFormButton.setExpanded(false);
    	
    	}
        

    }


    @FXML
    void startCamera(ActionEvent event) {
    	faceDetector.setFrame(feedWindow);
    	faceDetector.start();
    	cameraOffFlag=1;
    	// Run UI update on the JavaFX Application Thread
        Platform.runLater(() -> {
            feedWindow.setVisible(true);
        });

    }

    @FXML
    void stopCamera(ActionEvent event) {
    	cameraOffFlag=0;
    	faceDetector.stop();

    }

    

    @FXML
    void updateFacesRecord(ActionEvent event) {
    	
    	addNewFaceDisable();
    	displayFacesRecordDisable();
    	deleteFacesRecordDisable();
    	
    	
    	updateFacesRecordInit();
    	
    	

    }
    
    @FXML
    void addLoginInfo(ActionEvent event) {
    	displayFacesRecordDisable();
    	addNewFaceDisable();
    	updateFacesRecordDisable();
    	deleteFacesRecordDisable();
    	
    	loginUserAddButton.setVisible(true);
    	loginUserAddButton.setExpanded(true);
    	
    }
    
    @FXML
    void  userLoginAdd(ActionEvent event) {
    	
    	
    	if(usernameLoginField.getText().trim().isEmpty() || passwordLoginField.getText().trim().isEmpty()) {
    		
    		loginUserAddButton.setExpanded(false);
    		loginUserAddButton.setVisible(false);
    		return;

    	}
    	else {
    		
	    	try {
		    	String username = usernameLoginField.getText();
		    	String password = passwordLoginField.getText();
		    	DatabaseAdminLogin newUser = new DatabaseAdminLogin();
		    	newUser.init();
		    	newUser.insertUser(username, password);
		    	newUser.db_close();
	    		}
	    	catch(Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    	Font largerFont = Font.font(adminLoginAddStatus.getFont().getFamily(), FontWeight.BOLD, 16); // You can adjust the size (16 in this example)
        
    	adminLoginAddStatus.setFont(largerFont);
    	adminLoginAddStatus.setText("User Added!");
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	loginUserAddButton.setExpanded(false);
    	loginUserAddButton.setVisible(false);
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @FXML
    void recordFormSave(ActionEvent event) {
    	
			if(firstNameField.getText().trim().isEmpty() || specialCodeField.getText().trim().isEmpty() || registrationNumberField.getText().trim().isEmpty()) {
		    		
		    		recordFormButton.setExpanded(false);
		    		//recordFormButton.setVisible(false);
		    		return;
		
			} 
		    else {
		    		
		    	try {
			    	userDatabase.setSpecialCode(specialCodeField.getText());
			    	userDatabase.setFirstName(firstNameField.getText());
			    	userDatabase.setLastName(lastNameField.getText());
			    	userDatabase.setRegistrationId(Integer.parseInt(registrationNumberField.getText()));
			    	userDatabase.setAgeInfo(Integer.parseInt(ageInfoField.getText()));
			    	userDatabase.setSexInfo(sexInfoField.getText());
			    	userDatabase.setPhotoDirectoryPath("./facesRecord/" + userDatabase.specialCode + "/");
			    	userDatabase.setActiveStatus(true);
			    	
			    	String userCode = userDatabase.checkUserCode(userDatabase.specialCode);
			    	if(userDatabase.specialCode.equalsIgnoreCase(userCode)){
			    		System.out.println("User Already Exists!");
			    		takeSnapButton.setVisible(false);
			    		takeSnapFlag=0;
			    		return;
			    	}
			    	else {
			    	userDatabase.insert(userDatabase.specialCode, userDatabase.firstName, userDatabase.lastName, userDatabase.registrationId,
			    			userDatabase.ageInfo, userDatabase.sexInfo, userDatabase.photoDirectoryPath, userDatabase.activeStatus);
			    	}
		    	}
		    	catch(Exception e) {
		    		e.printStackTrace();
		    	}
    		
    	
    	}
			if(takeSnapFlag==1) {
				takeSnapButton.setVisible(true);
			}
			takeSnapFlag=0;
		

    }

    

    @FXML
    void formLogDisplay(ActionEvent event) {

    }

}
