package com.example.bookstore_app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public boolean updateUser(Long id, User user) {
        User userToUpdate = this.findUserById(id);
        if(userToUpdate != null) {
            user.setId(userToUpdate.getId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUserById(Long id) {
        User userToDelete = this.findUserById(id);
        if(userToDelete != null) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
