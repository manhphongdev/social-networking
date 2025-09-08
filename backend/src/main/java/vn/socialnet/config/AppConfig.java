package vn.socialnet.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridKey;


    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridKey);
    }
}
