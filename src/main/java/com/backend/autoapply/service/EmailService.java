package com.backend.autoapply.service;

import com.backend.autoapply.model.ApplicationLog;
import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.repository.ApplicationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final ApplicationLogRepository applicationLogRepository;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.cv.path}")
    private String cvPath;

    public void sendApplicationEmail(JobOffer jobOffer, String recipientEmail) {
        try {
            MimeMessage message = customCreateMimeMessage(jobOffer, recipientEmail);

            mailSender.send(message);

            saveSuccessLog(jobOffer, recipientEmail);

        } catch (MessagingException | MailException e) {

            log.error("Failed to send email for job offer: {}", jobOffer.getTitle(), e);

            saveFailureLog(jobOffer, recipientEmail, e.getMessage());

            //TODO create a custom exception
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private MimeMessage customCreateMimeMessage(JobOffer jobOffer,
                                                String recipientEmail) throws MessagingException
    {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(recipientEmail);

        helper.setSubject(
                "Application for "
                        + jobOffer.getTitle()
                        + " at "
                        + jobOffer.getCompany());

        helper.setText(buildEmailBody(jobOffer), true);

        attachCv(helper);

        return message;
    }

    private void attachCv(MimeMessageHelper helper) throws MessagingException
    {
        File cv = new File(cvPath);

        if (cv.exists()) {
            helper.addAttachment("CV.pdf", cv);
        } else {
            log.warn("CV file not found at {}", cvPath);
        }
    }

    private void saveSuccessLog(JobOffer offer,
                                String recipientEmail) {

        applicationLogRepository.save(
                ApplicationLog.builder()
                        .jobOffer(offer)
                        .recipientEmail(recipientEmail)
                        .status(ApplicationLog.ApplicationStatus.SENT)
                        .build()
        );
    }

    private void saveFailureLog(JobOffer offer,
                                String recipientEmail,
                                String error) {

        applicationLogRepository.save(
                ApplicationLog.builder()
                        .jobOffer(offer)
                        .recipientEmail(recipientEmail)
                        .status(ApplicationLog.ApplicationStatus.FAILED)
                        .errorMessage(error)
                        .build()
        );
    }

    private String buildEmailBody(JobOffer jobOffer) {
        return String.format(
                """
                <html>
                <body>
                    <h2>Application for %s</h2>
                    <p>Dear Hiring Team,</p>
                    <p>I am writing to express my interest in the <strong>%s</strong> position at <strong>%s</strong>.</p>
                    <p><strong>Location:</strong> %s</p>
                    <p><strong>Job Description:</strong></p>
                    <p>%s</p>
                    <p>Please find my CV attached for your review.</p>
                    <p>Thank you for considering my application. I look forward to hearing from you.</p>
                    <p>Best regards,</p>
                </body>
                </html>
                """,
                jobOffer.getTitle(),
                jobOffer.getTitle(),
                jobOffer.getCompany(),
                jobOffer.getLocation(),
                jobOffer.getDescription()
        );
    }
}
