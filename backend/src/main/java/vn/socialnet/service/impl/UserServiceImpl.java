package vn.socialnet.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;
import vn.socialnet.dto.request.UserCreationRequest;
import vn.socialnet.dto.request.UserProfileRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.UserDetailResponse;
import vn.socialnet.dto.response.UserProfileResponse;
import vn.socialnet.enums.UserRole;
import vn.socialnet.enums.UserStatus;
import vn.socialnet.exception.AppException;
import vn.socialnet.exception.ErrorCode;
import vn.socialnet.exception.ResourceNotFoundException;
import vn.socialnet.model.Role;
import vn.socialnet.model.User;
import vn.socialnet.repository.RoleRepository;
import vn.socialnet.repository.SearchRepository;
import vn.socialnet.repository.UserRepository;
import vn.socialnet.service.FollowService;
import vn.socialnet.service.PostService;
import vn.socialnet.service.UserService;
import vn.socialnet.utils.message.UserMessage;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final SearchRepository searchRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3Service s3Service;

    private final FollowService followService;

    private final PostService postService;

    @Transactional
    @Override
    public long userRegister(UserCreationRequest req, MultipartFile file) throws RuntimeException {

        if (isUserExist(req.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user;
        try {
            String avatarUrl = s3Service.uploadFile(file);
            user = User.builder()
                    .email(req.getEmail())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .name(req.getName())
                    .dateOfBirth(req.getDateOfBirth())
                    .gender(req.getGender())
                    .status(UserStatus.ACTIVE)
                    .avatar(avatarUrl)
                    .build();

            Role role = roleRepository.findByName(UserRole.USER.name())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found!"));
            user.setRole(role);
            userRepository.save(user);
            log.info("User {} added with role {}!", req.getEmail(), role.getName());
        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Failed to read avatar file: " + e.getMessage());
        }

        return user.getId();
    }

    @Override
    public void updateUser(UserProfileRequest req, long id) {
        User user = getUserById(id);

        user.setName(req.getName());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setGender(req.getGender());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("User updated successful, id: {}", user.getId());
    }

    @Override
    public void changeStatus(long id, UserStatus status) {
        User user = getUserById(id);

        user.setStatus(status);
        userRepository.save(user);
        log.info("User status changed successful, id: {}", user.getId());
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
        log.info("User deleted successful, id: {}", id);
    }

    @Override
    public UserDetailResponse getUser(long id) {
        User user = getUserById(id);
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .roleName(user.getRole().getName())
                .build();
    }

    @Override
    public PageResponse<?> getAllUsers(int pageNo, int pageSize) {
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<User> users = userRepository.findAll(pageable);
        return getPageResponse(pageNo, pageSize, users);
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {


        List<Sort.Order> sorts = new ArrayList<>();
        //neu co gia tri
        if (StringUtils.hasLength(sortBy)) {
            //firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    throw new InvalidParameterException("Invalid sort parameter");
                }
            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return getPageResponse(pageNo, pageSize, users);
    }

    private PageResponse<?> getPageResponse(int pageNo, int pageSize, Page<User> users) {
        List<UserDetailResponse> responses = users.stream().map(user -> UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .roleName(user.getRole().getName())
                .build()).toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (String sortBy : sorts) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    throw new InvalidParameterException("Invalid sort parameter");
                }
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        return getPageResponse(pageNo, pageSize, users);
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        return searchRepository.getAllUsersWithSortByColumnsAndSearch(page, pageSize, search, sortBy);
    }

    @Override
    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.getUserByEmail(email);

        return UserProfileResponse.builder()
                .name(user.getName())
                .avatarUrl(user.getAvatar())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .bio(user.getBio())
                .jointAt(user.getCreatedAt().toLocalDate())
                .location(user.getLocation())
                .totalFollowers(followService.totalFollowers(user))
                .totalFollowing(followService.totalFollowings(user))
                .totalPost(postService.totalPosts(user))
                .build();

    }

    private boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(UserMessage.USER_NOT_EXIST));
    }
}
