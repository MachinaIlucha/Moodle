package com.illiapinchukmoodle.security;

import com.illiapinchukmoodle.data.model.User;
import com.illiapinchukmoodle.security.jwt.JwtUser;
import com.illiapinchukmoodle.security.jwt.JwtUserFactory;
import com.illiapinchukmoodle.service.interfacies.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Illia Pinchuk
 */
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException("User with email: " + email + " not found");

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("User with email: {} successfully loaded", email);
        return jwtUser;
    }
}
