package com.ecommerce.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ResponseCode {
    AUTHENTICATED(700, "Authenticated", HttpStatus.OK),
    VALID_TOKEN(701, "Token valid", HttpStatus.OK),
    LOGOUT_SUCCESS(702, "Logout success", HttpStatus.OK),
    USER_NOT_EXISTED(901, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(902, "Unauthenticated", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(903, "Token invalid", HttpStatus.BAD_REQUEST),
    LOGOUT_ERROR(904, "Logout error", HttpStatus.BAD_REQUEST),
    SERVER_ERROR(1000, "Server error", HttpStatus.INTERNAL_SERVER_ERROR);

    ResponseCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
