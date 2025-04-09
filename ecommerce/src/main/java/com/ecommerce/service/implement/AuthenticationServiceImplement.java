package com.ecommerce.service.implement;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.User;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AuthenticationServiceImplement implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationServiceImplement(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse<String> authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByLogin(authenticationRequest.getLogin())
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ResponseCode.UNAUTHENTICATED);
        }
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ResponseCode.AUTHENTICATED.getCode());
        apiResponse.setMessage(ResponseCode.AUTHENTICATED.getMessage());
        apiResponse.setData("Token");
        return apiResponse;
    }

    @Override
    public boolean introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        try {
            jwtService.verifyToken(token, false);
            return true;
        } catch (AppException e) {
            return false;
        }
    }
}
