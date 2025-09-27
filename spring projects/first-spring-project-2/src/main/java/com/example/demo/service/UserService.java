package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;

@Service
public class UserService {

 @Autowired
    private UserRepository userRepository;

    public String saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User with this email already exists.";
        }
        userRepository.save(user);
        return "User saved successfully!";
    }

    public User authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }
    public User getUserByEmail(String email) {
        // Fetch user using repository
        return userRepository.findByEmail(email).orElse(null);
    }
}