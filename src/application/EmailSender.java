package application;

import java.io.File;
import java.util.Properties;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgcodecs;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


public class EmailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final String USERNAME = "visionguardiancommander@gmail.com";
    private static final String PASSWORD = "pglrbmlyyqdeftiz"; // This is secured!

    private static Session session;

    static {
        initialize();
    }

    public static void initialize() {
        if (session == null) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });
            session.setDebug(true);
        }
    }

    public static String sendEmail(Address[] toAddresses, String subject, String text, IplImage image) {
        try {
            Mat matImage = new Mat(image);
            String imagePath = "temp_image.jpg"; // Create a temporary image file

            // Save the IplImage as a JPEG file
            opencv_imgcodecs.imwrite(imagePath, matImage);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach the saved image file
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(new File(imagePath));
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(source.getName());
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            System.out.println("Sending...");
            Transport.send(message);
            System.out.println("Sent message successfully.");
            
            // Delete the temporary image file
            new File(imagePath).delete();

            return "Done";
        } catch (MessagingException mex) {
            System.err.println("Email sending failed.");
            mex.printStackTrace();
            return "Failed";
        }
    }
}
