package com.illiapinchukmoodle.service.interfacies;

import com.illiapinchukmoodle.data.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link com.illiapinchukmoodle.data.model.User}
 * @author Illia Pinchuk
 */
public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    Optional<User> getUserById(Long userId);
    User getUserByEmail(String email);
    User updateUser(User userRequest, Long userId);
    User deleteUser(Long userId);
    Optional<User> getUserWithCourses(Long userId);
}
