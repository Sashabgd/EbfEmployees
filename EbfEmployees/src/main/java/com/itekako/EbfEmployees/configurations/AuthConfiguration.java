package com.itekako.EbfEmployees.configurations;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwtauth")
public class AuthConfiguration {
    private int lifeTime;
    private String secret;
}

