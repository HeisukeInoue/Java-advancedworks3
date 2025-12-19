package com.example.dockerapi.service;

import com.example.dockerapi.model.User;
import com.example.dockerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(int id, User user) {
        int affectedRows = userRepository.update(id, user.getName(), user.getEmail());
        if (affectedRows == 0) {
            throw new RuntimeException("User not found: " + id);
        }
        return new User(id, user.getName(), user.getEmail());
    }

    @Transactional
    public void deleteUser(int id) {
        int affectedRows = userRepository.delete(id);
        if (affectedRows == 0) {
            throw new RuntimeException("User not found: " + id);
        }
    }
}