package com.ecommerce.service;

import com.ecommerce.entity.User;
import com.ecommerce.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

public interface UserService {

    public void encodePassword(User user);

    public void saveUser(User user);

    public User getUserByLogin(String login);

    public String isLoginUnique(Long id, String login);

    public boolean checkLoginRegistration(String login);

    public Page<User> listByPage(int pageNum);

    public User getUser(Long id) throws UserNotFoundException;

    public void deleteUser(Long id) throws UserNotFoundException;
}
