package com.itekako.EbfEmployees.database.repositories;

import com.itekako.EbfEmployees.database.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeesRepository extends CrudRepository<Employee,Long> {
}
