package com.itekako.EbfEmployees.database;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {


    @Override
    public String resolveCurrentTenantIdentifier() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context.getAuthentication() == null)return "admin";
        List<SimpleGrantedAuthority> collect = context.getAuthentication().getAuthorities().stream()
                .map(a -> (SimpleGrantedAuthority) a).collect(Collectors.toList());
        return collect.get(0).getAuthority().replace("ROLE_","");
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
