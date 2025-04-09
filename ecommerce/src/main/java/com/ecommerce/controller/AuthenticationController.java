package com.ecommerce.controller;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<?>> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        String authenticate = authenticationService.authenticate(authenticationRequest);
        ResponseCode responseCode = ResponseCode.AUTHENTICATED;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(responseCode.getCode());
        apiResponse.setMessage(responseCode.getMessage());
        apiResponse.setData(authenticate);
        return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<?>> introspect(@RequestBody IntrospectRequest introspectRequest) {
        boolean introspect = authenticationService.introspect(introspectRequest);
        if (!introspect) {
            throw new AppException(ResponseCode.INVALID_TOKEN);
        }
        ResponseCode responseCode = ResponseCode.VALID_TOKEN;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(responseCode.getCode());
        apiResponse.setMessage(responseCode.getMessage());
        apiResponse.setData(introspect);
        return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
    }
}
