package com.ecommerce.mapper;

import com.ecommerce.dto.request.UserInfoRegistrationRequest;
import com.ecommerce.entity.UserInfo;

public class UserInfoMapper {

    public static UserInfo toUserInfo(UserInfoRegistrationRequest userInfoRegistrationRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userInfoRegistrationRequest.getName());
        userInfo.setSurname(userInfoRegistrationRequest.getSurname());
        userInfo.setPhone(userInfoRegistrationRequest.getPhone());
        return userInfo;
    }
}
