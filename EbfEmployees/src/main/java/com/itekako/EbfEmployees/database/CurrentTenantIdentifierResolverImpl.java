package com.itekako.EbfEmployees.database;

import com.itekako.EbfEmployees.auth.TenantAuthentificationToken;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        Authentication authentication = context.getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
            return "admin";
        }
        return ((TenantAuthentificationToken) authentication).getDatabase();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
