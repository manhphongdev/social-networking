package vn.socialnet.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User already exists"),
    EMAIL_EXISTED(1002, "Email already existed"),
    USER_BANED(1003, "User is banned"),
    FILE_UPLOAD_FIELD(1004, "File upload field"),
    UNSUPPORTED_TYPE_OF_FILE(1005, "Unsupported file type"),
    ;


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
