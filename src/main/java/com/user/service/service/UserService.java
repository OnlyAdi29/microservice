package com.user.service.service;

import com.user.service.entity.User;
import com.user.service.exeptionResource.UserNotFound;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUser();

    User getUser(String userId) throws UserNotFound;

    void deleteUser(String userId);

    User updateUser(String userId,User user) throws UserNotFound;
}
