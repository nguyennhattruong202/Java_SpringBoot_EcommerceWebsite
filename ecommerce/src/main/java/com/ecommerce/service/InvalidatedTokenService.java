package com.ecommerce.service;

import com.ecommerce.entity.InvalidatedToken;
import java.util.Optional;

public interface InvalidatedTokenService {

    public Optional<InvalidatedToken> findById(String id);

    public InvalidatedToken save(InvalidatedToken invalidatedToken);
}
