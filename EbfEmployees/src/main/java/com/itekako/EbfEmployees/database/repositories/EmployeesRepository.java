package com.itekako.EbfEmployees.database.repositories;

import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface EmployeesRepository extends CrudRepository<Employee,Long> {
    Page<Employee> findAllEmployeesByCompany(Company company, Pageable pageable);
}
