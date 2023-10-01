package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.FlyCapture2.ImageMetadata;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.helper.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;


import application.Database;
//import application.MotionDetector;
//import application.ColoredObjectTracker;
import application.MainViewController;
import application.FaceRecognizerProcess;

/*
public class FaceDetector implements Runnable {

	//Database database = new Database();
	//ArrayList<String> user;

	//FaceRecognizer faceRecognizer = new FaceRecognizer();
	//MotionDetector motionDetector = new MotionDetector();
	OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
	Java2DFrameConverter paintConverter = new Java2DFrameConverter();
	//ArrayList<String> output = new ArrayList<String>();

	@FXML
	public Label ll;
	private Exception exception = null;
	
	private int count = 0;
	public String classiferName;
	public File classifierFile;
	public boolean isSmile = false;
	/*
	public boolean saveFace = false;
	public boolean isRecFace = false;
	public boolean isOutput = false;
	public boolean isOcrMode = false;
	public boolean isMotion = false;
	
	
	public boolean isUpperBody = false;
	public boolean isFullBody = false;
	
	public boolean isEyeDetection = false;
	private boolean stop = false;

	private CvHaarClassifierCascade classifier = null;
	private CvHaarClassifierCascade classifierEye = null;
	private CvHaarClassifierCascade classifierSideFace = null;
	private CvHaarClassifierCascade classifierUpperBody = null;
	private CvHaarClassifierCascade classifierFullBody = null;
	private CvHaarClassifierCascade classifierSmile = null;
	private CvHaarClassifierCascade classifierEyeglass = null;
	
	
	public CvMemStorage storage = null;
	private FrameGrabber grabber = null;
	private IplImage grabbedImage = null, temp, temp2, grayImage = null, smallImage = null;
	public ImageView frames2;
	public ImageView frames;
	
	
	private CvSeq faces = null;
	private CvSeq eyes = null;
	private CvSeq smile = null;
	private CvSeq upperBody = null;
	private CvSeq sideface = null;
	private CvSeq fullBody = null;

*/
	
	public class FaceDetector implements Runnable {
		
		
		 private boolean stop = false;
		    private OpenCVFrameGrabber grabber;
		    private List<CvHaarClassifierCascade> classifiers;
		    private CvMemStorage storage;
		    private ImageView frames;
		    private String framesDirectory; // Directory to save frames
		    public static IplImage tempFrame; // Field to store the current frame
		    

		    public void init() {
		    	stop = false;
		        classifiers = new ArrayList<>();
		        setClassifier("haar/haarcascade_frontalface_alt.xml");
		        //setClassifier("haar/haarcascade_eye.xml");
		        setClassifier("haar/haarcascade_profileface.xml");
		      // setClassifier("haar/haarcascade_eye_tree_eyeglasses.xml");
		        storage = CvMemStorage.create();
		    }

	    public void start() {
	        try {
	           // stop(); // Stop and release resources before starting again
	           // Thread.sleep(30); // Add a small delay to control frame rate
	            
	            
	            grabber = OpenCVFrameGrabber.createDefault(1); // Use 1 for secondary camera
	            grabber.start();
	            
	            init();
	            

	            new Thread(this).start();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	    public void stop() {
	        stop = true;
	        try {
	            if (grabber != null) {
	                grabber.stop();
	                grabber.release();
	                grabber = null;
	            }
	            storage.release();
	            storage = null;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void setFrame(ImageView frames) {
	        this.frames = frames;
	    }
	    
	    public IplImage getCurrentFrame() {
	        return tempFrame;
	    }

	    public void run() {
	        try {
	            while (!stop) {
	                Frame grabbedFrame = grabber.grab();
	                if (grabbedFrame == null) {
	                    continue;
	                }
	                //currentFrame = grabberConverter.convertToIplImage(grabbedFrame); // Update the current frame
	                IplImage grabbedImage = grabberConverter.convertToIplImage(grabbedFrame);
	                processFrame(grabbedImage);

	                //Thread.sleep(30); // Add a small delay to control frame rate
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    /*
	    public void processFrame(IplImage frame) {
	        cvClearMemStorage(storage);

	        List<CvRect> detectedFaces = new ArrayList<>();
	        List<String> recognizedNames = new ArrayList<>(); // Store recognized names

	        IplImage resizedFrame = cvCreateImage(cvSize(frame.width() / 2, frame.height() / 2), frame.depth(), frame.nChannels());
	        cvResize(frame, resizedFrame);

	        for (CvHaarClassifierCascade classifier : classifiers) {
	            CvSeq objects = cvHaarDetectObjects(resizedFrame, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

	            if (objects != null) {
	                for (int i = 0; i < objects.total(); i++) {
	                    CvRect detectedFace = new CvRect(cvGetSeqElem(objects, i));

	                    // Convert the coordinates back to the original frame size
	                    detectedFace.x(detectedFace.x() * 2); // Assuming you scaled by 2
	                    detectedFace.y(detectedFace.y() * 2);
	                    detectedFace.width(detectedFace.width() * 2);
	                    detectedFace.height(detectedFace.height() * 2);

	                    detectedFaces.add(detectedFace);

	                    // Recognize the face and get the name
	                    String recognizedName = recognizeFace(frame, detectedFace);

	                    recognizedNames.add(recognizedName);
	                }
	            }
	        }

	        // Create a new IplImage with the same dimensions as the original frame
	        IplImage frameWithOverlay = cvCloneImage(frame);

	        // Draw ellipses and names for all the detected and recognized faces
	        for (int i = 0; i < detectedFaces.size(); i++) {
	            CvRect detectedFace = detectedFaces.get(i);
	            String recognizedName = recognizedNames.get(i);

	            CvPoint center = cvPoint(detectedFace.x() + detectedFace.width() / 2, detectedFace.y() + detectedFace.height() / 2);
	            CvSize axes = cvSize(detectedFace.width() / 2, detectedFace.height() / 2);

	            // Draw the ellipse on the new frame
	            cvEllipse(frameWithOverlay, center, axes, 0, 0, 360, CvScalar.RED, 2, CV_AA, 0);

	            // Draw the recognized name just above the ellipse
	            CvPoint textPosition = cvPoint(detectedFace.x(), detectedFace.y() - 10); // Adjust position as needed
	            cvPutText(frameWithOverlay, recognizedName, textPosition, cvFont(1.0, 1), CvScalar.RED);
	        }

	        // Display the frame with ellipses and names
	        showFrame(frameWithOverlay);
	    }
	    
	    
	    public String recognizeFace(IplImage frame, CvRect detectedFace) {
	        // Crop the detected face region from the frame
	        IplImage faceRegion = cvCreateImage(cvSize(detectedFace.width(), detectedFace.height()), frame.depth(), frame.nChannels());
	        cvSetImageROI(frame, detectedFace);
	        cvCopy(frame, faceRegion);
	        cvResetImageROI(frame);

	        // Recognize the face using your LBPH recognizer or any other recognition method
	        ArrayList<String> recognitionResult = faceRecognizer.recognizeFace(faceRegion);

	        // Extract the recognized name
	        String recognizedName = recognitionResult.get(0); // Assuming the name is the first element in the result

	        return recognizedName;
	    }
	    */
	    
	    private void processFrame(IplImage frame) {
	        cvClearMemStorage(storage);

	        CvSeq objects = new CvSeq();
	        boolean frontalFaceDetected = false;

	        // Resize the frame to a smaller size (e.g., half the original size)
	        IplImage resizedFrame = cvCreateImage(cvSize(frame.width() / 2, frame.height() / 2), frame.depth(), frame.nChannels());
	        cvResize(frame, resizedFrame);

	        for (CvHaarClassifierCascade classifier : classifiers) {
	            CvSeq faces = cvHaarDetectObjects(resizedFrame, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

	            if (faces != null) {
	                for (int i = 0; i < faces.total(); i++) {
	                    CvRect detectedFace = new CvRect(cvGetSeqElem(faces, i));

	                    // Convert the coordinates back to the original frame size
	                    detectedFace.x(detectedFace.x() * 2); // Assuming you scaled by 2
	                    detectedFace.y(detectedFace.y() * 2);
	                    detectedFace.width(detectedFace.width() * 2);
	                    detectedFace.height(detectedFace.height() * 2);

	                 // Create a copy of the original frame for drawing
	                    IplImage frameCopy = cvCloneImage(frame);
	                    tempFrame = cvCloneImage(frameCopy);
	                    cvReleaseImage(frameCopy);
	                    //frontalFaceDetected = true;
	                    
	                    // Draw a rectangle around each detected face on the original frame
	                    CvPoint center = cvPoint(detectedFace.x() + detectedFace.width() / 2, detectedFace.y() + detectedFace.height() / 2);
	                    CvSize axes = cvSize(detectedFace.width() / 2, detectedFace.height() / 2);
	                    cvEllipse(frame, center, axes, 0, 0, 360, CvScalar.RED, 2, CV_AA, 0);
	                    

	                    //frontalFaceDetected = true;

	                    // You can save the frame or perform other actions here
	                    // Uncomment the line below if you want to save the frame
	                    // saveFrame(frame, "userName");

	                    // You can break here if you want to stop after detecting any face
	                    // break;
	                    
	                }
	            }
	        }
	        
	        if (frontalFaceDetected) {
	            showFrame(frame);
	        }
	        
	        showFrame(frame);
	    }
	    /*
	    private void processFrame(IplImage frame) {
	        cvClearMemStorage(storage);
	        
	        boolean frontalFaceDetected = false;
	        CvRect detectedFace = null;

	     // Resize the frame to a smaller size (e.g., half the original size)
	        IplImage resizedFrame = cvCreateImage(cvSize(frame.width() / 2, frame.height() / 2), frame.depth(), frame.nChannels());
	        cvResize(frame, resizedFrame);
	        
	        for (CvHaarClassifierCascade classifier : classifiers) {
	            CvSeq objects = cvHaarDetectObjects(frame, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
	           // CvPoint[] pts = new CvPoints();
	            if (objects != null) {
	                for (int i = 0; i < objects.total(); i++) {
	                	detectedFace = new CvRect(cvGetSeqElem(objects, i));
	                	
	                	
	                	// Convert the coordinates back to the original frame size
	                    detectedFace.x(detectedFace.x() * 2); // Assuming you scaled by 2
	                    detectedFace.y(detectedFace.y() * 2);
	                    detectedFace.width(detectedFace.width() * 2);
	                    detectedFace.height(detectedFace.height() * 2);
	                	
	                	// Create a copy of the original frame for drawing
	                    IplImage frameCopy = cvCloneImage(frame);
	                    tempFrame = cvCloneImage(frameCopy);
	                    cvReleaseImage(frameCopy);
	                    frontalFaceDetected = true;
		                //break; // Break out of the loop after detecting any face
	                }
	                if (frontalFaceDetected) {
	                	CvPoint center = cvPoint(detectedFace.x() + detectedFace.width() / 2, detectedFace.y() + detectedFace.height() / 2);
	                    CvSize axes = cvSize(detectedFace.width() / 2, detectedFace.height() / 2);
	                    cvEllipse(frame, center, axes, 0, 0, 360, CvScalar.RED, 2, CV_AA, 0);
	                    showFrame(frame);
	                }
	                frontalFaceDetected = false;  
	                
	                showFrame(frame);
	            }
	        }
	    }
	    
	    */
	    
	    private void showFrame(IplImage frame) {
	        Frame convertedFrame = grabberConverter.convert(frame);
	        BufferedImage bufferedImage = paintConverter.getBufferedImage(convertedFrame, 1); // Use gamma 1
	        WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

	        if (frames != null) {
	            javafx.application.Platform.runLater(() -> {
	                frames.setImage(fxImage);
	            });
	        }
	    }
	    
	    public void saveFrame(IplImage frame, String userName) {
	    	try {
	            setFrameDirectory("./facesRecord/" + userName + "/");
	            


	            File directory = new File(framesDirectory);

	            if (!directory.exists()) {
	                boolean created = directory.mkdir(); // Create the directory
	                if (created) {
	                    System.out.println("Directory created successfully.");
	                } else {
	                    System.out.println("Failed to create the directory.");
	                }
	            }
	            
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
	            String fileName = framesDirectory + "frame_" + timeStamp + ".jpg";
	            
	            int result = cvSaveImage(fileName, tempFrame);
	            
	            if (result == 1) {
	                System.out.println("Frame saved successfully to: " + fileName);
	            } else {
	                System.err.println("Failed to save frame to: " + fileName);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void setFrameDirectory(String directoryName) {
	    	this.framesDirectory = directoryName;
	    	/*
	        // Get the current working directory (the project's root directory)
	        String workingDirectory = System.getProperty("user.dir");

	        // Combine the working directory with the desired directory name
	        this.framesDirectory = workingDirectory + File.separator + directoryName;
	        */
	    }

	    private void setClassifier(String name) {
	        try {
	            String classifierPath = Loader.extractResource(name, null, "classifier", ".xml").getAbsolutePath();
	            CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(cvLoad(classifierPath));
	            
	            if (classifier.isNull()) {
	                throw new Exception("Could not load the classifier file.");
	            }
	            
	            classifiers.add(classifier);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
	    private Java2DFrameConverter paintConverter = new Java2DFrameConverter();

	}
	
	/*
    public void start() {
        try {
            grabber = OpenCVFrameGrabber.createDefault(0);
            grabber.start();
            init();

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            while (!stop) {
                Frame grabbedFrame = grabber.grab();
                if (grabbedFrame != null) {
                    final IplImage grabbedImage = grabberConverter.convertToIplImage(grabbedFrame);

                    executor.execute(() -> {
                        processFrame(grabbedImage);
                    });
                }

                // Add a small delay to control frame rate
                Thread.sleep(30);
            }

            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
	int recogniseCode;
	public int code;
	public int reg;
	public int age;
	
	public String fname; //first name
	public String Lname; //last name
	public String sex; //sex
	public String name; 

	public void init() {
		//faceRecognizer.init();

		setClassifier("haar/haarcascade_frontalface_alt.xml");
		setClassifierEye("haar/haarcascade_eye.xml");
		setClassifierEyeGlass("haar/haarcascade_eye_tree_eyeglasses.xml");
		setClassifierSideFace("haar/haarcascade_profileface.xml");
		setClassifierFullBody("haar/haarcascade_fullbody.xml");
		setClassifierUpperBody("haar/haarcascade_upperbody.xml");
		setClassifierSmile("haar/haarcascade_smile.xml");

	}

	public void start() {
		try {
			new Thread(this).start();
		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}
	}

	public void run() {
		try {
			try {
				grabber = OpenCVFrameGrabber.createDefault(0); //parameter 0 default camera , 1 for secondary

				grabber.setImageWidth(300);
				grabber.setImageHeight(300);
				grabber.start();

				grabbedImage = grabberConverter.convert(grabber.grab());

				storage = CvMemStorage.create();
			} catch (Exception e) {
				if (grabber != null)
					grabber.release();
					grabber = new OpenCVFrameGrabber(0);
					grabber.setImageWidth(300);
					grabber.setImageHeight(300);
					grabber.start();
					grabbedImage = grabberConverter.convert(grabber.grab());

			}
			
			OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
			
			int count = 15;
			grayImage = cvCreateImage(cvGetSize(grabbedImage), 8, 1); //converting image to grayscale
			
			//reducing the size of the image to speed up the processing
			smallImage = cvCreateImage(cvSize(grabbedImage.width() / 4, grabbedImage.height() / 4), 8, 1); 

			stop = false;

			while (!stop && (grabbedImage = grabberConverter.convert(grabber.grab())) != null) {
				Frame frame = grabberConverter.convert(grabbedImage);
				BufferedImage image = paintConverter.getBufferedImage(frame, 2.2 / grabber.getGamma());
				Graphics2D g2 = image.createGraphics();
                    
				if (faces == null) {
					cvClearMemStorage(storage);
					
					//creating a temporary image
					temp = cvCreateImage(cvGetSize(grabbedImage), grabbedImage.depth(), grabbedImage.nChannels());

					cvCopy(grabbedImage, temp);

					cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
					cvResize(grayImage, smallImage, CV_INTER_AREA);
					
					//cvHaarDetectObjects(image, cascade, storage, scale_factor, min_neighbors, flags, min_size, max_size)
					faces = cvHaarDetectObjects(smallImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
					//face detection
					
					CvPoint org = null;
					if (grabbedImage != null) {
/*
						if (isEyeDetection) { 		eye detection logic 
							eyes = cvHaarDetectObjects(smallImage, classifierEye, storage, 1.1, 3,
									CV_HAAR_DO_CANNY_PRUNING);

							if (eyes.total() == 0) {
								eyes = cvHaarDetectObjects(smallImage, classifierEyeglass, storage, 1.1, 3,
										CV_HAAR_DO_CANNY_PRUNING);

							}

							printResult(eyes, eyes.total(), g2);

						}
						

						if (faces.total() == 0) {
							faces = cvHaarDetectObjects(smallImage, classifierSideFace, storage, 1.1, 3,
									CV_HAAR_DO_CANNY_PRUNING);

						}

						if (faces != null) {
							g2.setColor(Color.green);
							g2.setStroke(new BasicStroke(4));
							int total = faces.total();

							for (int i = 0; i < total; i++) {
								
								//printing rectange box where face detected frame by frame
								CvRect r = new CvRect(cvGetSeqElem(faces, i));
								g2.drawRect((r.x() * 4), (r.y() * 4), (r.width() * 4), (r.height() * 4));

								CvRect re = new CvRect((r.x() * 4), r.y() * 4, (r.width() * 4), r.height() * 4);

								cvSetImageROI(temp, re);


								org = new CvPoint(r.x(), r.y());
								/*
								if (isRecFace) {
									String names="Unknown Person!";
									this.recogniseCode = faceRecognizer.recognize(temp);

									//getting recognised user from the database
									
									if(recogniseCode != -1)
									{
										database.init();
										user = new ArrayList<String>();
										user = database.getUser(this.recogniseCode);
										this.output = user;

										names = user.get(1) + " " + user.get(2);
									}
								
									//printing recognised person name into the frame
									g2.setColor(Color.GREEN);
									g2.setFont(new Font("monospace", Font.BOLD, 24));
									
									g2.drawString(names, (int) (r.x() * 6.5), r.y() * 4);

								}
							}
							
							faces = null;
							}
						}
						}
						WritableImage showFrame = SwingFXUtils.toFXImage(image, null);

						javafx.application.Platform.runLater(new Runnable(){
							
							@Override
							 public void run() {
							frames.setImage(showFrame);
							 }
							 });
					}
					cvReleaseImage(temp);
				}
			}
			
		catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}
	}

	
	public void stop() {
		stop = true;

		grabbedImage = grayImage = smallImage = null;
		try {
			grabber.stop();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			
			e.printStackTrace();
		}
		try {
			grabber.release();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
		
			e.printStackTrace();
		}
		grabber = null;
	}

	
	public void printResult(CvSeq data, int total, Graphics2D g2) {
		for (int j = 0; j < total; j++) {
			CvRect eye = new CvRect(cvGetSeqElem(eyes, j));

			g2.drawOval((eye.x() * 4), (eye.y() * 4), (eye.width() * 4), (eye.height() * 4));

		}
	}
	
	
	public void setClassifier(String name) {

		try {

			setClassiferName(name);
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifier = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}
	}
	
	public void setClassifierEye(String name) {

		try {

			classiferName = name;
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierEye = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}
	

	public void setClassifierSmile(String name) {

		try {

			setClassiferName(name);
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierSmile = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}

	

	public void setClassifierSideFace(String name) {

		try {

			classiferName = name;
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierSideFace = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}

	public void setClassifierFullBody(String name) {

		try {

			setClassiferName(name);
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierFullBody = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}

	public void setClassifierEyeGlass(String name) {

		try {

			setClassiferName(name);
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierEyeglass = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}

	
	public void setClassifierUpperBody(String name) {

		try {

			classiferName = name;
			classifierFile = Loader.extractResource(classiferName, null, "classifier", ".xml");

			if (classifierFile == null || classifierFile.length() <= 0) {
				throw new IOException("Could not extract \"" + classiferName + "\" from Java resources.");
			}

			// Preload the opencv_objdetect module to work around a known bug.
			Loader.load(opencv_objdetect.class);
			classifierUpperBody = new CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
			classifierFile.delete();
			if (classifier.isNull()) {
				throw new IOException("Could not load the classifier file.");
			}

		} catch (Exception e) {
			if (exception == null) {
				exception = e;

			}
		}

	}

	
	public String getClassiferName() {
		return classiferName;
	}

	
	public void setClassiferName(String classiferName) {
		this.classiferName = classiferName;
	}

	
	public void setFrames2(ImageView frames2) {
		this.frames2 = frames2;
	}
	
	
	public void setFrame(ImageView frame) {
		this.frames = frame;
	}
	
	
	public boolean isEyeDetection() {

		return isEyeDetection;
	}
	
	
	public void setEyeDetection(boolean isEyeDetection) {
		this.isEyeDetection = isEyeDetection;
	}
	
	public void setSmile(boolean isSmile) {
		this.isSmile = isSmile;
	}
	
/*
	



	public void destroy() {
	}


	public ArrayList<String> getOutput() {
		return output;
	}

	public void clearOutput() {
		this.output.clear();
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}


	public Boolean getIsRecFace() {
		return isRecFace;
	}

	public void setIsRecFace(Boolean isRecFace) {
		this.isRecFace = isRecFace;
	}

*/
