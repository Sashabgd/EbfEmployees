package com.itekako.EbfEmployees.auth;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(10000)
public class DatabaseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String database = request.getHeader("Selected-Database");
        if(database == null){
            filterChain.doFilter (request,response);
            return;
        }
        TenantAuthentificationToken authentication = (TenantAuthentificationToken) SecurityContextHolder.getContext().getAuthentication();
        authentication.setDatabase(database);
        filterChain.doFilter (request,response);
    }
}
