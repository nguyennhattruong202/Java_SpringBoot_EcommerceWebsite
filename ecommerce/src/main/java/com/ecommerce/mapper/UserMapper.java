package com.ecommerce.mapper;

import com.ecommerce.dto.request.UserRegistrationRequest;
import com.ecommerce.entity.User;

public class UserMapper {

    public static User toUser(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setLogin(userRegistrationRequest.getLogin());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(userRegistrationRequest.getPassword());
        user.setRole(userRegistrationRequest.getRole());
        return user;
    }
}
