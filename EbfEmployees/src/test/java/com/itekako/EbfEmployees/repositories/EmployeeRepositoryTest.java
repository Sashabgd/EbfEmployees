package com.itekako.EbfEmployees.repositories;

import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class EmployeeRepositoryTest {

    @Autowired
    private CompaniesRepository companiesRepository;
    @Autowired
    private EmployeesRepository employeesRepository;

    private Company companyTestObject;

    @Before
    public void setup() {
        companyTestObject = new Company()
                .setName("testCompanyName");
        companiesRepository.save(companyTestObject);
    }

    @Test
    public void findEmptyEmployees() {
        Page<Employee> all = employeesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(0, all.getTotalElements());
    }

    @Test
    public void findNonExistingEmployee() {
        Optional<Employee> employee = employeesRepository.findById(2222L);
        Assert.assertFalse(employee.isPresent());
    }

    @Test
    public void saveAndGetEmployee() {
        Employee employee = new Employee()
                .setCompany(companyTestObject)
                .setAddress("address")
                .setEmail("email")
                .setSalary(100)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);

        Optional<Employee> persistedEmployee = employeesRepository.findById(employee.getId());
        Assert.assertTrue(persistedEmployee.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            ",,,1,",
            ",surname,email,1,address",
            "name,,email,1,address",
            "name,surname,,1,address",
            "name,surname,email,1,"
    })
    public void testNullCase(String name, String surname, String email, double salary, String address) {
        companyTestObject = new Company().setName("company");
        companiesRepository.save(companyTestObject);
        Employee employee = new Employee()
                .setName(name)
                .setSalary(salary)
                .setEmail(email)
                .setSurname(surname)
                .setAddress(address)
                .setCompany(companyTestObject);

        Assert.assertThrows(DataIntegrityViolationException.class, () -> employeesRepository.save(employee));
    }

    @Test
    public void testNullCompany() {
        Employee employee = new Employee()
                .setCompany(null)
                .setAddress("address")
                .setEmail("email")
                .setSalary(100)
                .setSurname("surname")
                .setName("name");
        Assert.assertThrows(DataIntegrityViolationException.class, () -> employeesRepository.save(employee));
    }

    @ParameterizedTest
    @CsvSource({
            "100,9,9",
            "999,7,7",
            "11,100,11",
            "11,11,11"
    })
    public void testPage(int employeesCount, int pageSize,int employeesCountExpected) {
        companyTestObject = new Company().setName("company");
        companiesRepository.save(companyTestObject);
        for (int i = 0; i < employeesCount; i++) {
            Employee employee = new Employee()
                    .setCompany(companyTestObject)
                    .setAddress("address" + i)
                    .setEmail("email" + i)
                    .setSalary(i)
                    .setSurname("surname" + i)
                    .setName("name" + i);
            employeesRepository.save(employee);
        }
        Page<Employee> all = employeesRepository.findAll(Pageable.ofSize(pageSize));
        Assert.assertEquals(employeesCountExpected, all.getNumberOfElements());
        Assert.assertEquals(employeesCount, all.getTotalElements());
    }

    @Test
    public void testEmployeeParametersValues(){
        Employee employee = new Employee()
                .setCompany(companyTestObject)
                .setAddress("address")
                .setEmail("email")
                .setSalary(100)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);

        Optional<Employee> persistedEmployee = employeesRepository.findById(employee.getId());
        Assertions.assertEquals(persistedEmployee.get(),employee);
        Assert.assertEquals(persistedEmployee.get().getAddress(),employee.getAddress());
        Assert.assertEquals(persistedEmployee.get().getName(),employee.getName());
        Assert.assertEquals(persistedEmployee.get().getCompany(),employee.getCompany());
        Assert.assertEquals(persistedEmployee.get().getEmail(),employee.getEmail());
        Assert.assertEquals(persistedEmployee.get().getSalary(),employee.getSalary(),0);
        Assert.assertEquals(persistedEmployee.get().getSurname(),employee.getSurname());
    }
}
