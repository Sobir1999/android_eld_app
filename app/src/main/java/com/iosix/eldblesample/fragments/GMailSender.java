package com.iosix.eldblesample.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMailSender {
    static {
        Security.addProvider(new com.iosix.eldblesample.fragments.JSSEProvider());
    }

    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private String subject;
    private String body;
    private String sender;
    private String recipients;
    private String filename;
    ProgressDialog progressDialog;

    public GMailSender(String user, String password, String subject, String body, String sender, String
            recipients, String filename) {
        this.user = user;
        this.password = password;
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipients = recipients;
        this.filename = filename;





        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);

                    }
                });


        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);





            message.setContent(multipart);
            Transport.send(message);
            Log.d("Sendss", "sendMail: $message");
            System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            Log.d("SendMail", "doInBackground: "+e.getMessage());
        }

    }


}

