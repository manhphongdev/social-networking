package vn.socialnet.service;

import vn.socialnet.dto.request.EditPostRequest;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.PostResponse;

public interface PostService {

    Long addPost(PostCreationRequest request);

    void updatePost(Long postId, Long id, EditPostRequest request);

    PostResponse getPostById(Long postId, Long currentUserId);

    void deletePost(Long postId, Long userId);

    PageResponse<?> getUserPosts(Long userId, Long currentUserId, int page, int size);

    PageResponse<?> getFeed(Long userId, int page, int size);

    PageResponse<?> getExploreFeed(Long currentUserId, int page, int size);

}
