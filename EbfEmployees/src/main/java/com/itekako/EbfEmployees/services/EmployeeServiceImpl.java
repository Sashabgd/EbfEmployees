package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CreateEmployeeRequest;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import lombok.Data;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeesRepository employeesRepository;
    private final CompaniesRepository companiesRepository;

    @Override
    @Transactional
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeesRepository.findAll(pageable);
    }

    @Override
    @Transactional
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Employee getEmployee(Long id) throws ResourceNotFoundException {
        Optional<Employee> res = employeesRepository.findById(id);
        if(res.isEmpty()){
            throw new ResourceNotFoundException(String.format("Employee with id %o does not exist!",id));
        }
        return res.get();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Employee createEmployee(CreateEmployeeRequest createEmployeeRequest) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(createEmployeeRequest.getCompanyId());
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id %o does not exist!",createEmployeeRequest.getCompanyId()));
        }
        Employee employee = new Employee()
                .setSurname(createEmployeeRequest.getSurname())
                .setSalary(createEmployeeRequest.getSalary())
                .setEmail(createEmployeeRequest.getEmail())
                .setAddress(createEmployeeRequest.getAddress())
                .setName(createEmployeeRequest.getName())
                .setCompany(company.get());
        employeesRepository.save(employee);
        return employee;
    }
}
