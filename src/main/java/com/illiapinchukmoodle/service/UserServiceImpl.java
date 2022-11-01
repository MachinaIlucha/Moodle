package com.illiapinchukmoodle.service;

import com.illiapinchukmoodle.data.model.Role;
import com.illiapinchukmoodle.data.model.Status;
import com.illiapinchukmoodle.data.model.User;
import com.illiapinchukmoodle.exception.UserNotFoundException;
import com.illiapinchukmoodle.repository.RoleRepository;
import com.illiapinchukmoodle.repository.UserRepository;
import com.illiapinchukmoodle.service.interfacies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for {@link com.illiapinchukmoodle.service.interfacies.UserService}
 * @author Illia Pinchuk
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        Role roleStudent = roleRepository.findByName("ROLE_STUDENT");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleStudent);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User userRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setUpdated(new Date());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public Optional<User> getUserWithCourses(Long userId) {
        return userRepository.getUserWithCourses(userId);
    }

    @Override
    public User deleteUser(Long userId) {
        User user = getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setStatus(Status.DELETED);
        user.setUpdated(new Date());
        return userRepository.save(user);
    }
}
