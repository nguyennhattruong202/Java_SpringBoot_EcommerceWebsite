package com.ecommerce.service.implement;

import com.ecommerce.service.UserService;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplement(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void encodePassword(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.getReferenceById(user.getId());
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        userRepository.save(user);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
