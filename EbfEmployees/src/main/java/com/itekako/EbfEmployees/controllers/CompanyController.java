package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.Dtos.CreateCompanyRequest;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import com.itekako.EbfEmployees.services.CompanyService;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/companies",produces = MediaType.APPLICATION_JSON_VALUE)
@Data
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity getAllCompanies(Pageable pageable){
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity getAllEmployeesForCompany(@Valid @PathVariable("id") Long id, Pageable pageable) throws ResourceNotFoundException {
        return ResponseEntity.ok(companyService.getAllEmployeesForCompany(id, pageable));
    }

    @PostMapping
    public ResponseEntity createCompany(@RequestBody CreateCompanyRequest createCompanyRequest){
        return ResponseEntity.created(ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(companyService.createCompany(createCompanyRequest)).
                toUri()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getCompanyById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCompany(@PathVariable Long id,@Valid @RequestBody CompanyDetails companyDetails) throws ResourceNotFoundException {
        return ResponseEntity.ok(companyService.updateCompany(id,companyDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCompany(@PathVariable Long id) throws ResourceNotFoundException {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
