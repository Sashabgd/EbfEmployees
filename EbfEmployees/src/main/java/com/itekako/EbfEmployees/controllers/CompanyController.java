package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import com.itekako.EbfEmployees.services.CompanyService;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(value = "/api/companies",produces = MediaType.APPLICATION_JSON_VALUE)
@Data
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    @PageableAsQueryParam
    public ResponseEntity getAllCompanies(@NotNull @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    @GetMapping("/{id}/employees")
    @PageableAsQueryParam
    public ResponseEntity getAllEmployeesForCompany(@Valid @PathVariable("id") Long id, @NotNull @ParameterObject Pageable pageable) throws ResourceNotFoundException {
        return ResponseEntity.ok(companyService.getAllEmployeesForCompany(id, pageable));
    }

    @PostMapping
    public ResponseEntity createCompany(@Valid @RequestBody CompanyDetails companyDetails){
        return ResponseEntity.created(ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(companyService.createCompany(companyDetails)).
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
