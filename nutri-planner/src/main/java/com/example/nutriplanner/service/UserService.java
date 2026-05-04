package com.example.nutriplanner.service;

import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id) throws ApiExceptionResponse;
    List<User> getAllUsers();
    User updateUser(Long id, User updatedUser) throws ApiExceptionResponse;
}