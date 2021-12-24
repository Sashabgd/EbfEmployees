package com.itekako.EbfEmployees.services;

import com.itekako.EbfEmployees.Dtos.EmployeeDetails;
import com.itekako.EbfEmployees.database.models.Company;
import com.itekako.EbfEmployees.database.models.Employee;
import com.itekako.EbfEmployees.database.repositories.CompaniesRepository;
import com.itekako.EbfEmployees.database.repositories.EmployeesRepository;
import com.itekako.EbfEmployees.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    private static final int SOME_SIZE = 111;
    private static final long TEST_EMPLOYEE_ID = 666;
    private static final long TEST_COMPANY_ID = 53;

    @Mock
    private Page<Employee> employeesMocks;

    @Mock
    private CompaniesRepository companiesRepository;

    @Mock
    private EmployeesRepository employeesRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Optional<Employee> testEmployee;

    @Mock
    private Company companyMock;

    Optional<Company> company;

    @BeforeEach
    public void setup() {
        when(employeesRepository.findAll(any(Pageable.class))).thenReturn(employeesMocks);


        testEmployee = Optional.of(new Employee()
                .setName("empName")
                .setAddress("empAddress")
                .setCompany(new Company().setName("compName").setId(TEST_COMPANY_ID))
                .setSalary(111)
                .setSurname("empSurname")
                .setId(TEST_EMPLOYEE_ID)
                .setEmail("empEmail"));

        when(employeesRepository.findById(any(long.class))).thenReturn(Optional.empty());
        when(employeesRepository.findById(TEST_EMPLOYEE_ID)).thenReturn(testEmployee);

        company = Optional.of(companyMock);
        when(companiesRepository.findById(any(long.class))).thenReturn(Optional.empty());
        when(companiesRepository.findById(TEST_COMPANY_ID)).thenReturn(company);
        when(companyMock.getId()).thenReturn(TEST_COMPANY_ID);

    }

    @Test
    public void getAllEmployees() {
        Pageable pageable = Pageable.ofSize(SOME_SIZE);
        Page<Employee> allEmployees = employeeService.getAllEmployees(pageable);
        verify(employeesRepository, times(1)).findAll(pageable);
        Assert.assertSame(employeesMocks, allEmployees);
    }

    @Test
    public void getEmployee() throws ResourceNotFoundException {
        Employee employee = employeeService.getEmployee(TEST_EMPLOYEE_ID);
        verify(employeesRepository, times(1)).findById(TEST_EMPLOYEE_ID);
        Assert.assertSame(testEmployee.get(), employee);
    }

    @Test
    public void findNonExistingEmployee() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployee(22L));
        verify(employeesRepository, times(1)).findById(22L);
    }

    @Test
    public void createEmployee() throws ResourceNotFoundException {
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(companyMock.getId())
                .setName("testName")
                .setSurname("testSurname")
                .setEmail("testEmail")
                .setSalary(111)
                .setAddress("testAddress");
        Employee employee = employeeService.createEmployee(employeeDetails);

        Assert.assertSame(companyMock, employee.getCompany());
        Assert.assertEquals("testName", employee.getName());
        Assert.assertEquals("testSurname", employee.getSurname());
        Assert.assertEquals("testEmail", employee.getEmail());
        Assert.assertEquals(111D, employee.getSalary(), 0);
        Assert.assertEquals("testAddress", employee.getAddress());
        verify(employeesRepository, times(1)).save(employee);
        verify(companiesRepository, times(1)).findById(companyMock.getId());
    }

    @Test
    public void createEmployeeNonExistingCompany() throws ResourceNotFoundException {
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(555)
                .setName("testName")
                .setSurname("testSurname")
                .setEmail("testEmail")
                .setSalary(111)
                .setAddress("testAddress");
        Assert.assertThrows(ResourceNotFoundException.class,()-> employeeService.createEmployee(employeeDetails));
        verify(employeesRepository, times(0)).save(any(Employee.class));
        verify(companiesRepository, times(1)).findById(555L);
    }

    @Test
    public void updateEmployee() throws ResourceNotFoundException {
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(TEST_COMPANY_ID)
                .setName("testName")
                .setSurname("testSurname")
                .setEmail("testEmail")
                .setSalary(111)
                .setAddress("testAddress");
        Employee employee = employeeService.updateEmployee(TEST_EMPLOYEE_ID, employeeDetails);
        verify(employeesRepository,times(1)).findById(TEST_EMPLOYEE_ID);
        verify(employeesRepository,times(1)).save(employee);
        verify(companiesRepository,times(1)).findById(TEST_COMPANY_ID);
        Assert.assertSame(companyMock, employee.getCompany());
        Assert.assertEquals(employeeDetails.getName(), employee.getName());
        Assert.assertEquals(employeeDetails.getSurname(), employee.getSurname());
        Assert.assertEquals(employeeDetails.getEmail(), employee.getEmail());
        Assert.assertEquals(employeeDetails.getSalary(), employee.getSalary(), 0);
        Assert.assertEquals(employeeDetails.getAddress(), employee.getAddress());
    }

    @Test
    public void updateNonExistingEmployee(){
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(TEST_COMPANY_ID)
                .setName("testName")
                .setSurname("testSurname")
                .setEmail("testEmail")
                .setSalary(111)
                .setAddress("testAddress");
        Assert.assertThrows(ResourceNotFoundException.class,()->employeeService.updateEmployee(444L,employeeDetails));
        verify(employeesRepository,times(1)).findById(444L);
        verify(employeesRepository,times(0)).save(any(Employee.class));
        verify(companiesRepository,times(0)).findById(any(long.class));
    }

    @Test
    public void updateEmployeeNonExistingCompany(){
        EmployeeDetails employeeDetails = new EmployeeDetails()
                .setCompanyId(444L)
                .setName("testName")
                .setSurname("testSurname")
                .setEmail("testEmail")
                .setSalary(111)
                .setAddress("testAddress");
        Assert.assertThrows(ResourceNotFoundException.class,()->employeeService.updateEmployee(TEST_EMPLOYEE_ID,employeeDetails));
        verify(employeesRepository,times(1)).findById(TEST_EMPLOYEE_ID);
        verify(employeesRepository,times(0)).save(any(Employee.class));
        verify(companiesRepository,times(1)).findById(444L);
    }

    @Test
    public void deleteEmployee() throws ResourceNotFoundException {
        employeeService.deleteEmployee(TEST_EMPLOYEE_ID);
        verify(employeesRepository,times(1)).findById(TEST_EMPLOYEE_ID);
        verify(employeesRepository,times(1)).delete(testEmployee.get());
    }

    @Test
    public void deleteNonExistingEmployee() throws ResourceNotFoundException {
        Assert.assertThrows(ResourceNotFoundException.class,()->employeeService.deleteEmployee(544L));
        verify(employeesRepository,times(1)).findById(544L);
        verify(employeesRepository,times(0)).delete(any(Employee.class));
    }
}
