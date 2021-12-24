package com.itekako.EbfEmployees.controllers;

import Utils.PageModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itekako.EbfEmployees.Dtos.EmployeeDetails;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@EnableTransactionManagement
@Transactional
public class EmployeeControllerTest {

    private ObjectMapper objectMapper;


    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    private Company company;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new PageModule());
        company = new Company().setName("testCompany");
        companiesRepository.save(company);
    }

    @Test
    public void getAllEmployees() throws Exception {
        for (int i = 0; i < 23; i++) {
            Employee employee = new Employee()
                    .setCompany(company)
                    .setAddress("address" + i)
                    .setEmail("email" + i)
                    .setSalary(i)
                    .setSurname("surname" + i)
                    .setName("name" + i);
            employeesRepository.save(employee);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/employees/"))
                .andExpect(status().isOk())
                .andReturn();
        Page<Employee> employees = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Page<Employee>>() {
        });
        Assert.assertEquals(23, employees.getTotalElements());
    }

    @Test
    public void getEmployee() throws Exception {
        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);
        MvcResult mvcResult = mockMvc.perform(get("/api/employees/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Employee employeeResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                Employee.class);
        Assert.assertEquals(employee.getId(), employeeResult.getId());
        Assert.assertEquals(employee.getSurname(), employeeResult.getSurname());
        Assert.assertEquals(employee.getSalary(), employeeResult.getSalary(), 0);
        Assert.assertEquals(employee.getEmail(), employeeResult.getEmail());
        Assert.assertEquals(employee.getCompany().getId(), employeeResult.getCompany().getId());
        Assert.assertEquals(employee.getAddress(), employeeResult.getAddress());
        Assert.assertEquals(employee.getName(), employeeResult.getName());
    }

    @Test
    public void getNonExistingEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/{id}", 444))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource({
            ",,,1,",
            ",surname,email,1,address",
            "name,,email,1,address",
            "name,surname,,1,address",
            "name,surname,email,1,"
    })
    public void createEmployeeNullCase(String name, String surname, String email, double salary, String address) throws Exception {
        ObjectMapper objectMapper1 = new ObjectMapper(); //BEFORE is not working in ParameterizedTest
        objectMapper1.configure(SerializationFeature.INDENT_OUTPUT, true);
        Company company = new Company().setName("company");
        companiesRepository.save(company);
        EmployeeDetails employee = new EmployeeDetails()
                .setName(name)
                .setSalary(salary)
                .setEmail(email)
                .setSurname(surname)
                .setAddress(address)
                .setCompanyId(company.getId());

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper1.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployee() throws Exception {
        EmployeeDetails employee = new EmployeeDetails()
                .setCompanyId(company.getId())
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated());
        Page<Employee> all = employeesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(1, all.getTotalElements());
    }

    @Test
    public void updateEmployee() throws Exception {
        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(company.getId())
                .setAddress("newAddress")
                .setEmail("newEmail")
                .setSalary(222)
                .setSurname("newSurname")
                .setName("newName");

        mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .content(objectMapper.writeValueAsString(employeeDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        Optional<Employee> persisted = employeesRepository.findById(employee.getId());
        Assert.assertTrue(persisted.isPresent());
        Assert.assertEquals(employee.getId(), persisted.get().getId());
        Assert.assertEquals("newAddress", persisted.get().getAddress());
        Assert.assertEquals("newName", persisted.get().getName());
        Assert.assertEquals("newAddress", persisted.get().getAddress());
        Assert.assertEquals("newSurname", persisted.get().getSurname());
        Assert.assertEquals(222, persisted.get().getSalary(), 0);
    }

    @ParameterizedTest
    @CsvSource({
            ",,,1,",
            ",surname,email,1,address",
            "name,,email,1,address",
            "name,surname,,1,address",
            "name,surname,email,1,"
    })
    public void updateEmployeeNullCase(String name, String surname, String email, double salary, String address) throws Exception {

        ObjectMapper objectMapper1 = new ObjectMapper(); //BEFORE is not working in ParameterizedTest
        objectMapper1.configure(SerializationFeature.INDENT_OUTPUT, true);
        Company company = new Company().setName("company");
        companiesRepository.save(company);

        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");

        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setName(name)
                .setSalary(salary)
                .setEmail(email)
                .setSurname(surname)
                .setAddress(address)
                .setCompanyId(company.getId());

        mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .content(objectMapper1.writeValueAsString(employeeDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
    }

    @Test
    public void updateNonExistingEmployee() throws Exception {
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(company.getId())
                .setAddress("newAddress")
                .setEmail("newEmail")
                .setSalary(222)
                .setSurname("newSurname")
                .setName("newName");

        mockMvc.perform(put("/api/employees/{id}", 222)
                .content(objectMapper.writeValueAsString(employeeDetails))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployee() throws Exception {
        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);
        mockMvc.perform(delete("/api/employees/{id}",employee.getId()))
                .andExpect(status().isNoContent());
        Optional<Employee> byId = employeesRepository.findById(employee.getId());
        Assert.assertFalse(byId.isPresent());
    }

    @Test
    public void deleteNonExistingEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/{id}",222))
                .andExpect(status().isNotFound());
    }
}
