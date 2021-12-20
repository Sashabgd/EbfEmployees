package com.itekako.EbfEmployees.database.repositories;

import com.itekako.EbfEmployees.database.models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CompaniesRepository extends CrudRepository<Company,Long> {
    Page<Company> findAll(Pageable pageable);
}
