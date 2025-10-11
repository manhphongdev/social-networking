package vn.socialnet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.socialnet.dto.request.PostCreationRequest;
import vn.socialnet.enums.MediaType;
import vn.socialnet.exception.AppException;
import vn.socialnet.exception.ErrorCode;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Post;
import vn.socialnet.model.PostMedia;
import vn.socialnet.model.User;
import vn.socialnet.repository.PostRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public Long addPost(PostCreationRequest request) {
        //add file
        List<MultipartFile> files = request.getFile();
        List<String> urls = new ArrayList<>();
        String url;

        User user = getUserById(request.getUserId());

        //add Post
        Post post = Post.builder()
                .caption(request.getCaption())
                .user(user)
                .privacy(request.getPrivacy())
                .build();

        List<PostMedia> postMedias = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                url = s3Service.uploadFile(file);
                urls.add(url);
                postMedias.add(PostMedia.builder()
                        .url(url)
                        .post(post)
                        .mediaType(getMediaType(file))
                        .build());
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FIELD);
            }
        }

        post.setMedia(postMedias);
        postRepository.save(post);
        return post.getId();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private MediaType getMediaType(MultipartFile file) {
        Tika tika = new Tika();
        try {
            String detectedType = tika.detect(file.getInputStream());
            if (detectedType.startsWith("image/")) {
                return MediaType.IMAGE;
            } else if (detectedType.startsWith("video/")) {
                return MediaType.VIDEO;
            } else {
                throw new AppException(ErrorCode.UNSUPPORTED_TYPE_OF_FILE);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
