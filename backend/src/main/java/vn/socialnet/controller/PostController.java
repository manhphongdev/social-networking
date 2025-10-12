package vn.socialnet.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.dto.request.EditPostRequest;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.PostResponse;
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

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseData<?> editPost(@PathVariable Long id,
                                    @ModelAttribute @Valid EditPostRequest request,
                                    @AuthenticationPrincipal User user) {
        log.info("Request to edit post with id {}", id);
        postService.updatePost(id, user.getId(), request);
        log.info("Post with id {} updated successfully", id);
        return new ResponseData<>(HttpStatus.OK.value(), "Update post successfully, id: " + id);
    }

    @GetMapping("/{id}")
    public ResponseData<PostResponse> getPostById(@PathVariable Long id,
                                                  @AuthenticationPrincipal User user) {
        log.info("Request to get post with id {}", id);
        PostResponse post = postService.getPostById(id, user.getId());
        return new ResponseData<>(HttpStatus.OK.value(), "Post retrieved successfully", post);
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deletePost(@PathVariable Long id,
                                      @AuthenticationPrincipal User user) {
        log.info("Request to delete post with id {}", id);
        postService.deletePost(id, user.getId());
        log.info("Post with id {} deleted successfully", id);
        return new ResponseData<>(HttpStatus.OK.value(), "Post deleted successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseData<PageResponse<?>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal User user) {
        log.info("Request to get posts for user {}", userId);
        PageResponse<?> posts = postService.getUserPosts(userId, user.getId(), page, size);
        return new ResponseData<>(HttpStatus.OK.value(), "User posts retrieved successfully", posts);
    }

}
