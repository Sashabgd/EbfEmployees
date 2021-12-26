package com.itekako.EbfEmployees.controllers;

import Utils.JwtUtils;
import Utils.PageModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.configurations.AuthConfiguration;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.CompanySalaryStats;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@EnableTransactionManagement
@Transactional
public class CompanyControllerTest {

    private ObjectMapper objectMapper;


    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private AuthConfiguration authConfiguration;

    private JwtUtils jwtUtils;


    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new PageModule());
        jwtUtils = new JwtUtils(authConfiguration);
    }

    @Test
    public void getCompanyEndpoint() throws Exception {
        Company company = new Company().setName("testCompany");
        companiesRepository.save(company);
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/" + company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Company companyFomHttp = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Company.class);
        Assert.assertEquals(company.getId(), companyFomHttp.getId());
        Assert.assertEquals(company.getName(), companyFomHttp.getName());
    }

    @Test
    public void getCompanyEndpointNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/" + 1)
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAllCompanies() throws Exception {
        for (int i = 0; i < 10; i++) {
            Company company = new Company().setName("" + i);
            companiesRepository.save(company);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Page<Company> companies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<Page<Company>>() {
        });
        Assert.assertEquals(10, companies.getTotalElements());
    }

    @Test
    public void getAllCompaniesWithoutJwtToken() throws Exception {
        for (int i = 0; i < 10; i++) {
            Company company = new Company().setName("" + i);
            companiesRepository.save(company);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void getAllCompaniesWithExpiredJwtToken() throws Exception {
        for (int i = 0; i < 10; i++) {
            Company company = new Company().setName("" + i);
            companiesRepository.save(company);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessTokenExpired()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void getAllCompaniesWithWrongSecretJwtToken() throws Exception {
        for (int i = 0; i < 10; i++) {
            Company company = new Company().setName("" + i);
            companiesRepository.save(company);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessTokenWrongSecret()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void getAllCompaniesWithoutRoleJwtToken() throws Exception {
        for (int i = 0; i < 10; i++) {
            Company company = new Company().setName("" + i);
            companiesRepository.save(company);
        }
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessWithoutRole()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void createCompany() throws Exception {
        mockMvc.perform(post("/api/companies")
                .content(objectMapper.writeValueAsString(new CompanyDetails().setName("test")))
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        Page<Company> all = companiesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(1,all.getTotalElements());
    }

    @Test
    public void createCompanyWithSameName() throws Exception {
        mockMvc.perform(post("/api/companies")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
                .content(objectMapper.writeValueAsString(new CompanyDetails().setName("test")))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
        Page<Company> all = companiesRepository.findAll(Pageable.ofSize(10));
        Assert.assertEquals(1,all.getTotalElements());
        mockMvc.perform(post("/api/companies")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
                .content(objectMapper.writeValueAsString(new CompanyDetails().setName("test")))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCompanyWithNullName() throws Exception {
        mockMvc.perform(post("/api/companies")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
                .content(objectMapper.writeValueAsString(new CompanyDetails()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCompany() throws Exception {
        Company company = new Company().setName("testCompany");
        companiesRepository.save(company);
        mockMvc.perform(put("/api/companies/{id}", company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(new CompanyDetails().setName("newName"))))
                .andExpect(status().isOk());
        Optional<Company> persisted = companiesRepository.findById(company.getId());
        Assert.assertTrue(persisted.isPresent());
        Assert.assertEquals("newName",persisted.get().getName());
    }

    @Test
    public void updateNonExistingCompany() throws Exception {
        mockMvc.perform(put("/api/companies/{id}",22)
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new CompanyDetails().setName("newName"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompanyWithNullNewName() throws Exception {
        Company company = new Company().setName("testCompany");
        companiesRepository.save(company);
        mockMvc.perform(put("/api/companies/{id}",company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new CompanyDetails())))
                .andExpect(status().isBadRequest());
        Optional<Company> persisted = companiesRepository.findById(company.getId());
        Assert.assertTrue(persisted.isPresent());
        Assert.assertEquals("testCompany",persisted.get().getName());
    }

    @Test
    public void deleteCompany() throws Exception {
        Company company = new Company().setName("testCompany");
        companiesRepository.save(company);
        mockMvc.perform(delete("/api/companies/{id}",company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isNoContent());
        Optional<Company> persisted = companiesRepository.findById(company.getId());
        Assert.assertFalse(persisted.isPresent());
    }

    @Test
    public void deleteNonExistingCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/{id}",22)
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCompanyEmployees() throws Exception {
        Company company = new Company().setName("testCompany");
        companiesRepository.save(company);
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
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/{id}/employees",company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isOk())
                .andReturn();
        Page<Employee> employees = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),new TypeReference<Page<Employee>>(){});
        Assert.assertEquals(23,employees.getTotalElements());
    }

    @Test
    public void getCompanySalary() throws Exception {
        Company company = new Company().setName("companyName");
        companiesRepository.save(company);
        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/{id}/avg-salary", company.getId())
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isOk()).andReturn();
        CompanySalaryStats companySalaryStats = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CompanySalaryStats.class);
        Assert.assertEquals(111,companySalaryStats.getAvgSalary(),0);
        Assert.assertEquals(company.getId(),companySalaryStats.getId());
        Assert.assertEquals(company.getName(),companySalaryStats.getName());
    }

    @Test
    public void getNonExistingCompanySalary() throws Exception {
        mockMvc.perform(get("/api/companies/{id}/avg-salary", 55)
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void getCompaniesAvgSalary() throws Exception {
        Company company = new Company().setName("companyName");
        companiesRepository.save(company);
        Employee employee = new Employee()
                .setCompany(company)
                .setAddress("address")
                .setEmail("email")
                .setSalary(111)
                .setSurname("surname")
                .setName("name");
        employeesRepository.save(employee);
        MvcResult mvcResult = mockMvc.perform(get("/api/companies/avg-salaries")
                .header("Authorization", "Bearer " + jwtUtils.generateAccessToken()))
                .andExpect(status().isOk()).andReturn();
        Page<CompanySalaryStats> companySalaryStats = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<Page<CompanySalaryStats>>(){});
        Assert.assertEquals(1,companySalaryStats.getTotalElements());
        Assert.assertEquals(company.getId(),companySalaryStats.get().findFirst().get().getId());
        Assert.assertEquals(111.0,companySalaryStats.get().findFirst().get().getAvgSalary(),0);
    }
}
