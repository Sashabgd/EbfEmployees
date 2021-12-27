package com.itekako.EbfAuthServer.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

@Data
@Accessors(chain = true)
public class Login {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
