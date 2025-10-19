package vn.socialnet.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SendMessageRequest {
    private String message;
    private MultipartFile media; // Optional media attachment
}
