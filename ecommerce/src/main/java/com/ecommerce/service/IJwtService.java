package com.ecommerce.service;

import com.nimbusds.jwt.SignedJWT;

public interface IJwtService {

    public String generateToken();

    public SignedJWT verifyToken(String token, boolean isRefresh);
}
