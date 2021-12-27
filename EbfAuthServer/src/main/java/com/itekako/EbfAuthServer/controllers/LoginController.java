package com.itekako.EbfAuthServer.controllers;

import com.itekako.EbfAuthServer.dto.Login;
import com.itekako.EbfAuthServer.exceptions.ResourceNotFoundException;
import com.itekako.EbfAuthServer.services.LoginService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity login(@NonNull @Validated @RequestBody Login login) throws ResourceNotFoundException {
        return ResponseEntity.ok(loginService.login(login));
    }
}
