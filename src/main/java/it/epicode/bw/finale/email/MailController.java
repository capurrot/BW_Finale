package it.epicode.bw.finale.email;

import it.epicode.bw.finale.common.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class MailController {
    @Autowired
    EmailSenderService emailSenderService;
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void sendEmail(@RequestBody EmailRequest request) throws MessagingException {
        emailSenderService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
    }
}
