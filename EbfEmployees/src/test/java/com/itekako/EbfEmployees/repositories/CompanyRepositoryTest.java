package com.itekako.EbfEmployees.repositories;

import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class CompanyRepositoryTest {


    @Autowired
    private CompaniesRepository companiesRepository;
    @Autowired
    private EmployeesRepository employeesRepository;

    @Test
    public void findAllEmptyTest() {
        Page<Company> emptyResult = companiesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(0, emptyResult.getTotalElements());
    }

    @Test
    public void findAllPageSize() {
        for (int i = 0; i < 100; i++) {
            Company company = new Company()
                    .setName(String.valueOf(i));
            companiesRepository.save(company);
        }
        Page<Company> result = companiesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(10, result.get().count());
        Assert.assertEquals(100, result.getTotalElements());
        Assert.assertEquals(10, result.getTotalPages());
    }

    @Test
    public void findAllCompanyEmployees() {
        Company company = new Company().setName("test");
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
        Page<Employee> companyEmployees = employeesRepository.findAllEmployeesByCompany(company, Pageable.ofSize(11));
        Assert.assertEquals(100, companyEmployees.getTotalElements());
        Assert.assertEquals(11, companyEmployees.getNumberOfElements());
    }

    @Test
    public void findAllCompanyEmployeesOfAnotherCompany() {
        Company company = new Company().setName("test");
        Company company2 = new Company().setName("test2");
        companiesRepository.save(company);
        companiesRepository.save(company2);
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
        Page<Employee> companyEmployees = employeesRepository.findAllEmployeesByCompany(company2, Pageable.ofSize(11));
        Assert.assertEquals(0, companyEmployees.getTotalElements());
        Assert.assertEquals(0, companyEmployees.getNumberOfElements());
    }

    @Test
    public void getCompanyById(){
        Company company = new Company()
                .setName("test");
        companiesRepository.save(company);
        Optional<Company> res = companiesRepository.findById(company.getId());
        Assert.assertTrue(res.isPresent());
        Assert.assertEquals("test", res.get().getName());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveCompaniesWithSameName() {
        Company company = new Company().setName("test");
        Company company2 = new Company().setName("test");
        companiesRepository.save(company);
        companiesRepository.save(company2);
    }
}
