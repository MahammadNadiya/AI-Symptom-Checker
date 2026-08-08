package com.nadiya.backend.service;

import com.nadiya.backend.entity.User;
import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);

    User loginUser(String email, String password);
}