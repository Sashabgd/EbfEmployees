package com.itekako.EbfAuthServer.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.itekako.EbfAuthServer.configs.AuthConfiguration;
import com.itekako.EbfAuthServer.dto.AccessToken;
import com.itekako.EbfAuthServer.dto.Login;
import com.itekako.EbfAuthServer.dto.User;
import com.itekako.EbfAuthServer.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final AuthConfiguration authConfiguration;
    private final UserServicesImpl userServices;

    public AccessToken login(Login login) throws ResourceNotFoundException {
        Authentication authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        if (authenticate == null || !authenticate.isAuthenticated()) {
            throw new ResourceNotFoundException();
        }
        User user = userServices.getUser(login.getUsername());
        return new AccessToken().setAccessToken(generateAccessToken(login.getUsername(),user.getRole().toString()));
    }

    public String generateAccessToken(String username,String role) {
        return JWT.create().withSubject(String.valueOf(username))
                .withClaim("ROLE", role.toLowerCase())
                .withExpiresAt(new Date(System.currentTimeMillis() + authConfiguration.getLifeTime()))
                .sign(Algorithm.HMAC512(authConfiguration.getSecret().getBytes()));
    }
}
