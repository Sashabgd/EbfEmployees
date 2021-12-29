package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.CompanySalaryStats;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.CompanyStatisticsRepository;
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

@Data
@Service
public class CompanyServiceImpl implements CompanyService{

    private final CompaniesRepository companiesRepository;
    private final EmployeesRepository employeesRepository;
    private final CompanyStatisticsRepository companyStatisticsRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Page<Employee> getAllEmployeesForCompany(long companyId, Pageable pageable) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(companyId);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id %o does not exist!",companyId));
        }
        Page<Employee> employees = employeesRepository.findAllEmployeesByCompany(company.get(),pageable);
        return employees;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public long createCompany(CompanyDetails createCompanyRequest) {
        Company company = new Company()
                .setName(createCompanyRequest.getName());
        companiesRepository.save(company);
        return company.getId();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Company getCompany(Long id) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(id);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id %o does not exist!",id));
        }
        return company.get();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Company updateCompany(Long id, CompanyDetails companyDetails) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(id);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id %o does not exist!",id));
        }
        company.get().setName(companyDetails.getName());
        companiesRepository.save(company.get());
        return company.get();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public void deleteCompany(Long id) throws ResourceNotFoundException {
        Company company = companiesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Company with id %o does not exist!", id)));
        companiesRepository.delete(company);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Page<Company> getAllCompanies(Pageable pageable) {
        return companiesRepository.findAll(pageable);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public CompanySalaryStats getAvgSalary(Long id) throws ResourceNotFoundException {
        Optional<Company> company = companiesRepository.findById(id);
        Optional<CompanySalaryStats> avgSalaryForCompany = companyStatisticsRepository.getAvgSalaryForCompany(id);
        if(company.isEmpty()){
            throw new ResourceNotFoundException(String.format("Company with id %o does not exist!",id));
        }
        if(avgSalaryForCompany.isEmpty()){
            return new CompanySalaryStats().setAvgSalary(0).setName(company.get().getName()).setId(company.get().getId());
        }
        return avgSalaryForCompany.get();
    }

    @Override
    public Page<CompanySalaryStats> getCompaniesAvgSalary(Pageable pageable) {
        return companyStatisticsRepository.getCompaniesAvgSalary(pageable);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public void generateCompanies() {
        for(int i=0;i<102;i++){
            Company company = new Company()
                    .setName("Company "+i);
            companiesRepository.save(company);
        }
    }
}
