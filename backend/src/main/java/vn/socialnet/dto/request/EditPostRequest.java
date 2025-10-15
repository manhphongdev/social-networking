package vn.socialnet.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EditPostRequest {

    @Size(max = 1000, message = "Caption must not exceed 1000 characters")
    private String caption;

    private String privacy;

    private List<Long> removedMediaIds;

    private List<MultipartFile> file;
}
