package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.CompanyDetails;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {
    @Mock
    private CompaniesRepository companiesRepository;

    @Mock
    EmployeesRepository employeesRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private final Company testCompanyObject = new Company().setName("test").setId(112L);

    @Before
    public void setup(){
        when(companiesRepository.findById(112L)).thenReturn(Optional.of(testCompanyObject));
    }

    @Test
    public void getCompany() throws ResourceNotFoundException {
        Company res = companyService.getCompany(112L);
        Assert.assertEquals(112L,res.getId());
        Assert.assertEquals("test",res.getName());
        verify(companiesRepository,times(1)).findById(112L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getNonExistingCompany() throws ResourceNotFoundException {
        try {
            companyService.getCompany(1L);
        }finally {
            verify(companiesRepository,times(1)).findById(1L);
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findCompanyByNull() throws ResourceNotFoundException {
        try {
            companyService.getCompany(null);
        }finally {
            verify(companiesRepository,times(1)).findById(null);
        }
    }

    @Test
    public void createCompany(){
        CompanyDetails companyDetails = new CompanyDetails()
                .setName("testc");
        companyService.createCompany(companyDetails);
        ArgumentCaptor<Company> argumentCaptor = ArgumentCaptor.forClass(Company.class);
        verify(companiesRepository,times(1)).save(argumentCaptor.capture());
        Assert.assertEquals("testc",argumentCaptor.getValue().getName());
    }

    @Test(expected = NullPointerException.class)
    public void createNullCompany(){
        try {
            companyService.createCompany(null);
        }finally {
            verify(companiesRepository,never()).save(any(Company.class));
        }
    }

    @Test
    public void getCompanyEmployees() throws ResourceNotFoundException {
        Pageable pageable = Pageable.ofSize(10);
        companyService.getAllEmployeesForCompany(112L,pageable);
        verify(employeesRepository,times(1)).findAllEmployeesByCompany(testCompanyObject,pageable);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getNonExistingCompanyEmployees() throws ResourceNotFoundException {
        try {
            companyService.getAllEmployeesForCompany(1, Pageable.ofSize(19));
        }finally {
            verify(employeesRepository,never()).findAllEmployeesByCompany(any(Company.class),any(Pageable.class));
        }
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

    @Test(expected = ResourceNotFoundException.class)
    public void updateNonExistingCompany() throws ResourceNotFoundException {
        try {
            companyService.updateCompany(1L,new CompanyDetails());
        }finally {
            verify(companiesRepository,times(1)).findById(1L);
            verify(companiesRepository,never()).save(any(Company.class));
        }
    }

    @Test
    public void deleteCompany() throws ResourceNotFoundException {
        companyService.deleteCompany(112L);
        verify(companiesRepository,times(1)).findById(112L);
        verify(companiesRepository,times(1)).delete(testCompanyObject);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteNonExistingCompany() throws ResourceNotFoundException {
        try {
            companyService.deleteCompany(1L);
        }finally {
            verify(companiesRepository,times(1)).findById(1L);
            verify(companiesRepository,never()).delete(any(Company.class));
        }
    }

    @Test
    public void findAllCompanies(){
        Pageable pageable = Pageable.ofSize(1);
        companyService.getAllCompanies(pageable);
        verify(companiesRepository,times(1)).findAll(pageable);
    }
}
