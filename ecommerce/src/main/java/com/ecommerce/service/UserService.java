package com.ecommerce.service;

import com.ecommerce.entity.User;

public interface UserService {

    public void encodePassword(User user);

    public void saveUser(User user);

    public User getUserByLogin(String login);

}
