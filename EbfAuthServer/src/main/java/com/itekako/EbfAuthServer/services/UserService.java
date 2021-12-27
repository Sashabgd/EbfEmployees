package com.itekako.EbfAuthServer.services;

import com.itekako.EbfAuthServer.dto.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getUser(String username);
}
