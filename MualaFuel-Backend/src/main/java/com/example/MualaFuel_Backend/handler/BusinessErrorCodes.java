package com.example.MualaFuel_Backend.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BusinessErrorCodes{

    NO_CODE(0,"No code", HttpStatus.NOT_IMPLEMENTED),
    BAD_CREDENTIALS(301, "Login and / or password is incorrect", HttpStatus.FORBIDDEN),
    EMAIL_IS_USED(302, "Email is used", HttpStatus.BAD_REQUEST),
    BAD_COOKIE(303, "No jwt cookie found", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(304,"Invalid jwt token", HttpStatus.BAD_REQUEST),
    NOT_FOUND(305,"Not found", HttpStatus.NOT_FOUND),
    IMAGE_FETCH_FAILED(306,"Image fetch failed", HttpStatus.BAD_REQUEST),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;
}
