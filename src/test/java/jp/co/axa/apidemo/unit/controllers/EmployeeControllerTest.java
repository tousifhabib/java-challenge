package jp.co.axa.apidemo.unit.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setSalary(1000);
        employee.setDepartment("IT");
        Mockito.reset(employeeService);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void getEmployees() throws Exception {
        when(employeeService.retrieveEmployees()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));

        verify(employeeService, times(1)).retrieveEmployees();
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void getEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(employeeService, times(1)).getEmployee(1L);
    }


    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void getEmployee_notFound() throws Exception {
        when(employeeService.getEmployee(1L)).thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployee(1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void saveEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setSalary(1000);
        employee.setDepartment("IT");

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"salary\": 1000, \"department\": \"IT\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.salary", is(1000)))
                .andExpect(jsonPath("$.department", is("IT")));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void deleteEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(delete("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }


    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void deleteEmployee_notFound() throws Exception {
        when(employeeService.getEmployee(1L)).thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(delete("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployee(1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void updateEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(put("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Jane Doe\", \"salary\": 2000, \"department\": \"HR\" }"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void updateEmployee_notFound() throws Exception {
        doThrow(new EmployeeNotFoundException("Employee not found"))
                .when(employeeService).updateEmployee(any(Employee.class));

        mockMvc.perform(put("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Jane Doe\", \"salary\": 2000, \"department\": \"HR\" }"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void saveEmployee_invalidData() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"salary\": 1000, \"department\": \"IT\" }"))
                .andExpect(status().isBadRequest());

        verify(employeeService, times(0)).saveEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void updateEmployee_invalidData() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employee);

        mockMvc.perform(put("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"salary\": 2000, \"department\": \"HR\" }"))
                .andExpect(status().isBadRequest());

        verify(employeeService, times(0)).updateEmployee(any(Employee.class));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void getEmployee_invalidId() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{employeeId}", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(employeeService, times(0)).getEmployee(anyLong());
    }
    @Test
    public void getEmployees_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void saveEmployee_unauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"salary\": 1000, \"department\": \"IT\" }"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getEmployee_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(employeeService, times(0)).getEmployee(anyLong());
    }

    @Test
    public void updateEmployee_unauthenticated() throws Exception {
        mockMvc.perform(put("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Jane Doe\", \"salary\": 2000, \"department\": \"HR\" }"))
                .andExpect(status().isForbidden());

        verify(employeeService, times(0)).updateEmployee(any(Employee.class));
    }

    @Test
    public void deleteEmployee_unauthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{employeeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(employeeService, times(0)).deleteEmployee(anyLong());
    }
}