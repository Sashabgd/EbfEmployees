package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Page<Employee> getAllEmployeesForCompany(long companyId, Pageable pageable) throws ResourceNotFoundException;
}
