package jp.co.axa.apidemo.unit.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeServiceImplTest {

    @InjectMocks
    EmployeeServiceImpl employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));
        assert(employeeService.retrieveEmployees().size() == 2);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assert(employeeService.getEmployee(1L).getId().equals(1L));
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployee_notFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            employeeService.getEmployee(1L);
        } catch (EmployeeNotFoundException e) {
            assert(e.getMessage().equals("Employee with id 1 was not found"));
        }
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveEmployee() {
        Employee employee = new Employee();
        employeeService.saveEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployee_notFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);
        try {
            employeeService.deleteEmployee(1L);
        } catch (EmployeeNotFoundException e) {
            assert(e.getMessage().equals("Employee with id 1 was not found"));
        }
        verify(employeeRepository, times(1)).existsById(1L);
    }

    @Test
    public void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.existsById(1L)).thenReturn(true);
        employeeService.updateEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testUpdateEmployee_notFound() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(employeeRepository.existsById(1L)).thenReturn(false);
        try {
            employeeService.updateEmployee(employee);
        } catch (EmployeeNotFoundException e) {
            assert(e.getMessage().equals("Employee with id 1 was not found"));
        }
        verify(employeeRepository, times(1)).existsById(1L);
    }

    @Test
    public void testSaveEmployee_nullEmployee() {
        try {
            employeeService.saveEmployee(null);
        } catch (NullPointerException e) {
            assert(e.getMessage().equals("Employee must not be null"));
        }
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_nullEmployee() {
        try {
            employeeService.updateEmployee(null);
        } catch (NullPointerException e) {
            assert(e.getMessage().equals("Employee must not be null"));
        }
        verify(employeeRepository, times(0)).existsById(anyLong());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_nullId() {
        Employee employee = new Employee();
        try {
            employeeService.updateEmployee(employee);
        } catch (NullPointerException e) {
            assert(e.getMessage().equals("Employee id must not be null"));
        }
        verify(employeeRepository, times(0)).existsById(anyLong());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }
}
