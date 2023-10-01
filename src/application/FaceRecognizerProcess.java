package application;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;

import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
 
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.*;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;






/*
public class FaceRecognizer{

    private CascadeClassifier faceCascade;
    private LBPHFaceRecognizer faceRecognizer;

    public FaceRecognizer(String faceCascadePath) {
        this.faceCascade = new CascadeClassifier(faceCascadePath);
        this.faceRecognizer = LBPHFaceRecognizer.create();
    }

    public void init(String trainingDir) {
        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);
        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            
            // Extracting unique face code from the face image names
            int label = Integer.parseInt(image.getName().split("_")[0]); // Assumes filenames like "1_ABC1.jpg"
            
            images.put(counter, img);
            labelsBuf.put(counter, label);
            counter++;
        }

        // Face training
        this.faceRecognizer = LBPHFaceRecognizer.create();
        this.faceRecognizer.train(images, labels);
    }

    
    public int recognize(IplImage faceData) {
        Mat faces = cvarrToMat(faceData);
        cvtColor(faces, faces, CV_BGR2GRAY);

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(0);

        this.faceRecognizer.predict(faces, label, confidence);

        int predictedLabel = label.get(0);

        System.out.println(confidence.get(0));

        // Confidence value less than 60 means face is known
        // Confidence value greater than 60 means face is unknown
        if (confidence.get(0) > 60) {
            return -1;
        }

        return predictedLabel;
    }
}

*/



public class FaceRecognizerProcess {
	
	LBPHFaceRecognizer lbphRecognizer;
    private Map<Integer, String> labelToPersonMap;

    public FaceRecognizerProcess() {
        lbphRecognizer = createLBPHFaceRecognizer();
        labelToPersonMap = new HashMap<>();
    }

    public void trainRecognizer(Map<String, List<IplImage>> trainingData) {
        // Train the recognizer with the provided training data
        List<Mat> images = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        
        //debug
        for (Map.Entry<String, List<IplImage>> entry : trainingData.entrySet()) {
            String person = entry.getKey();
            List<IplImage> imagesDebug = entry.getValue();
            
            System.out.println("Person: " + person);
            
            for (IplImage image : imagesDebug) {
                System.out.println("Image: " + image);
            }
        }

        for (String person : trainingData.keySet()) {
        	// Extract the numeric part (e.g., "u1" becomes 1) from the uniqueCode
            int label = Integer.parseInt(person.substring(1));
            labelToPersonMap.put(label, person);

            for (IplImage image : trainingData.get(person)) {
                // Load the image in grayscale
            	// Convert the IplImage to grayscale
                IplImage grayscaleImage = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
                cvCvtColor(image, grayscaleImage, CV_BGR2GRAY);

                // Convert the grayscale IplImage to Mat
                Mat img = cvarrToMat(grayscaleImage);
                images.add(img);
                labels.add(label);
            }
        }

        MatVector imagesMat = new MatVector(images.toArray(new Mat[0]));
        Mat labelsMat = new Mat(labels.size(), 1, CV_32SC1, new IntPointer(labels.stream().mapToInt(Integer::intValue).toArray()));

        lbphRecognizer.train(imagesMat, labelsMat);
    }
  /*
    public ArrayList<String> recognizeFace(IplImage testImage) {
        ArrayList<String> resultList = new ArrayList<>(2);
        Mat testImageMat = cvarrToMat(testImage);
        cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

        // Convert Mat to UMat
        UMat testImageUMat = new UMat();
        testImageMat.copyTo(testImageUMat);

        int[] label = new int[1];
        double[] confidence = new double[1]; // Change data type to double

        lbphRecognizer.predict(testImageUMat, label, confidence);

        int predictedLabel = label[0];
        double predictedConfidence = confidence[0]; // Correct data type

        String recognizedPerson = labelToPersonMap.get(predictedLabel);
        
        System.out.println("Confidence Level: " + (100.0 - predictedConfidence)); // Ensure proper output format
        System.out.println("Predicted Label: " + predictedLabel);

        if (predictedConfidence < CONFIDENCE_THRESHOLD) {
            resultList.add(recognizedPerson);
            resultList.add(Double.toString(100.0 - predictedConfidence));
            return resultList;
        } else {
            resultList.add("unknown");
            resultList.add(Double.toString(100.0 - 1.0));
            System.out.println(resultList.get(0));
            return resultList;
        }
    }
*/    

    public ArrayList<String> recognizeFace(IplImage testImage) {
    	ArrayList<String> resultList = new ArrayList<>(2);
        Mat testImageMat = cvarrToMat(testImage);
        cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

        // Convert Mat to UMat
        UMat testImageUMat = new UMat();
        testImageMat.copyTo(testImageUMat);

        int[] label = new int[1];
        double[] confidence = new double[1];

        lbphRecognizer.predict(testImageUMat, label, confidence);

        int predictedLabel = label[0];
        int predictedConfidence = (int)confidence[0];

        String recognizedPerson = labelToPersonMap.get(predictedLabel);
        
        System.out.println("Confidence Level" + (100.0-predictedConfidence));
        System.out.println(predictedLabel);

        if (predictedConfidence < CONFIDENCE_THRESHOLD) {
        	resultList.add(recognizedPerson);
        	resultList.add(Double.toString(100.0 - predictedConfidence));
            return resultList;
        } else {
        	
        	resultList.add("unknown");
        	resultList.add(Double.toString(100.0 - 1.0));
        	System.out.println(resultList.get(0));
            return resultList;
        }
    }


    private Mat cvmatFromIplImage(IplImage iplImage) {
        return new Mat(iplImage);
    }

    // Define a confidence threshold for recognition
    private static final int CONFIDENCE_THRESHOLD = 55;
	
}



/*
public void trainRecognizer(Map<String, List<IplImage>> trainingData) {
    // Train the recognizer with the provided training data
    int label = 0;
    List<Mat> images = new ArrayList<>();
    List<Integer> labels = new ArrayList<>();

    for (String person : trainingData.keySet()) {
        label++;
        labelToPersonMap.put(label, person);

        for (IplImage image : trainingData.get(person)) {
            images.add(cvmatFromIplImage(image));
            labels.add(label);
        }
    }

    MatVector imagesMat = new MatVector(images.toArray(new Mat[0]));
    Mat labelsMat = new Mat(labels.size(), 1, CV_32SC1, new IntPointer(labels.stream().mapToInt(Integer::intValue).toArray()));

    lbphRecognizer.train(imagesMat, labelsMat);
}
*/
/*
public String recognizeFace(IplImage testImage) {
	
	Mat testImageMat = cvarrToMat(testImage);

	cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);
	
    int[] label = new int[1];
    
    double[] confidence = new double[1];

    lbphRecognizer.predict(testImageMat, label, confidence);
   
    int predictedLabel = label[0];
    double predictedConfidence = confidence[0];

    if (predictedConfidence < CONFIDENCE_THRESHOLD) {
        return labelToPersonMap.get(predictedLabel);
    } 
    else {
        return "Unknown";
    }
}
*/
/*
public String recognizeFace(IplImage testImage) {
    // Convert IplImage to Mat
    Mat testImageMat = cvarrToMat(testImage);

    // Convert Mat to grayscale
    cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);
    
    int[] label = new int[1];
    double[] confidence = new double[1];
    

    if (testImageMat.channels() != 1 || testImageMat.type() != CV_8U) {
        // Handle incorrect image format
        System.err.println("Invalid image format. Ensure it's grayscale and of type CV_8U.");
        return "Unknown";
    }

    lbphRecognizer.predict(testImageMat, label, confidence);

    int predictedLabel = label[0];
    double predictedConfidence = confidence[0];

    if (predictedConfidence < CONFIDENCE_THRESHOLD) {
        return labelToPersonMap.get(predictedLabel);
    } 
    else {
        return "Unknown";
    }
}
*/


















/*
public class FaceRecognizer {

	LBPHFaceRecognizer faceRecognizer;
 
	public File root;
	MatVector images;
	Mat labels;

	public void init() {
		// mention the directory the faces has been saved
		String trainingDir = "./faces";

		root = new File(trainingDir);

		FilenameFilter imgFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				name = name.toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
			}
		};

		File[] imageFiles = root.listFiles(imgFilter);

		this.images = new MatVector(imageFiles.length);

		this.labels = new Mat(imageFiles.length, 1, CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();

		int counter = 0;
		// reading face images from the folder

		for (File image : imageFiles) {
			Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

			// extracting unique face code from the face image names
			/*
			this unique face will be used to fetch all other information from
			I dont put face data on database.
			I just store face indexes on database.

			For example:
			When you train a new face to the system suppose person named ABC.
			Now this person named ABC has 10(can be more or less)  face image which
			will be saved in the project folder named "/Faces" using a naming convention such as
			1_ABC1.jpg
			1_ABC2.jpg
			1_ABC3.jpg
			.......
			1_ABC10.jpg
		
			The initial value of the file name is the index key in the database table of that person.
			the key 1 will be used to fetch data from database.
 
			
			int label = Integer.parseInt(image.getName().split("\\-")[0]);

			images.put(counter, img);

			labelsBuf.put(counter, label);

			counter++;
		}

		// face training
		//this.faceRecognizer = createLBPHFaceRecognizer();
		this.faceRecognizer = createLBPHFaceRecognizer();
		
		
		this.faceRecognizer.train(images, labels);

	}

	public int recognize(IplImage faceData) {

		Mat faces = cvarrToMat(faceData);

		cvtColor(faces, faces, CV_BGR2GRAY);

		IntPointer label = new IntPointer(1);
		DoublePointer confidence = new DoublePointer(0);
		
 
		this.faceRecognizer.predict(faces, label, confidence);
		
		 
		int predictedLabel = label.get(0);
			
	 
		 
		System.out.println(confidence.get(0));
		
 
	
		//Confidence value less than 60 means face is known 
		//Confidence value greater than 60 means face is unknown 
		 if(confidence.get(0) > 60)
		 {
			 //System.out.println("-1");
			 return -1;
		 }

		return predictedLabel;

	
}
*/