package vn.socialnet.model;

import jakarta.persistence.*;
import lombok.*;
import vn.socialnet.enums.MediaType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post_media")
public class PostMedia extends AbstractEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

}
