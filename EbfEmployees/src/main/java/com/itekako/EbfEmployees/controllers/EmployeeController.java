package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.Dtos.EmployeeDetails;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import com.itekako.EbfEmployees.services.EmployeeService;
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
@RequestMapping(value = "/api/employees",produces = MediaType.APPLICATION_JSON_VALUE)
@Data
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PageableAsQueryParam
    public ResponseEntity getAllEmployees(@NotNull @ParameterObject Pageable pageable){
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployee(@PathVariable("id") Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PostMapping
    public ResponseEntity createEmployee(@Valid @RequestBody EmployeeDetails employeeDetails) throws ResourceNotFoundException {
        Employee createdEmployee = employeeService.createEmployee(employeeDetails);
        return ResponseEntity.
                created(ServletUriComponentsBuilder.
                        fromCurrentRequest().
                        path("/{id}").
                        buildAndExpand(createdEmployee.
                                getId()).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDetails employeeDetails) throws ResourceNotFoundException {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }
}
