package vn.socialnet.service;

import vn.socialnet.dto.request.EditPostRequest;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.model.User;

public interface PostService {
    int totalPosts(User user);

    Long addPost(PostCreationRequest request);

    void updatePost(Long postId, Long id, EditPostRequest request);
}
