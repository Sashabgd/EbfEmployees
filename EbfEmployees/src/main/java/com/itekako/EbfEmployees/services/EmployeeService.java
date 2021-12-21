package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.EmployeeDetails;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<Employee> getAllEmployees(Pageable pageable);

    Employee getEmployee(Long id) throws ResourceNotFoundException;

    Employee createEmployee(EmployeeDetails employeeDetails) throws ResourceNotFoundException;

    Employee updateEmployee(Long employeeId, EmployeeDetails employeeDetails) throws ResourceNotFoundException;

    void deleteEmployee(Long id) throws ResourceNotFoundException;
}
