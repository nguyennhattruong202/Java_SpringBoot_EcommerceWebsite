package com.ecommerce.service.implement;

import com.ecommerce.entity.InvalidatedToken;
import com.ecommerce.repository.InvalidatedTokenRepository;
import com.ecommerce.service.InvalidatedTokenService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class IInvalidatedTokenService implements InvalidatedTokenService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public IInvalidatedTokenService(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Override
    public Optional<InvalidatedToken> findById(String id) {
        return invalidatedTokenRepository.findById(id);
    }

    @Override
    public InvalidatedToken save(InvalidatedToken invalidatedToken) {
        return invalidatedTokenRepository.save(invalidatedToken);
    }
}
