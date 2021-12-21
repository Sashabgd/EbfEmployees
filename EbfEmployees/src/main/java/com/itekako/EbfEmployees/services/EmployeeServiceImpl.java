package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import lombok.Data;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeesRepository employeesRepository;

    @Override
    @Transactional
    @Retryable(value = CannotAcquireLockException.class,backoff = @Backoff(delay = 100),maxAttempts = 15)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeesRepository.findAll(pageable);
    }
}
