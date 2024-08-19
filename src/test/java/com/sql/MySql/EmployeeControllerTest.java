package com.sql.MySql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.MySql.helpers.ManagerChangeRequest;
import com.sql.MySql.models.EmployeeManagerModel;
import com.sql.MySql.response.ManagerChangeResponseDTO;
import com.sql.MySql.response.ResponseDTO;
import com.sql.MySql.response.ResponseMessage;
import com.sql.MySql.services.EmployeeManagerService;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeManagerService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // Auto-configured by Spring Boot

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployeesFromController() throws Exception {
        // Create a mock employee
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDesignation("Associate");
        employee.setLocation("Delhi");
        employee.setManagerId("1001");
        employee.setMobile("1234567890");
        employee.setDepartment("sales");
        employee.setCreatedTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        employee.setUpdatedTime(LocalDateTime.of(2024, 1, 2, 10, 0));

        List<EmployeeManagerModel> employees = Arrays.asList(employee);

        // Mock the behavior of employeeService
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // Perform the request and verify
        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].designation").value("Associate"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].department").value("sales"))
                .andExpect(jsonPath("$[0].mobile").value("1234567890"))
                .andExpect(jsonPath("$[0].location").value("Delhi"))
                .andExpect(jsonPath("$[0].managerId").value("1001"));
    }

    @Test
    void testAddEmployeeSuccess() throws Exception {
        // Create a mock employee
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDesignation("Associate");
        employee.setLocation("Delhi");
        employee.setManagerId("1001");
        employee.setMobile("1234567890");
        employee.setDepartment("sales");
        employee.setCreatedTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        employee.setUpdatedTime(LocalDateTime.of(2024, 1, 2, 10, 0));

        // Mock the behavior of employeeService
        when(employeeService.addEmployee(employee)).thenReturn(employee);

        // Perform the request and verify
        mockMvc.perform(post("/api/newEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Successfully created."));
    }

    @Test
    public void testAddEmployee() throws Exception {
        // Create a sample Employee object
        EmployeeManagerModel employee1 = new EmployeeManagerModel();
        employee1.setId("1001");
        employee1.setName("Alice Johnson");
        employee1.setDesignation("Account Manager");
        employee1.setEmail("alice.johnson@example.com");
        employee1.setDepartment("les");
        employee1.setMobile("9876543210");
        employee1.setLocation("Los Angeles");
        employee1.setManagerId("0");

        // Perform the request and verify
        mockMvc.perform(MockMvcRequestBuilders.post("/api/newEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testDeleteEmployeeSuccess() throws Exception {
        // Mock the behavior of employeeService
        ResponseMessage response = new ResponseMessage("Successfully deleted.");
        when(employeeService.deleteEmployee("1")).thenReturn(response);

        // Perform the request and verify
        mockMvc.perform(delete("/api/oldEmployee")
                .param("employeeId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Successfully deleted.\"}"));
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        // Mock the behavior of employeeService to throw an exception
        doThrow(new IllegalArgumentException("Employee not found.")).when(employeeService).deleteEmployee("1");

        // Perform the request and verify
        mockMvc.perform(delete("/api/oldEmployee")
                .param("employeeId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Employee not found.\"}"));
    }

    @Test
    void testChangeManagerSuccess() throws Exception {
        // Create a request and response DTO
        ManagerChangeRequest request = new ManagerChangeRequest();
        request.setEmployeeId("123");
        request.setManagerId("456");

        ManagerChangeResponseDTO responseDTO = new ManagerChangeResponseDTO("Manager changed successfully");

        // Mock the behavior of employeeService
        when(employeeService.changeManager("123", "456")).thenReturn(responseDTO);

        // Perform the request and verify
        mockMvc.perform(put("/api/newManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Manager changed successfully\"}"));
    }

    @Test
    void testChangeManagerFailure() throws Exception {
        // Create a request and response DTO
        ManagerChangeRequest request = new ManagerChangeRequest();
        request.setEmployeeId("123");
        request.setManagerId("456");

        // Mock the behavior of employeeService to throw an exception
        doThrow(new IllegalArgumentException("Invalid manager ID")).when(employeeService)
                .changeManager("123", "456");

        // Perform the request and verify
        mockMvc.perform(put("/api/newManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Invalid manager ID\"}"));
    }

    @Test
    void testGetManagerWithExperience_Success() throws Exception {
        // Create a mock Details object
        ResponseDTO.Details mockDetails = new ResponseDTO.Details();
        mockDetails.setAccountManager("John Doe");
        mockDetails.setDepartment("HR");
        mockDetails.setId("123");

        // Create a mock ResponseDTO object
        ResponseDTO mockResponse = new ResponseDTO(null);
        mockResponse.setDetails(Arrays.asList(mockDetails)); // Add the Details object to the list

        // Mock the behavior of the employeeService
        when(employeeService.managerWithExperience("123", 5)).thenReturn(mockResponse);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/managerWithYear")
                .param("managerId", "123")
                .param("year", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details[0].id").value("123"))
                .andExpect(jsonPath("$.details[0].accountManager").value("John Doe"))
                .andExpect(jsonPath("$.details[0].department").value("HR"));
    }

    @Test
    void testGetManagerWithExperience_NotFound() throws Exception {
        when(employeeService.managerWithExperience("123", 10)).thenReturn(null);

        mockMvc.perform(get("/api/managerWithYear")
                .param("managerId", "123")
                .param("year", "10"))
                .andExpect(status().isNotFound());
    }
}
