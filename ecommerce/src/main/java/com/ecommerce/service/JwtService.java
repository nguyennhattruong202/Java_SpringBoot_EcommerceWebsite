package com.ecommerce.service;

import com.ecommerce.entity.User;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {

    public String generateToken(User user);

    public SignedJWT verifyToken(String token, boolean isRefresh);
}
