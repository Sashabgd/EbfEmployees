package com.itekako.EbfAuthServer.services;

import com.itekako.EbfAuthServer.dto.Role;
import com.itekako.EbfAuthServer.dto.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;

public class UserServicesImpl implements UserService {
    private final HashMap<String, User> userHashMap = new HashMap<>();

    public UserServicesImpl(PasswordEncoder passwordEncoder) {
        userHashMap.put("admin", new User()
                .setUsername("admin")
                .setPassword(passwordEncoder.encode("adminpass"))
                .setRole(Role.Admin));
        userHashMap.put("user", new User()
                .setRole(Role.User)
                .setUsername("user")
                .setPassword(passwordEncoder.encode("userpass")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userHashMap.get(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton((GrantedAuthority) () -> user.getRole().toString().toLowerCase()));
    }

    public User getUser(String username) {
        return userHashMap.get(username);
    }
}
