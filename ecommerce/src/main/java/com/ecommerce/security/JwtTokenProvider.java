package com.ecommerce.security;

import com.ecommerce.service.InvalidatedTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final InvalidatedTokenService invalidatedTokenService;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public JwtTokenProvider(InvalidatedTokenService invalidatedTokenService) {
        this.invalidatedTokenService = invalidatedTokenService;
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, VALID_DURATION);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, REFRESHABLE_DURATION);
    }

    public String generateToken(UserDetails userDetails, long validityInSeconds) {
        JWTClaimsSet jWTClaimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issuer("ecommerce.com")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(validityInSeconds, ChronoUnit.SECONDS)))
                .jwtID(UUID.randomUUID().toString()).claim("scope", userDetails.getAuthorities())
                .build();
        try {
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), jWTClaimsSet);
            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new MACVerifier(SECRET_KEY.getBytes()))
                    && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())
                    && invalidatedTokenService.findById(signedJWT.getJWTClaimsSet().getJWTID()) == null;
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    public String getTokenFromHeaderRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public String extractJWTID(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
    }

    public Date extractExpirationTime(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
    }
}
