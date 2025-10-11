package vn.socialnet.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.enums.PostPrivates;
import vn.socialnet.model.User;
import vn.socialnet.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Slf4j(topic = "POST_CONTROLLER")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseData<Long> createPost(@RequestPart List<MultipartFile> file,
                                         @RequestPart String caption,
                                         @RequestPart String privacy,
                                         @AuthenticationPrincipal User user) {
        PostCreationRequest request = PostCreationRequest.builder()
                .caption(caption)
                .file(file)
                .userId(user.getId())
                .privacy(PostPrivates.valueOf(privacy.toUpperCase()))
                .build();

        Long id = postService.addPost(request);
        log.info("Post created successfully, Id: {}", id);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Post created successfully", id);
    }

}
