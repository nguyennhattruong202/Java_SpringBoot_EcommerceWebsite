package com.ecommerce.service;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    public UserDetails authenticate(AuthenticationRequest authenticationRequest);

    public ResponseCookie cleanJwtCookie();
    
    public boolean introspect(IntrospectRequest introspectRequest);
}
