package com.example.nutriplanner.service;

import com.example.nutriplanner.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    User getUserByEmail(String email);
}