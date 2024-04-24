package com.scaler.EcomNotificationService.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.EcomNotificationService.dto.SendEmailDto;
import com.scaler.EcomNotificationService.services.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import java.util.Properties;

@Service
public class SendEmailConsumer {
    private ObjectMapper objectMapper;
    private EmailService emailService;

    public SendEmailConsumer(ObjectMapper objectMapper, EmailService emailService) {
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    //This method should be called if we receive an event for sending an email(SignUp)
    //This method/consumer should register itself to the signUp topic.
    @KafkaListener(topics = "signUp" , groupId = "emailService")
    public void handleSignUpEvent(String message) throws JsonProcessingException {
        //We are getting string message
        //Convert this string message into object using ObjectMapper
        System.out.println("Message Consumed from Kafka Topic");
        SendEmailDto sendEmailDto = objectMapper.readValue(message , SendEmailDto.class);

        String smtpHostServer = "smtp.gmail.com";


        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHostServer);
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendEmailDto.getFrom(), "lljz bmko rtmz aahz");
                // The Password used above here is the App Password of the "from email id" for which SMTP is enabled.
            }
        };
        Session session = Session.getInstance(props, auth);

        emailService.sendEmail(session, sendEmailDto.getTo(),sendEmailDto.getSubject(), sendEmailDto.getBody());
    }
}
