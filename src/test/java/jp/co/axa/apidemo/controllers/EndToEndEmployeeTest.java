package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.dto.AuthenticationRequest;
import jp.co.axa.apidemo.dto.AuthenticationResponse;
import jp.co.axa.apidemo.entities.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndEmployeeTest {

    @Autowired
    private MockMvc mockMvc;

    private static String jwtToken;
    private static Employee createdEmployee;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Order(1)
    public void authenticateUser() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "password");
        String response = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);
        jwtToken = authenticationResponse.getJwt();
    }

    @Test
    @Order(2)
    public void createEmployee() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");
        newEmployee.setSalary(1000);
        newEmployee.setDepartment("IT");

        String response = mockMvc.perform(post("/api/v1/employees")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdEmployee = objectMapper.readValue(response, Employee.class);
    }

    @Test
    @Order(3)
    public void getEmployeeById() throws Exception {
        mockMvc.perform(get("/api/v1/employees/" + createdEmployee.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(createdEmployee.getName())));
    }

    @Test
    @Order(4)
    public void getAllEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(createdEmployee.getName())));
    }
    @Test
    @Order(5)
    public void updateEmployee() throws Exception {
        createdEmployee.setName("Jane Doe");
        mockMvc.perform(put("/api/v1/employees/" + createdEmployee.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(createdEmployee.getName())));
    }

    @Test
    @Order(6)
    public void deleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/" + createdEmployee.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}