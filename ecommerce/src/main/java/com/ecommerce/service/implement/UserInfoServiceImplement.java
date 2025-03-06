package com.ecommerce.service.implement;

import com.ecommerce.entity.UserInfo;
import com.ecommerce.repository.UserInfoRepository;
import com.ecommerce.service.UserInfoService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserInfoServiceImplement implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public UserInfoServiceImplement(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserInfo getUserDetail(Long id) {
        UserInfo userInfo = null;
        Optional<UserInfo> optional = userInfoRepository.findById(id);
        if (optional.isPresent()) {
            userInfo = optional.get();
        }
        return userInfo;
    }
}
