package com.ecommerce.service.implement;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import com.ecommerce.entity.User;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.service.AuthenticationService;

@Transactional
@Service
public class IAuthenticationService implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;

    public IAuthenticationService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            IJwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByLogin(authenticationRequest.getLogin())
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ResponseCode.UNAUTHENTICATED);
        }
        return jwtService.generateToken(user);
    }

    @Override
    public boolean introspect(IntrospectRequest introspectRequest) {
        boolean isValidToken = false;
        jwtService.verifyToken(introspectRequest.getToken(), false);
        isValidToken = true;
        return isValidToken;
    }
}
