package vn.socialnet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.socialnet.enums.MediaType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaResponse {
    private Long id;
    private String url;
    private MediaType mediaType;
}
