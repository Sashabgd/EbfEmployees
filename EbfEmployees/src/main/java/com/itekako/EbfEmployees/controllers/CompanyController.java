package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/companies",produces = MediaType.APPLICATION_JSON_VALUE)
@Data
public class CompanyController {

    private final CompaniesRepository companiesRepository;

    @GetMapping
    public ResponseEntity getAllCompanies(Pageable pageable){
        return ResponseEntity.ok(companiesRepository.findAll(pageable));
    }
}
