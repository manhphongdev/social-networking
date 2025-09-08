package vn.socialnet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownException(Exception e, WebRequest request) {
        System.out.println("================> UNKNOW ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        setResponseBadRequest(errorResponse, request);

        if (e != null) {
            errorResponse.setMessage("Failed to convert value of type");
        }
        return errorResponse;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400 BAD_REQUEST
    public ErrorResponse handleValidationError(MethodArgumentNotValidException e, WebRequest req) {
        System.out.println("================> BAD REQUEST");
        ErrorResponse errorResponse = new ErrorResponse();
        setResponseBadRequest(errorResponse, req);

        String message = e.getMessage();
        int start = message.lastIndexOf("[");
        int end = message.lastIndexOf("]");
        message = message.substring(start + 1, end - 1);
        errorResponse.setMessage(message);

        errorResponse.setMessage(message);
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(MethodArgumentTypeMismatchException e, WebRequest req) {
        System.out.println("================> INTERNAL SERVER ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        setResponseBadRequest(errorResponse, req);

        if (e != null) {
            errorResponse.setMessage("Failed to convert value of type");
        }
        return errorResponse;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundError(ResourceNotFoundException e, WebRequest req) {
        System.out.println("================> RESOURCE NOT FOUND ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        setResponseBadRequest(errorResponse, req);

        if (e != null) {
            errorResponse.setMessage(e.getMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler(AuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticatedError(AuthenticatedException e, WebRequest req) {
        System.out.println("================> Authenticated ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase()); // "Bad Request"
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setInstance(req.getDescription(false).replace("uri=", ""));
        errorResponse.setTimestamp(LocalDateTime.now());

        if (e != null) {
            errorResponse.setMessage(e.getMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthenticatedError(UsernameNotFoundException e, WebRequest req) {
        System.out.println("================> USERNAME NOT FOUND ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase()); // "NOT_FOUND"
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setInstance(req.getDescription(false).replace("uri=", ""));
        errorResponse.setTimestamp(LocalDateTime.now());

        if (e != null) {
            errorResponse.setMessage(e.getMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler({NullPointerException.class, InvalidParameterException.class, DuplicateResourceException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNullPointerError(Exception e, WebRequest req) {
        System.out.println("================> ERROR");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase()); // "NOT_FOUND"
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setInstance(req.getDescription(false).replace("uri=", ""));
        errorResponse.setTimestamp(LocalDateTime.now());

        if (e != null) {
            errorResponse.setMessage(e.getMessage());
        }
        return errorResponse;
    }


    private void setResponseBadRequest(ErrorResponse errorResponse, WebRequest req) {
        errorResponse.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase()); // "Bad Request"
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setInstance(req.getDescription(false).replace("uri=", ""));
        errorResponse.setTimestamp(LocalDateTime.now());
    }

}
