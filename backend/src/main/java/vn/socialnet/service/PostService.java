package vn.socialnet.service;

import vn.socialnet.dto.request.PostCreationRequest;

public interface PostService {

    Long addPost(PostCreationRequest request);

}
