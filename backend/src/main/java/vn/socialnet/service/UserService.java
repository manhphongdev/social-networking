package vn.socialnet.service;

import vn.socialnet.dto.request.UserCreationRequest;
import vn.socialnet.dto.request.UserProfileRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.UserDetailResponse;
import vn.socialnet.enums.UserStatus;

public interface UserService {

    long saveUser(UserCreationRequest req);

    void updateUser(UserProfileRequest req, long id);

    void changeStatus(long id, UserStatus status);

    void deleteUser(long id);

    UserDetailResponse getUser(long id);

    PageResponse<?> getAllUsers(int pageNo, int pageSize);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersWithSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy);

}
