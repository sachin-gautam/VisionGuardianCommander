package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import org.bytedeco.javacpp.opencv_core.IplImage;

import jakarta.mail.Address;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import org.bytedeco.javacpp.opencv_core.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainViewController {

    @FXML
    private Button adminPanelButton;

    @FXML
    private Button monitorButton;

    @FXML
    private Button startCameraButton;

    @FXML
    private Button stopCameraButton;
    
    @FXML
    private ImageView feedWindow;
    
    @FXML
    private ImageView photoDetail;
    
    @FXML
    public Label userDetailsLabel;
    
    @FXML
    private TitledPane dataPanel;
    
    @FXML
	public ListView<String> logList;
    
    
    public static ObservableList<String> event = FXCollections.observableArrayList();
    

    public Boolean stopThread;
    
    
    public Boolean startCameraFlag;
    
    
    public ArrayList<String> recognizedPerson = new ArrayList<>(2);

   // EmailSender mail = new EmailSender();
    FaceDetector faceDetect = new FaceDetector();	//Creating Face detector object	
    public Database userDatabase = new Database();
    public Database userDetail = new Database();
	FaceRecognizerProcess faceRecognizer = new FaceRecognizerProcess();
	Map<String, List<IplImage>> trainingData = new HashMap<>();
	ArrayList<String> uniqueCodes = new ArrayList<>();
	List<IplImage> userImages = new ArrayList<IplImage>();
	
	
    ImageView imageView1;
    
	public MainViewController() {
		trainingInit();
		faceRecognizer.trainRecognizer(trainingData); // Provide training data here
		
	}
    
    
    
    
    
    @FXML
    void startAdminPanel(ActionEvent event) throws Exception {
    	FXMLLoader adminLoader = new FXMLLoader(MainApplication.class.getResource("adminView.fxml"));
    	MainApplication.mainViewRoot = adminLoader.load();
       // BorderPane mainPane = mainLoader.load();

        // Get the MainViewController instance and set any necessary references
    	MainApplication.adminController = adminLoader.getController();
        // You might need to pass some data or perform setup in mainController

        Scene scene = MainApplication.primaryStage.getScene();
        scene.setRoot(MainApplication.mainViewRoot);
        
        //changeSceneWithFadeInTransition(mainViewRoot);
        
        MainApplication.primaryStage.setTitle("Admin View"); // Set title for the new scene
        //primaryStage.show();

    }
    
    void trainingInit() {
    	userDatabase.init();
    	//putOnLog("Training Data Initialized...");
    	List<Database> users = userDatabase.getUsers(); // Assuming this method retrieves user data from the database
    	
    	
    	
    	for (Database user : users) {
    	    String uniqueCode = user.getSpecialCode();
    	    String imagePath = user.getPhotoDirectoryPath(); // Assuming this path points to the directory with user photos


    	 // Load all images from the folderPath (use loadImagesFromFolder for multiple images)
    	    List<IplImage> image = loadImagesFromFolder(imagePath);

    	    if (image != null) {
    	    	System.out.println(uniqueCode);
    	        uniqueCodes.add(uniqueCode);
    	        System.out.println(image);
    	        userImages.addAll(image);
    	    }
    	}
    	
    	//Map<String, List<IplImage>> trainingData = new HashMap<>();
    	for (int i = 0; i < uniqueCodes.size(); i++) {
    	    String uniqueCode = uniqueCodes.get(i);
    	    IplImage image = userImages.get(i);

    	    if (!trainingData.containsKey(uniqueCode)) {
    	        trainingData.put(uniqueCode, new ArrayList<>());
    	    }

    	    trainingData.get(uniqueCode).add(image);
    	}
    }
/*
    public IplImage loadImage(String imagePath) {
        try {
            // Load the image using OpenCV
            Mat mat = imread(imagePath);

            // Check if the image was loaded successfully
            if (mat != null && !mat.empty()) {
                // Convert the Mat object to an IplImage
                IplImage iplImage = new IplImage(mat);

                return iplImage;
            } else {
                System.err.println("Failed to load image: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if image loading fails
    }
    */
    
    public IplImage loadImage(String imagePath) {
        try {
            // Load the image using OpenCV in JavaCV
            Mat mat = org.bytedeco.javacpp.opencv_imgcodecs.imread(imagePath);

            // Check if the image was loaded successfully
            if (mat != null && !mat.empty()) {
                // Convert the Mat object to an IplImage
                IplImage iplImage = new IplImage(mat);

                return iplImage;
            } else {
                System.err.println("Failed to load image: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if image loading fails
    }
    
    public List<IplImage> loadImagesFromFolder(String folderPath) {
        List<IplImage> images = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file.getName())) {
                    String imagePath = file.getAbsolutePath();
                    IplImage image = loadImage(imagePath);
                    if (image != null) {
                        images.add(image);
                    }
                }
            }
        }

        return images;
    }
    
    private boolean isImageFile(String fileName) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "bmp", "gif"};
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return Arrays.asList(imageExtensions).contains(fileExtension);
    }

    
    @FXML
    void startCamera(ActionEvent event) {
    	//initializing objects from start camera button event
    			//faceDetect.stop();
    			//userDetail.init();
    			//trainingInit();
    			//faceRecognizer.trainRecognizer(trainingData); 
    			
    			
    			faceDetect.setFrame(feedWindow);

    			faceDetect.start();
    			startCameraFlag=true;
    			startCameraButton.setDisable(true);
    			putOnLog(" Real Time Camera Feed Started...");

    }
    
    
    void monitorPhase() {
    	putOnLog("Monitoring for Recognized faces now...");
        new Thread(() -> {
            while (!stopThread) {
                try {
                    recognizedPerson = faceRecognizer.recognizeFace(faceDetect.tempFrame);
                    System.out.println("Recognized Person: " + recognizedPerson.get(0));
                    System.out.println("Confidence Level: " + recognizedPerson.get(1));
                    displayFacesRecord();
                    //databaseClear();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        }).start();
    }
  
    void databaseClear() {
    	userDetail.setSpecialCode("NA");
    	userDetail.setFirstName("Unknown");
    	userDetail.setLastName("Identity");
    	userDetail.setRegistrationId(0);
    	userDetail.setAgeInfo(0);
    	userDetail.setSexInfo("NA");
    	userDetail.setPhotoDirectoryPath("NA");
    	
    }
    
    void displayFacesRecord() {
    		
   
    	    System.out.println(recognizedPerson.get(0));
    		 //System.out.println(userDetail);
    		 //Label userDetailsLabel = new Label();
    		if(recognizedPerson.get(0).equalsIgnoreCase("unknown")) {
    			Font largerFont = Font.font(userDetailsLabel.getFont().getFamily(), FontWeight.BOLD, 12); // You can adjust the size (16 in this example)
    			
				 javafx.application.Platform.runLater(() -> {
				        // Apply the larger font to the label and set its text
				        userDetailsLabel.setFont(largerFont);
				        userDetailsLabel.setText("Name: Unknown Identity"
				                + "\nRegistration ID: NA"
				                + "\nAge: NA"
				                + "\nSex: NA"
				                + "\nConfidence Level: " + recognizedPerson.get(1));
			            });
				 
				 Image nullImage = new Image("file:C:/Users/iceman44/Desktop/ExoVisix-master/assets/icons/no-feed.png");
	            	javafx.application.Platform.runLater(() -> {
		                photoDetail.setImage(nullImage);
		                });
	            	putOnLog("Face Recognition unsuccessful...");
	            	return;
	            	
    			}
    			userDetail = userDatabase.getUserAsDatabase(recognizedPerson.get(0));
				 Font largerFont = Font.font(userDetailsLabel.getFont().getFamily(), FontWeight.BOLD, 12); // You can adjust the size (16 in this example)
	
				 javafx.application.Platform.runLater(() -> {
				        // Apply the larger font to the label and set its text
				        userDetailsLabel.setFont(largerFont);
				        userDetailsLabel.setText("Name: " + userDetail.getFirstName() + " " + userDetail.getLastName()
				                + "\nRegistration ID: " + userDetail.getRegistrationId()
				                + "\nAge: " + userDetail.getAgeInfo()
				                + "\nSex: " + userDetail.getSexInfo()
				                + "\nConfidence Level: " + recognizedPerson.get(1));
			            });
			        // Load and display the first photo from the directory
			        String photoDirectoryPath = userDetail.getPhotoDirectoryPath();
			        File directory = new File(photoDirectoryPath);

			        if (directory.exists() && directory.isDirectory()) {
			            File[] files = directory.listFiles();

			            if (files != null && files.length > 0) {
			                // Assuming the first file in the directory is the photo you want to display
			                File firstPhoto = files[0];
			                Image image = new Image(firstPhoto.toURI().toString());
			                javafx.application.Platform.runLater(() -> {
			                photoDetail.setImage(image);
			                });
			                Instant now = Instant.now();
			                String newData = "Face of "+userDetail.getFirstName()+" "+ userDetail.getLastName()+" has been recognized...";
			        		String eventNow = now.toString() + ":\n" + newData;
			        		putOnLog(newData);
			        		// Create an array of recipients (you can add multiple recipients)
			        		try {
				                Address[] toAddresses = InternetAddress.parse("gautamsachin757@gmail.com");
				                EmailSender.initialize();
				                String result = EmailSender.sendEmail(toAddresses, "Guardian Alert!", eventNow, FaceDetector.tempFrame);
				                putOnLog("Email sent Successful = "+result);
				                Thread.sleep(2000);
			        		} 
			        		catch (Exception e) {
			        			e.printStackTrace();
			        		}
			            }
			            
			        }
			        
			    
    	
    }

    @FXML
    void startMonitor(ActionEvent event) {
    	userDetail.init();
    	stopThread = false;
    	if(startCameraFlag==true) {
    		monitorPhase();
    	}
    	userDetail.init();
    	
    }

    	
    void stopCameraInit() {
    	stopThread = true;
    	Image originalImage = new Image("file:C:/Users/iceman44/Desktop/ExoVisix-master/assets/icons/no-feed.png");
		feedWindow.setImage(originalImage);
    	faceDetect.stop();
    	
		startCameraButton.setVisible(true);
		stopCameraButton.setVisible(true);
  
    
    }
    
    
    @FXML
    void stopCamera(ActionEvent event) {
    	stopCameraInit();
    	startCameraFlag = false;
		startCameraButton.setDisable(false);
		//feedWindow.setVisible(false);
		

		/* this.saveFace=true; */

		putOnLog("Camera Feed Stopped...");

		

    }

   
    /*
    private ImageView createImageView(final File imageFile) {

		try {
			final Image img = new Image(new FileInputStream(imageFile), 120, 0, true, true);
			imageView1 = new ImageView(img);

			imageView1.setStyle("-fx-background-color: BLACK");
			imageView1.setFitHeight(120);

			imageView1.setPreserveRatio(true);
			imageView1.setSmooth(true);
			imageView1.setCache(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return imageView1;
	}
    */

    public void putOnLog(String data) {

		Instant now = Instant.now();

		String logs = now.toString() + ":\n" + data;

		event.add(logs);

		logList.setItems(event);

	}

}
