package vn.socialnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.socialnet.dto.request.UserCreationRequest;
import vn.socialnet.dto.request.UserProfileRequest;
import vn.socialnet.dto.response.PageResponse;
import vn.socialnet.dto.response.ResponseData;
import vn.socialnet.dto.response.UserDetailResponse;
import vn.socialnet.enums.UserStatus;
import vn.socialnet.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    @Operation(method = "POST", summary = "create new user account",
            description = "Send a request via this API to add new user and set role to db")
    public ResponseData<Long> addUser(@Valid @RequestBody UserCreationRequest user) {

        log.info("Request add new user, email: {} ", user.getEmail());
        long id = userService.saveUser(user);
        log.info("Response add new user success, email: {} ", user.getEmail());

        return new ResponseData<>(HttpStatus.CREATED.value(), "User added", id);
    }

    @Operation(summary = "Get a user by id",
            description = "Send a request via this API to get a user")
    @GetMapping("/{id}")
    public ResponseData<UserDetailResponse> getUser(@PathVariable long id) {

        log.info("Request find user by id, id: {} ", id);
        UserDetailResponse userDetail = userService.getUser(id);
        log.info("Response find user success, id: {} ", id);

        return new ResponseData<>(HttpStatus.OK.value(), "User founded", userDetail);
    }

    @Operation(summary = "Get all users",
            description = "Send a request via this API to get all users")
    @GetMapping("/list")
    public ResponseData<?> getAllUser(@Min(0) @RequestParam(defaultValue = "0", required = false) int pageNo,
                                      @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {

        log.info("Request getAllUser, pageNo: {}, pageSize: {}", pageNo, pageSize);
        PageResponse<?> users = userService.getAllUsers(pageNo, pageSize);
        log.info("Response getAllUser, pageNo: {}, pageSize: {}", pageNo, pageSize);

        return new ResponseData<>(HttpStatus.OK.value(), "Users founded successful", users);
    }


    @Operation(summary = "Get all users with sort by",
            description = "Send a request via this API to get all users with sort by")
    @GetMapping("/list-with-sortBy")
    public ResponseData<?> getAllUserWithSortBy(@Min(1) @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                @RequestParam(required = false) String sortBy) {

        log.info("Request getAllUser, pageNo: {}, pageSize: {}", pageNo, pageSize);
        PageResponse<?> users = userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy);
        log.info("Response getAllUser, pageNo: {}, pageSize: {}", pageNo, pageSize);

        return new ResponseData<>(HttpStatus.OK.value(), "Users founded successful", users);
    }

    @Operation(summary = "Get all users with sort by many column",
            description = "Send a request via this API to get all users with sort by many column ")
    @GetMapping("/list-with-sortBy-multiple-column")
    public ResponseData<?> getAllUserWithSortByAndMultipleColumn(@Min(1) @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                 @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                 @RequestParam(required = false) String... sorts) {

        log.info("Request getAllUser, pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sorts);
        PageResponse<?> users = userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts);
        log.info("Response getAllUser, pageNo: {}, pageSize: {},sortBy: {}", pageNo, pageSize, sorts);

        return new ResponseData<>(HttpStatus.OK.value(), "Users founded successful", users);
    }

    @Operation(summary = "Get all users with sort by many column",
            description = "Send a request via this API to get all users with sort by many column ")
    @GetMapping("/list-with-sortBy-multiple-column-search")
    public ResponseData<?> getAllUserWithSortByColumnAndSearch(@Min(1) @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                               @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                               @RequestParam(defaultValue = "20", required = false) String search,
                                                               @RequestParam(required = false) String sortBy) {

        log.info("Request getAllUser, pageNo: {}, pageSize: {}, sortBy: {}", pageNo, pageSize, sortBy);
        PageResponse<?> users = userService.getAllUsersWithSortByColumnsAndSearch(pageNo, pageSize, search, sortBy);
        log.info("Response getAllUser, pageNo: {}, pageSize: {},sortBy: {}", pageNo, pageSize, sortBy);

        return new ResponseData<>(HttpStatus.OK.value(), "Users founded successful", users);
    }

    @Operation(summary = "Update user profile by Id",
            description = "Send a request via this API to update user profile(name, date of birth, gender)")
    @PutMapping("/update/{id}")
    public ResponseData<?> updateUser(@Min(1) @PathVariable long id, @Valid @RequestBody UserProfileRequest user) {

        log.info("Request updateUser, id: {} ", id);
        userService.updateUser(user, id);
        log.info("Response updateUser, id: {} ", id);
        return new ResponseData<>(HttpStatus.OK.value(), "User updated successful");
    }

    @Operation(summary = "Change status of a user",
            description = "Send a request via this API to update user")
    @PatchMapping("/update-status/{id}")
    public ResponseData<?> updateUserStatus(@Min(1) @PathVariable long id, @Valid @RequestParam UserStatus status) {

        log.info("Request update User status, id: {}, status:  {}", id, status);
        userService.changeStatus(id, status);
        log.info("Response update User status, id: {}, status:  {}", id, status);

        return new ResponseData<>(HttpStatus.OK.value(), "User Status updated successful");
    }
}
