package com.itekako.EbfEmployees.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itekako.EbfEmployees.configurations.AuthConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JwtFilter extends BasicAuthenticationFilter {

    private final AuthConfiguration authConfiguration;

    public JwtFilter(AuthenticationManager authenticationManager, AuthConfiguration configuration) {
        super(authenticationManager);
        authConfiguration = configuration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            super.doFilterInternal(request, response, chain);
            return;
        }
        String jwtToken = token.replace("Bearer ", "");

        String subject = null;
        DecodedJWT decodedJWT;
        try{
            decodedJWT = JWT.require(Algorithm.HMAC512(authConfiguration.getSecret())).build().verify(jwtToken);
            subject = decodedJWT.getSubject();
        }catch (TokenExpiredException e){
            super.doFilterInternal(request, response, chain);
            return;
        }
        if (subject == null) {
            super.doFilterInternal(request, response, chain);
            return;
        }
        Claim role = decodedJWT.getClaims().get("ROLE");

        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        if(role!=null) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + role.asString()));
        }
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(subject,null, roles));
        super.doFilterInternal(request, response, chain);
    }
}
