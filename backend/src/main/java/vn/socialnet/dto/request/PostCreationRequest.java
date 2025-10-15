package vn.socialnet.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.enums.PostPrivates;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class PostCreationRequest implements Serializable {
    @NotNull(message = "Input must be not blank")
    private String caption;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PostPrivates privacy;

    private long userId;

    private List<MultipartFile> file;
}
