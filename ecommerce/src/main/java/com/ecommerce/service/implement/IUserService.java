package com.ecommerce.service.implement;

import com.ecommerce.service.UserService;
import com.ecommerce.entity.User;
import com.ecommerce.enums.Pagination;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class IUserService implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public IUserService(UserRepository userRepository,
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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public String isLoginUnique(Long id, String email) {
        User userByLogin = userRepository.findByEmail(email).get();
        if (id == null) {
            if (userByLogin != null) {
                return "Duplicate";
            }
        } else {
            if (!Objects.equals(userByLogin.getId(), id)) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    @Override
    public boolean checkLoginRegistration(String email) {
        User user = userRepository.findByEmail(email).get();
        return user == null;
    }

    @Override
    public Page<User> listByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.USER_PER_PAGE.getValue());
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUser(Long id) throws UserNotFoundException {
        try {
            return userRepository.getReferenceById(id);
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Couldn't find any user with id " + id);
        }
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with id " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with " + email));
    }
}
