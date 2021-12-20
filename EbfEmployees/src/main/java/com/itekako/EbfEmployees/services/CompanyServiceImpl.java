package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CreateCompanyRequest;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class CompanyServiceImpl implements CompanyService{

    private final CompaniesRepository companiesRepository;
    private final EmployeesRepository employeesRepository;

    @Override
    public Page<Employee> getAllEmployeesForCompany(long companyId, Pageable pageable) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(companyId);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id $o does not exist!",companyId));
        }
        Page<Employee> employees = employeesRepository.findAllEmployeesByCompany(company.get(),pageable);
        return employees;
    }

    @Override
    public long createCompany(CreateCompanyRequest createCompanyRequest) {
        Company company = new Company()
                .setName(createCompanyRequest.getName());
        companiesRepository.save(company);
        return company.getId();
    }

    @Override
    public Company getCompany(Long id) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(id);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id $o does not exist!",id));
        }
        return company.get();
    }
}
