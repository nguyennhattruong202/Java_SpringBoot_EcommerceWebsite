package com.ecommerce.configuration;

import com.ecommerce.enums.ResponseCode;
import com.ecommerce.exception.AppException;
import com.ecommerce.security.JwtTokenProvider;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtTokenProvider jwtTokenProvider;
    private NimbusJwtDecoder nimbusJwtDecoder = null;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public CustomJwtDecoder(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
//        if (!jwtTokenProvider.validateToken(token)) {
//            log.info("JWT DECODER");
//            throw new AppException(ResponseCode.INVALID_TOKEN);
//        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512).build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
