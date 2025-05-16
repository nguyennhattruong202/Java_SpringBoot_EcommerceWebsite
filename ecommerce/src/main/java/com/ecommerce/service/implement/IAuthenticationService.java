package com.ecommerce.service.implement;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.service.AuthenticationService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Transactional
@Service
public class IAuthenticationService implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public IAuthenticationService(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails authenticate(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword());
        if (!authenticated) {
            throw new AppException(ResponseCode.UNAUTHENTICATED);
        }
        return userDetails;
    }

    @Override
    public ResponseCookie cleanJwtCookie() {
        return ResponseCookie.from("refreshToken", null).httpOnly(true)
                .secure(false).path("/").maxAge(0).sameSite("Strict").build();
    }

    @Override
    public boolean introspect(IntrospectRequest introspectRequest) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
