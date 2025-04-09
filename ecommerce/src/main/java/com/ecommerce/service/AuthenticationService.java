package com.ecommerce.service;

import com.ecommerce.dto.request.AuthenticationRequest;
import com.ecommerce.dto.request.IntrospectRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;

public interface AuthenticationService {

    public ApiResponse<String> authenticate(AuthenticationRequest authenticationRequest);

    public boolean introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
}
