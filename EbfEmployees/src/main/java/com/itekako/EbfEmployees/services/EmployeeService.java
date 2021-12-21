package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.database.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<Employee> getAllEmployees(Pageable pageable);
}
