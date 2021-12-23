package com.itekako.EbfEmployees.repositories;

import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.CompanySalaryStats;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.CompanyStatisticsRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class CompanyStatisticRepositoryTest {

    @Autowired
    private CompanyStatisticsRepository companyStatisticsRepository;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Test
    public void companySalaryTest() {
        Company company = new Company().setName("companyName");
        companiesRepository.save(company);
        for (int i = 0; i < 100; i++) {
            Employee employee = new Employee()
                    .setCompany(company)
                    .setName("name")
                    .setAddress("address")
                    .setEmail(String.format("%o@domain.tdl", i))
                    .setSalary(i)
                    .setSurname("surname");
            employeesRepository.save(employee);
        }

        Optional<CompanySalaryStats> avgSalaryForCompany = companyStatisticsRepository.getAvgSalaryForCompany(company.getId());
        Assert.assertTrue(avgSalaryForCompany.isPresent());
        Assert.assertEquals(49.5, avgSalaryForCompany.get().getAvgSalary(), 0);
    }

    @Test
    public void getNonExistingCompanyAvgSalary(){
        Optional<CompanySalaryStats> avgSalaryForCompany = companyStatisticsRepository.getAvgSalaryForCompany(55L);
        Assert.assertFalse(avgSalaryForCompany.isPresent());
    }

    @Test
    public void companySalaryPageCountTest() {
        Company company = new Company().setName("companyName");
        Company company1 = new Company().setName("anotherCompany");
        companiesRepository.save(company);
        companiesRepository.save(company1);
        for (int i = 0; i < 100; i++) {
            Employee employee = new Employee()
                    .setCompany(i % 2 == 0 ? company1 : company)
                    .setName("name")
                    .setAddress("address")
                    .setEmail(String.format("%o@domain.tdl", i))
                    .setSalary(i)
                    .setSurname("surname");
            employeesRepository.save(employee);
        }


        Page<CompanySalaryStats> avgSalary = companyStatisticsRepository.getCompaniesAvgSalary(Pageable.ofSize(10));
        Assert.assertEquals(2, avgSalary.getTotalElements());
    }
}
