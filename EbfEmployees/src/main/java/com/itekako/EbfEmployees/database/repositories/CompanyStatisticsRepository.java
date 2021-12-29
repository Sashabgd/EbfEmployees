package com.itekako.EbfEmployees.database.repositories;

import com.itekako.EbfEmployees.database.models.CompanySalaryStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface CompanyStatisticsRepository extends JpaRepository<CompanySalaryStats, Serializable> {
    @Query(value = "select c.id,c.name,COALESCE(AVG(e.salary),0) as avg_salary from companies c left join employees e on e.company_id = c.id group by c.id",
    countQuery = "select COUNT(*) from companies",
    nativeQuery = true)
    Page<CompanySalaryStats> getCompaniesAvgSalary(Pageable pageable);

    @Query("select new com.itekako.EbfEmployees.database.models.CompanySalaryStats(c.id,c.name,AVG(e.salary) as avgSalary) " +
            "from Employee e " +
            "join e.company c " +
            "where c.id =:id " +
            "group by c")
    Optional<CompanySalaryStats> getAvgSalaryForCompany(Long id);
}
