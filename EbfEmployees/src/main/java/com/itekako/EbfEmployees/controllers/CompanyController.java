package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import com.itekako.EbfEmployees.services.CompanyService;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/companies",produces = MediaType.APPLICATION_JSON_VALUE)
@Data
public class CompanyController {

    private final CompaniesRepository companiesRepository;
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity getAllCompanies(Pageable pageable){
        return ResponseEntity.ok(companiesRepository.findAll(pageable));
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity getAllEmployeesForCompany(@Valid @PathVariable("id") Long id, Pageable pageable) throws ResourceNotFoundException {
        return ResponseEntity.ok(companyService.getAllEmployeesForCompany(id, pageable));
    }
}
