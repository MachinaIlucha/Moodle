package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select user from User user join fetch user.userCourses where user.id = :userId")
    Optional<User> getUserWithCourses(Long userId);
}
