package com.ecommerce.exception;

import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handlingAppException(AppException appException) {
        ResponseCode responseCode = appException.getResponseCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(responseCode.getCode());
        apiResponse.setMessage(responseCode.getMessage());
        return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
    }
}
