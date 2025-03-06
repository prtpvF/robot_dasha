package diplom.by.robot.service;

import diplom.by.robot.email.JavaEmailSenderBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

        private final JavaEmailSenderBean javaMailSender;

        public void sendEmail(String toEmail,
                              String subject,
                              String body){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("caplyginmihail48@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            javaMailSender.getJavaEmailSender().send(message);

            log.info("mail send successfully...");
        }
}