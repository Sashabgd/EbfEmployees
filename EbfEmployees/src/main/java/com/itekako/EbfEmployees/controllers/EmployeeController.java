package com.itekako.EbfEmployees.controllers;

import com.itekako.EbfEmployees.services.EmployeeService;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
