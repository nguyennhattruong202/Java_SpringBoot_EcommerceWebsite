package com.ecommerce.controller;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.InvalidatedToken;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.security.JwtTokenProvider;
import com.ecommerce.service.AuthenticationService;
import com.ecommerce.service.InvalidatedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final InvalidatedTokenService invalidatedTokenService;

    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public AuthenticationController(AuthenticationService authenticationService,
            JwtTokenProvider jwtTokenProvider, InvalidatedTokenService invalidatedTokenService) {
        this.authenticationService = authenticationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.invalidatedTokenService = invalidatedTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            UserDetails userDetails = authenticationService.authenticate(authenticationRequest);
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", jwtTokenProvider.generateAccessToken(userDetails));
            ResponseCode responseCode = ResponseCode.AUTHENTICATED;
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(responseCode.getCode());
            apiResponse.setMessage(responseCode.getMessage());
            apiResponse.setData(tokenMap);
            ResponseCookie cookie = ResponseCookie.from("refreshToken",
                    jwtTokenProvider.generateRefreshToken(userDetails))
                    .httpOnly(true).secure(false).path("/").maxAge(REFRESHABLE_DURATION)
                    .sameSite("Strict").build();
            return ResponseEntity.status(responseCode.getHttpStatusCode())
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(apiResponse);
        } catch (UsernameNotFoundException | AppException e) {
            ResponseCode responseCode = ResponseCode.UNAUTHENTICATED;
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(responseCode.getCode());
            apiResponse.setMessage(responseCode.getMessage());
            return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, @CookieValue(name = "refreshToken", required = false) String refreshToken) throws ParseException {
        String accessToken = jwtTokenProvider.getTokenFromHeaderRequest(request);
        if (accessToken != null && refreshToken != null) {
            InvalidatedToken accessInvalidatedToken = new InvalidatedToken(jwtTokenProvider.extractJWTID(accessToken), jwtTokenProvider.extractExpirationTime(accessToken));
            InvalidatedToken refreshInvalidatedToken = new InvalidatedToken(jwtTokenProvider.extractJWTID(refreshToken), jwtTokenProvider.extractExpirationTime(refreshToken));
            invalidatedTokenService.save(accessInvalidatedToken);
            invalidatedTokenService.save(refreshInvalidatedToken);
            ResponseCode responseCode = ResponseCode.LOGOUT_SUCCESS;
            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setCode(responseCode.getCode());
            apiResponse.setMessage(responseCode.getMessage());
            return ResponseEntity.status(responseCode.getHttpStatusCode())
                    .header(HttpHeaders.SET_COOKIE, authenticationService.cleanJwtCookie().toString())
                    .body(apiResponse);
        } else {
            ResponseCode responseCode = ResponseCode.LOGOUT_ERROR;
            ApiResponse<Void> apiResponse = new ApiResponse<>();
            apiResponse.setCode(responseCode.getCode());
            apiResponse.setMessage(responseCode.getMessage());
            return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
        }
    }

//    @PostMapping("/introspect")
//    public ResponseEntity<ApiResponse<?>> introspect(@RequestBody IntrospectRequest introspectRequest) {
//        boolean introspect = authenticationService.introspect(introspectRequest);
//        if (!introspect) {
//            throw new AppException(ResponseCode.INVALID_TOKEN);
//        }
//        ResponseCode responseCode = ResponseCode.VALID_TOKEN;
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(responseCode.getCode());
//        apiResponse.setMessage(responseCode.getMessage());
//        apiResponse.setData(introspect);
//        return ResponseEntity.status(responseCode.getHttpStatusCode()).body(apiResponse);
//    }
}
