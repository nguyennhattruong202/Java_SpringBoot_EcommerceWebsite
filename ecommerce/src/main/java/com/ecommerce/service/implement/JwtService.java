package com.ecommerce.service.implement;

import com.ecommerce.entity.User;
import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.repository.InvalidatedTokenRepository;
import com.ecommerce.service.IJwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class JwtService implements IJwtService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signer-key}")
    private String SIGNER_KEY;
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public JwtService(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Override
    public String generateToken() {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jWTClaimsSet = new JWTClaimsSet.Builder()
                .subject("admin@gmail.com")
                .issuer("ecommerce.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "ADMIN")
                .build();
        Payload payload = new Payload(jWTClaimsSet.toJSONObject());
        JWSObject jWSObject = new JWSObject(header, payload);
        try {
            jWSObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jWSObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!isVerify(signedJWT) || isTokenExpired(signedJWT, isRefresh) || isInValidToken(signedJWT)) {
                log.info("Unauthenticate");
                throw new AppException(ResponseCode.UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (ParseException ex) {
            throw new AppException(ResponseCode.SERVER_ERROR);
        }
    }

    private boolean isTokenExpired(SignedJWT signedJWT, boolean isRefresh) {
        try {
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (isRefresh) {
                expirationTime = new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli());
            }
            return expirationTime.before(new Date());
        } catch (ParseException ex) {
            throw new AppException(ResponseCode.SERVER_ERROR);
        }
    }

    private boolean isVerify(SignedJWT signedJWT) {
        try {
            return signedJWT.verify(new MACVerifier(SIGNER_KEY.getBytes()));
        } catch (JOSEException ex) {
            throw new AppException(ResponseCode.SERVER_ERROR);
        }
    }

    private boolean isInValidToken(SignedJWT signedJWT) {
        try {
            return invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
        } catch (ParseException ex) {
            throw new AppException(ResponseCode.SERVER_ERROR);
        }
    }
}
