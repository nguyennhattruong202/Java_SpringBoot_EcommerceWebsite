package com.ecommerce.service;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;

public interface AuthenticationService {

    public String authenticate(AuthenticationRequest authenticationRequest);

    public boolean introspect(IntrospectRequest introspectRequest);
}
