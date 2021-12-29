package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.CompanySalaryStats;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.CompanyStatisticsRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompaniesRepository companiesRepository;

    @Mock
    private EmployeesRepository employeesRepository;

    @Mock
    private CompanyStatisticsRepository companyStatisticsRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private final Company testCompanyObject = new Company().setName("test").setId(112L);

    @BeforeEach
    public void setup() {
        when(companiesRepository.findById(112L)).thenReturn(Optional.of(testCompanyObject));
    }

    @Test
    public void getCompany() throws ResourceNotFoundException {
        Company res = companyService.getCompany(112L);
        Assert.assertEquals(112L,res.getId());
        Assert.assertEquals("test",res.getName());
        verify(companiesRepository,times(1)).findById(112L);
    }

    @Test
    public void getNonExistingCompany() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.getCompany(1L));
        verify(companiesRepository, times(1)).findById(1L);
    }

    @Test
    public void findCompanyByNull() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.getCompany(null));
        verify(companiesRepository, times(1)).findById(null);
    }

    @Test
    public void createCompany() {
        CompanyDetails companyDetails = new CompanyDetails()
                .setName("testc");
        companyService.createCompany(companyDetails);
        ArgumentCaptor<Company> argumentCaptor = ArgumentCaptor.forClass(Company.class);
        verify(companiesRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals("testc", argumentCaptor.getValue().getName());
    }

    @Test
    public void createNullCompany() {
        Assert.assertThrows(NullPointerException.class, () -> companyService.createCompany(null));
        verify(companiesRepository, never()).save(any(Company.class));
    }

    @Test
    public void getCompanyEmployees() throws ResourceNotFoundException {
        Pageable pageable = Pageable.ofSize(10);
        companyService.getAllEmployeesForCompany(112L, pageable);
        verify(employeesRepository, times(1)).findAllEmployeesByCompany(testCompanyObject, pageable);
    }

    @Test
    public void getNonExistingCompanyEmployees() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.getAllEmployeesForCompany(1, Pageable.ofSize(19)));
        verify(employeesRepository, never()).findAllEmployeesByCompany(any(Company.class), any(Pageable.class));
    }

    @Test
    public void updateCompany() throws ResourceNotFoundException {
        Company res = companyService.updateCompany(112L,new CompanyDetails().setName("newName"));
        ArgumentCaptor<Company> argumentCaptor = ArgumentCaptor.forClass(Company.class);
        verify(companiesRepository,times(1)).findById(112L);
        verify(companiesRepository,times(1)).save(argumentCaptor.capture());
        Assert.assertEquals("newName",argumentCaptor.getValue().getName());
        Assert.assertSame(res,testCompanyObject);
    }

    @Test
    public void updateNonExistingCompany() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.updateCompany(1L, new CompanyDetails()));
        verify(companiesRepository, times(1)).findById(1L);
        verify(companiesRepository, never()).save(any(Company.class));
    }

    @Test
    public void deleteCompany() throws ResourceNotFoundException {
        companyService.deleteCompany(112L);
        verify(companiesRepository,times(1)).findById(112L);
        verify(companiesRepository,times(1)).delete(testCompanyObject);
    }

    @Test
    public void deleteNonExistingCompany() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.deleteCompany(1L));
        verify(companiesRepository, times(1)).findById(1L);
        verify(companiesRepository, never()).delete(any(Company.class));
    }

    @Test
    public void findAllCompanies() {
        Pageable pageable = Pageable.ofSize(1);
        companyService.getAllCompanies(pageable);
        verify(companiesRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAvgSalaryForCompany() throws ResourceNotFoundException {
        CompanySalaryStats companySalaryStats = new CompanySalaryStats();
        when(companyStatisticsRepository.getAvgSalaryForCompany(any(Long.class))).thenReturn(Optional.of(companySalaryStats));
        when(companiesRepository.findById(any(Long.class))).thenReturn(Optional.of(new Company()));
        CompanySalaryStats avgSalary = companyService.getAvgSalary(55l);
        verify(companyStatisticsRepository, times(1)).getAvgSalaryForCompany(55L);
        Assert.assertSame(companySalaryStats, avgSalary);
    }

    @Test
    public void getAvgSalaryForNonExistingCompany() throws ResourceNotFoundException {
        when(companyStatisticsRepository.getAvgSalaryForCompany(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(ResourceNotFoundException.class, () -> companyService.getAvgSalary(55l));
        verify(companyStatisticsRepository, times(1)).getAvgSalaryForCompany(55L);
    }

    @Test
    public void getAvgSalaryForCompanies(){
        Pageable pageable = Pageable.ofSize(10);
        companyService.getCompaniesAvgSalary(pageable);
        verify(companyStatisticsRepository,times(1)).getCompaniesAvgSalary(pageable);
    }
}
