package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Illia Pinchuk
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
