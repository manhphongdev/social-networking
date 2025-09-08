package vn.socialnet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.socialnet.service.impl.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public void send(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        log.info("Sending email to {}, subject {}", to, subject);
        emailService.send(to, subject, content);
        log.info("Email sent");
    }

    @GetMapping("/send-verify")
    public void sendVerify(@RequestParam String to, @RequestParam String name, @RequestParam String verifyUrl, @RequestParam String homeUrl) {
        log.info("Verifying email to {}", to);
        emailService.sendVerificationEmail(to, name, verifyUrl, homeUrl);
        log.info("Email sent");
    }
}
