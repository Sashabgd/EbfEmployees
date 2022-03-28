package com.itekako.EbfEmployees.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TenantAuthentificationToken extends UsernamePasswordAuthenticationToken {

    private final String userId;
    private String database;

    public TenantAuthentificationToken(String userId,Collection<? extends GrantedAuthority> authorities) {
        super(userId,null,authorities);
        this.userId = userId;
    }


    public String getDatabase(){
        return database;
    }

    public void setDatabase(String database){
        this.database = database;
    }
}
