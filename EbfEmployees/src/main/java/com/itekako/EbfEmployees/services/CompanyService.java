package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.Dtos.CreateCompanyRequest;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Page<Employee> getAllEmployeesForCompany(long companyId, Pageable pageable) throws ResourceNotFoundException;

    long createCompany(CreateCompanyRequest createCompanyRequest);

    Company getCompany(Long id) throws ResourceNotFoundException;

    Company updateCompany(Long id, CompanyDetails companyDetails) throws ResourceNotFoundException;

    void deleteCompany(Long id) throws ResourceNotFoundException;

    Page<Company> getAllCompanies(Pageable pageable);
}
