package com.sql.MySql;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.sql.MySql.controllers.EmployeeManagerController;
import com.sql.MySql.handlers.GlobalExceptionHandler;
import com.sql.MySql.helpers.ManagerChangeRequest;
import com.sql.MySql.models.EmployeeManagerModel;
import com.sql.MySql.repositories.EmployeeManagerCustomRepository;
import com.sql.MySql.repositories.EmployeeManagerRepository;
import com.sql.MySql.response.EmployeeResponseDTO;
import com.sql.MySql.response.ManagerChangeResponseDTO;
import com.sql.MySql.response.ResponseDTO;
import com.sql.MySql.response.ResponseMessage;
import com.sql.MySql.services.EmployeeManagerService;

@SpringBootTest
class MySqlApplicationTests {

	@Mock
	private EmployeeManagerCustomRepository employeeManagerRepository;

    @Mock
    private EmployeeManagerRepository employeeManagerMainRepository;

	@InjectMocks
	private EmployeeManagerService employeeService;

	@InjectMocks
	private EmployeeManagerController employeeController;

	@Mock
	private EmployeeManagerModel employee;

	public MockMvc mockMvc;

    private EmployeeResponseDTO employeeResponseDTO;

    private GlobalExceptionHandler globalExceptionHandler;

    private ManagerChangeRequest managerChangeRequest;

	@BeforeEach

	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
	}

	// validation of
	// employees======================================================================================================

    @Test
    void testValidateEmployeeData_ValidInput() {
        // Arrange
        String id = "1"; // Added ID field
        String email = "valid.email@example.com";
        String designation = "Associate";
        String mobileNumber = "1234567890";
        String department = "Engineering";
    
        when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
        when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);
    
        // Act & Assert
        assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
    }



@Test
void testValidateEmployeeData_ValidDesignation_AccountManager() {
    // Arrange
    String id = "2"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ValidDesignation_Associate() {
    // Arrange
    String id = "3"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Associate";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_InvalidDesignation() {
    // Arrange
    String id = "4"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "InvalidDesignation";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
    assertEquals("Designation can only be Account Manager or associate.", exception.getMessage());
}

@Test
void testValidateEmployeeData_Designation_CaseInsensitive() {
    // Arrange
    String id = "5"; // Added ID field
    String email = "valid.email@example.com";
    String designation1 = "account manager"; // lowercase
    String designation2 = "ASSOCIATE"; // uppercase
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert for lowercase "account manager"
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation1, mobileNumber, department)); // Added ID parameter

    // Act & Assert for uppercase "ASSOCIATE"
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation2, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ExistingEmail() {
    // Arrange
    String id = "6"; // Added ID field
    String email = "existing.email@example.com";
    String designation = "Associate";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(true);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
    assertEquals("Employee with email " + email + " already exists.", exception.getMessage());
}

@Test
void testValidateEmployeeData_ValidDepartment_Sales() {
    // Arrange
    String id = "7"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "Sales";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ValidDepartment_Delivery() {
    // Arrange
    String id = "8"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "Delivery";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ValidDepartment_QA() {
    // Arrange
    String id = "9"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "QA";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ValidDepartment_Engineering() {
    // Arrange
    String id = "10"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_ValidDepartment_BA() {
    // Arrange
    String id = "11"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "BA";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
}

@Test
void testValidateEmployeeData_InvalidDepartment() {
    // Arrange
    String id = "12"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department = "InvalidDepartment";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department)); // Added ID parameter
    assertEquals("Invalid department. Must be one of: sales, delivery, QA, engineering, BA.",
            exception.getMessage());
}

@Test
void testValidateEmployeeData_Department_CaseInsensitive() {
    // Arrange
    String id = "13"; // Added ID field
    String email = "valid.email@example.com";
    String designation = "Account Manager";
    String mobileNumber = "1234567890";
    String department1 = "sales"; // lowercase
    String department2 = "DELIVERY"; // uppercase

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false); // Added check for ID
    when(employeeManagerMainRepository.existsByEmail(email)).thenReturn(false);

    // Act & Assert for lowercase "sales"
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department1)); // Added ID parameter

    // Act & Assert for uppercase "DELIVERY"
    assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department2)); // Added ID parameter
}

@Test
void testValidateEmployeeData_NullId() {
    // Arrange
    String id = null;
    String email = "test@example.com";
    String designation = "Developer";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
    assertEquals("Employee ID cannot be null.", exception.getMessage());
}

@Test
void testValidateEmployeeData_EmployeeIdAlreadyExists() {
    // Arrange
    String id = "123";
    String email = "test@example.com";
    String designation = "Developer";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(true);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
    assertEquals("Employee with id 123 already exists.", exception.getMessage());
}

@Test
void testValidateEmployeeData_ValidData() {
    // Arrange
    String id = "123";
    String email = "test@example.com";
    String designation = "Associate";
    String mobileNumber = "1234567890";
    String department = "Engineering";

    when(employeeManagerMainRepository.existsById(id)).thenReturn(false);

    // Act & Assert
    // No exception should be thrown for valid data
    employeeService.validateEmployeeData(id, email, designation, mobileNumber, department);
}
    
	// adding an
	// employee=======================================================================================================================

	@Test
	void testAddEmployee_NewManager_Valid() {
		// Arrange
		EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
		employee.setEmail("new.manager@example.com");
		employee.setDesignation("Account Manager");
		employee.setMobile("1234567890");
		employee.setDepartment("Sales");
		employee.setManagerId("0");

		// Act
		EmployeeManagerModel result = employeeService.addEmployee(employee);

		// Assert
		assertNotNull(result);
		assertEquals("new.manager@example.com", result.getEmail());
		verify(employeeManagerMainRepository, times(1)).save(employee);
	}

    @Test
    void testAddEmployee_WithManager_Valid() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Associate");
        employee.setMobile("1234567890");
        employee.setDepartment("Sales");
        employee.setManagerId("2");
    
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setManagerId("2"); // Manager is a top-level manager
        manager.setDepartment("Sales");
    
        // Corrected the misplaced parenthesis
        when(employeeManagerRepository.findByExistingManagerId("2")).thenReturn(manager);
    
        // Act
        EmployeeManagerModel result = employeeService.addEmployee(employee);
    
        // Assert
        assertNotNull(result);
        assertEquals("employee@example.com", result.getEmail());
        verify(employeeManagerMainRepository, times(1)).save(employee);
    }
    
    @Test
    void testValidateEmployeeData_InvalidEmailFormat() {
        // Arrange
        String id = "123";
        String email = "invalid-email";
        String designation = "Associate";
        String mobileNumber = "1234567890";
        String department = "Engineering";

        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeData_InvalidMobileNumber_LengthNot10Digits() {
        // Arrange
        String id = "123";
        String email = "test@example.com";
        String designation = "Associate";
        String mobileNumber = "123456789"; // 9 digits instead of 10
        String department = "Engineering";

        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
        assertEquals("Invalid mobile number. It must be a 10-digit number.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeData_InvalidMobileNumber_NonNumericCharacters() {
        // Arrange
        String id = "123";
        String email = "test@example.com";
        String designation = "Associate";
        String mobileNumber = "12345abcde"; // Contains non-numeric characters
        String department = "Engineering";

        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
        assertEquals("Invalid mobile number. It must be a 10-digit number.", exception.getMessage());
    }



    @Test
    void testAddEmployeeDoesNotThrowExceptionWhenManagerIdIsNotZero() {
        // Arrange
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setManagerId("2"); // Valid manager
        manager.setDepartment("Engineering");
    
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("test@example.com");
        employee.setDesignation("Account Manager");
        employee.setMobile("1234567890");
        employee.setDepartment("Engineering"); // Matching department
        employee.setManagerId("2"); // Valid manager
    
        when(employeeManagerRepository.findByExistingManagerId("2")).thenReturn(manager);
    
        // Act & Assert
        assertDoesNotThrow(() -> employeeService.addEmployee(employee));
    }
    
    @Test
    void testAddEmployee_WithNonMatchingDepartment_ShouldThrowException() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Associate");
        employee.setMobile("1234567890");
        employee.setDepartment("Delivery");
        employee.setManagerId("1");
    
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setManagerId("0"); // Manager is a top-level manager
        manager.setDepartment("Sales");
    
        when(employeeManagerRepository.findByExistingManagerId("1")).thenReturn(manager);
    
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee));
        assertEquals("Employee's department does not match the manager's department.", exception.getMessage());
    }
    
    @Test
    void testValidateEmployeeData_DepartmentMatchesManager() {
        // Arrange
        String id = "12345";
        String email = "valid.email@example.com";
        String designation = "Associate";
        String mobileNumber = "1234567890";
        String department = "Engineering"; // Employee's department
        String managerDepartment = "Engineering"; // Manager's department (matches)
    
        // Create a mock manager with department "Engineering"
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setDepartment(managerDepartment);
    
        // Mock repository and service methods
        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);
        when(employeeManagerRepository.findByIdCustom("1")).thenReturn(manager);
    
        // Act & Assert
        assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
    }
    
    @Test
    void testValidateEmployeeData_DesignationNotAccountManager_DepartmentMatches() {
        // Arrange
        String id = "12345";
        String email = "valid.email@example.com";
        String designation = "Associate"; // Designation is not "Account Manager"
        String mobileNumber = "1234567890";
        String department = "Engineering"; // Employee's department
        String managerDepartment = "Engineering"; // Manager's department (matches)
    
        // Create a mock manager with department "Engineering"
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setDepartment(managerDepartment);
    
        // Mock repository and service methods
        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);
        when(employeeManagerRepository.findByIdCustom("1")).thenReturn(manager);
    
        // Act & Assert
        assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
    }
    
    @Test
    void testValidateEmployeeData_ManagerNotFound() {
        // Arrange
        String id = "12345";
        String email = "valid.email@example.com";
        String designation = "Associate"; // Designation is valid
        String mobileNumber = "1234567890";
        String department = "Engineering"; // Employee's department
    
        // Mock repository and service methods
        when(employeeManagerMainRepository.existsById(id)).thenReturn(false);
        when(employeeManagerRepository.findByIdCustom("1")).thenReturn(null); // No manager found
    
        // Act & Assert
        assertDoesNotThrow(() -> employeeService.validateEmployeeData(id, email, designation, mobileNumber, department));
    }
    
    @Test
    void testValidateEmployeeData_ValidManager_DifferentDepartment() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Associate");
        employee.setMobile("1234567890");
        employee.setDepartment("Delivery");
        employee.setManagerId("1");
    
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setManagerId("0"); // Manager is a top-level manager
        manager.setDepartment("Sales");
    
        when(employeeManagerRepository.findByExistingManagerId("1")).thenReturn(manager);
    
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee));
        assertEquals("Employee's department does not match the manager's department.", exception.getMessage());
    }
    
    @Test
    void testAddEmployee_WithManagerNotFound_ShouldThrowException() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Associate");
        employee.setMobile("1234567890");
        employee.setDepartment("Sales");
        employee.setManagerId("999"); // Manager ID does not exist
    
        when(employeeManagerRepository.findByIdCustom("999")).thenReturn(null);
    
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee));
        assertEquals("Manager with ID 999 does not exist.", exception.getMessage());
    }
    
    @Test
    void testAddEmployee_NewManager_InvalidDesignation_ShouldThrowException() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("invalid.manager@example.com");
        employee.setDesignation("Associate");
        employee.setMobile("1234567890");
        employee.setDepartment("Sales");
        employee.setManagerId("0");
    
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee));
        assertEquals("Designation must be Account Manager for a new manager.", exception.getMessage());
    }
    
    @Test
    void testAddEmployee_WithInvalidDesignation_ShouldThrowException() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Account Manager");
        employee.setMobile("1234567890");
        employee.setDepartment("Sales");
        employee.setManagerId("1");
    
        EmployeeManagerModel manager = new EmployeeManagerModel();
        manager.setManagerId("0"); // Manager is a top-level manager
        manager.setDepartment("Sales");
    
        when(employeeManagerRepository.findByExistingManagerId("1")).thenReturn(manager);
    
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.addEmployee(employee));
        assertEquals("Designation cannot be Account Manager for an employee with a valid manager ID.",
                exception.getMessage());
    }
    
    @Test
    void testAddEmployee_ThrowsExceptionWhenDepartmentAlreadyHasManager() {
        // Arrange
        String department = "Sales";
        EmployeeManagerModel existingManager = new EmployeeManagerModel();
        existingManager.setDepartment(department);
        existingManager.setManagerId("1");
    
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("1");
        employee.setEmail("employee@example.com");
        employee.setDesignation("Account Manager");
        employee.setMobile("1234567890");
        employee.setDepartment("Sales");
        employee.setManagerId("0");
    
        // Mock the repository behavior
        when(employeeManagerRepository.findManagerByDepartment(department)).thenReturn(existingManager);
    
        // Act and Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(employee);
        });
    
        // Verify the exception message
        assertEquals("Department " + department + " already has a manager.", thrown.getMessage());
    }
    
    @Test
    void testGetAllEmployees() {
        // Arrange
        EmployeeManagerModel employee1 = new EmployeeManagerModel();
        employee1.setEmail("employee1@example.com");
        employee1.setDesignation("Developer");
        employee1.setMobile("1234567890");
        employee1.setDepartment("Engineering");
        employee1.setManagerId("1");
    
        EmployeeManagerModel employee2 = new EmployeeManagerModel();
        employee2.setEmail("employee2@example.com");
        employee2.setDesignation("Analyst");
        employee2.setMobile("0987654321");
        employee2.setDepartment("QA");
        employee2.setManagerId("2");
    
        List<EmployeeManagerModel> employees = Arrays.asList(employee1, employee2);
    
        when(employeeManagerRepository.getAllEmployees()).thenReturn(employees);
    
        // Act
        List<EmployeeManagerModel> result = employeeService.getAllEmployees();
    
        // Assert
        assertEquals(2, result.size());
        assertEquals("employee1@example.com", result.get(0).getEmail());
        assertEquals("employee2@example.com", result.get(1).getEmail());
    }
 
	// delete an employee
	// ==========================================================================================================

	@Test
void testDeleteEmployee_EmployeeNotFound() {
    // Arrange
    String id = "1";
    when(employeeManagerMainRepository.findById(id)).thenReturn(null);

    // Act
    ResponseMessage response = employeeService.deleteEmployee(id);

    // Assert
    assertEquals("Cannot find employee with ID " + id + " in the organization.", response.getMessage());
}


@Test
void testDeleteEmployee_EmployeeIsManagerWithSubordinates() {
    // Arrange
    String id = "1";
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(id);
    manager.setManagerId("0"); // Top-level manager

    List<EmployeeManagerModel> subordinates = Collections.singletonList(new EmployeeManagerModel());
    when(employeeManagerRepository.findByIdCustom(id)).thenReturn(manager);
    when(employeeManagerRepository.findByManagerId(id)).thenReturn(subordinates);

    // Act
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.deleteEmployee(id);
    });

    assertEquals("Cannot delete employee as they have employees to manage.", exception.getMessage());
}

@Test
void testDeleteEmployee_EmployeeIsManagerWithoutSubordinates() {
    // Arrange
    String id = "1";
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(id);
    manager.setManagerId("0"); // Top-level manager

    when(employeeManagerRepository.findByIdCustom(id)).thenReturn(manager);
    when(employeeManagerRepository.findByManagerId(id)).thenReturn(Collections.emptyList());

    // Act
    ResponseMessage response = employeeService.deleteEmployee(id);

    // Assert
    assertEquals("Successfully deleted " + manager.getName() + " from the organization.", response.getMessage());
    verify(employeeManagerMainRepository, times(1)).deleteById(id);
}

@Test
void testDeleteEmployee_EmployeeIsNotAManager() {
    // Arrange
    String id = "1";
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId(id);
    employee.setManagerId("1"); // Not a manager

    when(employeeManagerRepository.findByIdCustom(id)).thenReturn(employee);

    // Act
    ResponseMessage response = employeeService.deleteEmployee(id);

    // Assert
    assertEquals("Successfully deleted " + employee.getName() + " from the organization.", response.getMessage());
    verify(employeeManagerMainRepository, times(1)).deleteById(id);
}


	// chnage an employee's manager

	@Test
void testChangeManager_EmployeeNotFound() {
    // Arrange
    String empId = "1";
    String newManagerId = "2";
    when(employeeManagerMainRepository.findById(empId)).thenReturn(null);

    // Act
    ManagerChangeResponseDTO response = employeeService.changeManager(empId, newManagerId);

    // Assert
    assertEquals("Cannot find employee with id " + empId + " in the organisation", response.getMessage());
}

@Test
void testChangeManager_OldManagerExists_NewManagerFound() {
    // Arrange
    String empId = "1";
    String oldManagerId = "2";
    String newManagerId = "3";

    // Mock old manager
    EmployeeManagerModel oldManager = new EmployeeManagerModel();
    oldManager.setName("Old Manager");
    oldManager.setManagerId("0");

    // Mock new manager
    EmployeeManagerModel newManager = new EmployeeManagerModel();
    newManager.setName("New Manager");
    newManager.setManagerId("0");

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("Employee");
    employee.setManagerId(oldManagerId);

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(oldManagerId)).thenReturn(Optional.of(oldManager));
    when(employeeManagerMainRepository.findById(newManagerId)).thenReturn(Optional.of(newManager));

    // Act
    ManagerChangeResponseDTO response = employeeService.changeManager(empId, newManagerId);

    // Assert
    assertEquals("Employee's manager has been succesfully changed from Old Manager to New Manager", response.getMessage());
}

@Test
void testChangeManager_NewManagerIsNotValid() {
    // Arrange
    String empId = "1";
    String oldManagerId = "2";
    String newManagerId = "3";

    // Mock old manager
    EmployeeManagerModel oldManager = new EmployeeManagerModel();
    oldManager.setName("Old Manager");
    oldManager.setManagerId("0");

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("Employee");
    employee.setManagerId(oldManagerId);

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(oldManagerId)).thenReturn(Optional.of(oldManager));
    when(employeeManagerMainRepository.findById(newManagerId)).thenReturn(Optional.empty());

    // Act
    ManagerChangeResponseDTO response = employeeService.changeManager(empId, newManagerId);

    // Assert
    assertEquals("Cannot find new Manager with id " + newManagerId + " in the organisation", response.getMessage());
}

@Test
void testChangeManager_NewManagerIsInvalid() {
    // Arrange
    String empId = "1";
    String oldManagerId = "2";
    String newManagerId = "3";

    // Mock old manager
    EmployeeManagerModel oldManager = new EmployeeManagerModel();
    oldManager.setName("Old Manager");
    oldManager.setManagerId("0");

    // Mock new manager
    EmployeeManagerModel newManager = new EmployeeManagerModel();
    newManager.setName("New Manager");
    newManager.setManagerId("1"); // Invalid managerId

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("Employee");
    employee.setManagerId(oldManagerId);

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(oldManagerId)).thenReturn(Optional.of(oldManager));
    when(employeeManagerMainRepository.findById(newManagerId)).thenReturn(Optional.of(newManager));

    // Act
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.changeManager(empId, newManagerId);
    });

    // Assert
    assertEquals("New manager must be a valid manager of the department.", thrown.getMessage());
}

@Test
void testChangeManager_EmployeeIsAManager() {
    // Arrange
    String empId = "1";
    String newManagerId = "3";

    // Mock new manager
    EmployeeManagerModel newManager = new EmployeeManagerModel();
    newManager.setName("New Manager");
    newManager.setManagerId("0");

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("Employee");
    employee.setManagerId("0"); // Employee is already a manager

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(newManagerId)).thenReturn(Optional.of(newManager));

    // Act
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.changeManager(empId, newManagerId);
    });

    // Assert
    assertEquals("Employee is already a manager. Cannot assign manager to another manager", thrown.getMessage());
}

@Test
void testChangeManager_SuccessfulManagerChange() {
    // Arrange
    String empId = "1";
    String oldManagerId = "2";
    String newManagerId = "3";

    // Mock old manager
    EmployeeManagerModel oldManager = new EmployeeManagerModel();
    oldManager.setName("Old Manager");
    oldManager.setManagerId("0");

    // Mock new manager
    EmployeeManagerModel newManager = new EmployeeManagerModel();
    newManager.setName("New Manager");
    newManager.setManagerId("0");

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("John Doe");
    employee.setManagerId(oldManagerId);

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(oldManagerId)).thenReturn(Optional.of(oldManager));
    when(employeeManagerMainRepository.findById(newManagerId)).thenReturn(Optional.of(newManager));

    // Act
    ManagerChangeResponseDTO response = employeeService.changeManager(empId, newManagerId);

    // Assert
    assertEquals("John Doe's manager has been succesfully changed from Old Manager to New Manager", response.getMessage());
    verify(employeeManagerMainRepository, times(1)).save(employee);
}

@Test
void testChangeManager_SameManagerChange() {
    // Arrange
    String empId = "1";
    String oldManagerId = "2";

    // Mock old manager
    EmployeeManagerModel oldManager = new EmployeeManagerModel();
    oldManager.setName("Old Manager");
    oldManager.setManagerId("0");

    // Mock employee
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setName("John Doe");
    employee.setManagerId(oldManagerId);

    when(employeeManagerMainRepository.findById(empId)).thenReturn(Optional.of(employee));
    when(employeeManagerMainRepository.findById(oldManagerId)).thenReturn(Optional.of(oldManager));

    // Act
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.changeManager(empId, oldManagerId);
    });

    // Assert
    assertEquals("Employee is currently working under the manager with id " + oldManagerId, thrown.getMessage());
}

//manager with experience=============================================================================================================================================

@Test
void testManagerWithExperience_ManagerAndExperienceProvided() {
    // Arrange
    String managerId = "1";
    Integer minYearsOfExperience = 5;

    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(managerId);
    manager.setName("Manager Name");
    manager.setDepartment("Department");

    List<EmployeeManagerModel> employees = new ArrayList<>();
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setName("Employee Name");
    employee.setDepartment("Department");
    employees.add(employee);

    when(employeeManagerRepository.findByExistingManagerId(managerId)).thenReturn(manager);
    when(employeeManagerRepository.getEmployeesByManagerIdAndJoiningDate(managerId, LocalDateTime.now().minusYears(minYearsOfExperience)))
            .thenReturn(employees);

    // Act
    ResponseDTO responseDTO = employeeService.managerWithExperience(managerId, minYearsOfExperience);

    // Assert
    assertEquals("Successfully fetched", responseDTO.getMessage());
    assertNotNull(responseDTO.getDetails());
    assertEquals(1, responseDTO.getDetails().size());
    ResponseDTO.Details details = responseDTO.getDetails().get(0);
    assertEquals(managerId, details.getId());
    assertEquals("Manager Name", details.getAccountManager());
    assertEquals("Department", details.getDepartment());
   
}

@Test
void testManagerWithExperience_ManagerIdOnly() {
    // Arrange
    String managerId = "1";

    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(managerId);
    manager.setName("Manager Name");
    manager.setDepartment("Department");
    manager.setManagerId("0");

    List<EmployeeManagerModel> employees = new ArrayList<>();
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setName("Employee Name");
    employee.setDepartment("Department");
    employees.add(employee);

    when(employeeManagerRepository.findByExistingManagerId(managerId)).thenReturn(manager);
    when(employeeManagerRepository.findByManagerId(managerId)).thenReturn(employees);

    // Act
    ResponseDTO responseDTO = employeeService.managerWithExperience(managerId, null);

    // Assert
    assertEquals("Successfully fetched", responseDTO.getMessage());
    assertNotNull(responseDTO.getDetails());
    assertEquals(1, responseDTO.getDetails().size());
    ResponseDTO.Details details = responseDTO.getDetails().get(0);
    assertEquals(managerId, details.getId());
    assertEquals("Manager Name", details.getAccountManager());
    assertEquals("Department", details.getDepartment());
    assertEquals(1, details.getEmployeeList().size());
    EmployeeResponseDTO employeeDTO = details.getEmployeeList().get(0);
    assertEquals("Employee Name", employeeDTO.getName());
}

@Test
void testManagerWithExperience_InvalidManagerId_NegativeNumber() {
    // Arrange
    String managerId = "-1";
    Integer minYearsOfExperience = null;

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            employeeService.managerWithExperience(managerId, minYearsOfExperience));
    assertEquals("Invalid Manager ID", exception.getMessage());
}

@Test
void testManagerWithExperience_InvalidManagerId_NotFoundInRepository() {
    // Arrange
    String managerId = "999"; // Assume this ID does not exist in the repository
    Integer minYearsOfExperience = null;

    when(employeeManagerRepository.findByExistingManagerId(managerId)).thenReturn(null);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            employeeService.managerWithExperience(managerId, minYearsOfExperience));
    assertEquals("Invalid Manager ID", exception.getMessage());
}

@Test
void testManagerWithExperience_NegativeMinYearsOfExperience() {
    // Arrange
    String managerId = null; // Manager ID is not the focus in this test
    Integer minYearsOfExperience = -1;

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            employeeService.managerWithExperience(managerId, minYearsOfExperience));
    assertEquals("Minimum Years of Experience must be non-negative.", exception.getMessage());
}


@Test
void testManagerWithExperience_ExperienceOnly() {
    // Arrange
	String managerId = "1";
    Integer minYearsOfExperience = 5;

	List<EmployeeManagerModel> managers = new ArrayList<>();
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(managerId);
    manager.setName("Manager Name");
    manager.setDepartment("Department");
	manager.setManagerId("0");
	managers.add(manager);

    List<EmployeeManagerModel> employees = new ArrayList<>();
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setName("Employee Name");
    employee.setDepartment("Department");
    employees.add(employee);

    when(employeeManagerRepository.findByManagerId("0")).thenReturn(managers);
    when(employeeManagerRepository.getEmployeesByManagerIdAndJoiningDate(managerId, LocalDateTime.now().minusYears(minYearsOfExperience)))
            .thenReturn(employees);

    // Act
    ResponseDTO responseDTO = employeeService.managerWithExperience(null, minYearsOfExperience);

    // Assert
    assertEquals("Successfully fetched", responseDTO.getMessage());
    assertNotNull(responseDTO.getDetails());
    assertEquals(1, responseDTO.getDetails().size());
    ResponseDTO.Details details = responseDTO.getDetails().get(0);
    assertEquals(managerId, details.getId());
    assertEquals("Manager Name", details.getAccountManager());
    assertEquals("Department", details.getDepartment());
}

@Test
void testManagerWithExperience_NeitherProvided() {
    // Arrange
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId("1");
    manager.setName("Manager Name");
    manager.setDepartment("Department");

    List<EmployeeManagerModel> managers = new ArrayList<>();
    managers.add(manager);

    List<EmployeeManagerModel> employees = new ArrayList<>();
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setName("Employee Name");
    employee.setDepartment("Department");
    employees.add(employee);

    when(employeeManagerRepository.findByManagerId("0")).thenReturn(managers);
    when(employeeManagerRepository.findByManagerId(manager.getId())).thenReturn(employees);

    // Act
    ResponseDTO responseDTO = employeeService.managerWithExperience(null, null);

    // Assert
    assertEquals("Successfully fetched", responseDTO.getMessage());
    assertNotNull(responseDTO.getDetails());
    assertEquals(1, responseDTO.getDetails().size());
    ResponseDTO.Details details = responseDTO.getDetails().get(0);
    assertEquals(manager.getId(), details.getId());
    assertEquals("Manager Name", details.getAccountManager());
    assertEquals("Department", details.getDepartment());
    assertEquals(1, details.getEmployeeList().size());
    EmployeeResponseDTO employeeDTO = details.getEmployeeList().get(0);
    assertEquals("Employee Name", employeeDTO.getName());
}

@Test
void testManagerWithExperience_BothValid() {
    // Arrange
    String managerId = "1";
    Integer minYearsOfExperience = 5;

    // Act & Assert
    assertDoesNotThrow(() -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
}
@Test
void testManagerWithExperience_ManagerIdNull_MinYearsOfExperienceValid() {
    // Arrange
    String managerId = null;
    Integer minYearsOfExperience = 5;

    // Act & Assert
    assertDoesNotThrow(() -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
}
@Test
void testManagerWithExperience_ManagerIdValid_MinYearsOfExperienceNull() {
    // Arrange
    String managerId = "1";
    Integer minYearsOfExperience = null;

    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(managerId);
    manager.setName("Manager Name");
    manager.setDepartment("Department");

    List<EmployeeManagerModel> employees = new ArrayList<>();
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setName("Employee Name");
    employee.setDepartment("Department");
    employees.add(employee);

    when(employeeManagerRepository.findByExistingManagerId(managerId)).thenReturn(manager);
    when(employeeManagerRepository.findByManagerId(managerId)).thenReturn(employees);

    // Act & Assert
    assertDoesNotThrow(() -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
}
@Test
void testManagerWithExperience_BothNull() {
    // Arrange
    String managerId = null;
    Integer minYearsOfExperience = null;

    // Act & Assert
    assertDoesNotThrow(() -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
}


@Test
void testManagerWithExperience_ManagerIdNegative_MinYearsOfExperienceValid() {
    // Arrange
    String managerId = "-1";
    Integer minYearsOfExperience = 5;

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
    assertEquals("Manager ID and Minimum Years of Experience must be non-negative.", exception.getMessage());
}

@Test
void testManagerWithExperience_ManagerIdValid_MinYearsOfExperienceNegative() {
    // Arrange
    String managerId = "1";
    Integer minYearsOfExperience = -5;

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        employeeService.managerWithExperience(managerId, minYearsOfExperience);
    });
    assertEquals("Manager ID and Minimum Years of Experience must be non-negative.", exception.getMessage());
}





	

//toString ====================================================================================================================

@Test
void testToStringMethod() {
    // Arrange
    String id = "1";
    String name = "John Doe";
    String designation = "Manager";
    String email = "john.doe@example.com";
    String department = "Engineering";
    String mobile = "1234567890";
    String location = "New York";
    String managerId = "0";
    LocalDateTime dateOfJoining = LocalDateTime.of(2020, 1, 1, 0, 0);
    LocalDateTime createdTime = LocalDateTime.of(2020, 1, 2, 10, 0);
    LocalDateTime updatedTime = LocalDateTime.of(2020, 1, 3, 12, 0);

    EmployeeManagerModel employeeManagerModel = new EmployeeManagerModel();
    employeeManagerModel.setId(id);
    employeeManagerModel.setName(name);
    employeeManagerModel.setDesignation(designation);
    employeeManagerModel.setEmail(email);
    employeeManagerModel.setDepartment(department);
    employeeManagerModel.setMobile(mobile);
    employeeManagerModel.setLocation(location);
    employeeManagerModel.setManagerId(managerId);
    employeeManagerModel.setDateOfJoining(dateOfJoining);
    employeeManagerModel.setCreatedTime(createdTime);
    employeeManagerModel.setUpdatedTime(updatedTime);

    String expectedString = "EmployeeManagerModel [id=" + id + ", name=" + name + ", designation=" + designation 
        + ", email=" + email + ", department=" + department + ", mobile=" + mobile + ", location=" + location 
        + ", managerId=" + managerId + ", dateOfJoining=" + dateOfJoining + ", createdTime=" + createdTime 
        + ", updatedTime=" + updatedTime + "]";

    // Act
    String actualString = employeeManagerModel.toString();

    // Assert
    assertEquals(expectedString, actualString);
}





// @Test
//     void testGetEmployeesByManagerIdAndExperience() {
//         // Arrange
//         String managerId = "manager123";
//         int yearsOfExperience = 5;

//         EmployeeManagerModel employee1 = new EmployeeManagerModel();
//         EmployeeManagerModel employee2 = new EmployeeManagerModel();
//         List<EmployeeManagerModel> expectedEmployees = Arrays.asList(employee1, employee2);

//         when(employeeManagerRepository.getEmployeesByManagerIdAndJoiningDate(managerId, yearsOfExperience))
//             .thenReturn(expectedEmployees);

//         // Act
//         List<EmployeeManagerModel> actualEmployees = employeeManagerRepository.getEmployeesByManagerIdAndExperience(managerId, yearsOfExperience);

//         // Assert
//         assertEquals(expectedEmployees, actualEmployees);
//         verify(employeeManagerRepository, times(1)).getEmployeesByManagerIdAndExperience(managerId, yearsOfExperience);
//     }

    @Test
    void testGetId() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String id = "123";
        employeeResponseDTO.setId(id);
        assertEquals(id, employeeResponseDTO.getId(), "The ID should match the value that was set.");
    }

    @Test
    void testGetName() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String name = "John Doe";
        employeeResponseDTO.setName(name);
        assertEquals(name, employeeResponseDTO.getName(), "The name should match the value that was set.");
    }

    @Test
    void testGetDesignation() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String designation = "Software Engineer";
        employeeResponseDTO.setDesignation(designation);
        assertEquals(designation, employeeResponseDTO.getDesignation(), "The designation should match the value that was set.");
    }

    @Test
    void testGetEmail() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String email = "john.doe@example.com";
        employeeResponseDTO.setEmail(email);
        assertEquals(email, employeeResponseDTO.getEmail(), "The email should match the value that was set.");
    }

    @Test
    void testGetDepartment() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String department = "Engineering";
        employeeResponseDTO.setDepartment(department);
        assertEquals(department, employeeResponseDTO.getDepartment(), "The department should match the value that was set.");
    }

    @Test
    void testGetMobile() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String mobile = "1234567890";
        employeeResponseDTO.setMobile(mobile);
        assertEquals(mobile, employeeResponseDTO.getMobile(), "The mobile should match the value that was set.");
    }

    @Test
    void testGetLocation() {
        employeeResponseDTO = new EmployeeResponseDTO();
        String location = "New York";
        employeeResponseDTO.setLocation(location);
        assertEquals(location, employeeResponseDTO.getLocation(), "The location should match the value that was set.");
    }

    @Test
    void testGetDateOfJoining() {
        employeeResponseDTO = new EmployeeResponseDTO();
        LocalDateTime dateOfJoining = LocalDateTime.of(2023, 1, 1, 12, 0);
        employeeResponseDTO.setDateOfJoining(dateOfJoining);
        assertEquals(dateOfJoining, employeeResponseDTO.getDateOfJoining(), "The date of joining should match the value that was set.");
    }

    @Test
    void testGetCreatedTime() {
        employeeResponseDTO = new EmployeeResponseDTO();
        LocalDateTime createdTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        employeeResponseDTO.setCreatedTime(createdTime);
        assertEquals(createdTime, employeeResponseDTO.getCreatedTime(), "The created time should match the value that was set.");
    }

    @Test
    void testGetUpdatedTime() {
        employeeResponseDTO = new EmployeeResponseDTO();
        LocalDateTime updatedTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        employeeResponseDTO.setUpdatedTime(updatedTime);
        assertEquals(updatedTime, employeeResponseDTO.getUpdatedTime(), "The updated time should match the value that was set.");
    }


   


@Test
void testHandleGenericException() {
    globalExceptionHandler = new GlobalExceptionHandler();
    // Arrange
    Exception exception = new Exception("Generic error occurred");

    // Act
    ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleGenericException(exception);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "The status code should be 500 INTERNAL_SERVER_ERROR.");
    assertEquals("An unexpected error occurred: Generic error occurred", responseEntity.getBody().getMessage(), "The error message should match the exception message.");
}

@Test
void testHandleValidationExceptions() {
    
    globalExceptionHandler = new GlobalExceptionHandler();
    // Arrange
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);

    // Act
    ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleValidationExceptions(exception);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "The status code should be 400 BAD_REQUEST.");
    assertEquals("Validation failed: null", responseEntity.getBody().getMessage(), "The error message should indicate validation failure.");
}


    

    @Test
    void testHandleIllegalArgumentException() {
        globalExceptionHandler = new GlobalExceptionHandler();
        // Arrange
        String errorMessage = "Invalid argument";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody().getMessage());
    }


















    

    @Test
    void testSetAndGetEmployeeId() {
        managerChangeRequest = new ManagerChangeRequest();
        // Arrange
        String employeeId = "E12345";

        // Act
        managerChangeRequest.setEmployeeId(employeeId);
        String retrievedEmployeeId = managerChangeRequest.getEmployeeId();

        // Assert
        assertEquals(employeeId, retrievedEmployeeId);
    }

    @Test
    void testSetAndGetManagerId() {
        managerChangeRequest = new ManagerChangeRequest();
        // Arrange
        String managerId = "M67890";

        // Act
        managerChangeRequest.setManagerId(managerId);
        String retrievedManagerId = managerChangeRequest.getManagerId();

        // Assert
        assertEquals(managerId, retrievedManagerId);
    }

    @Test
    void testResponseMessageConstructor() {
        // Arrange
        String expectedMessage = "Test message";

        // Act
        ResponseMessage responseMessage = new ResponseMessage(expectedMessage);

        // Assert
        assertEquals(expectedMessage, responseMessage.getMessage(), "The message should be correctly set by the constructor.");
    }

    @Test
    void testManagerChangeResponseDTOConstructor() {
        // Arrange
        String expectedMessage = "Manager change successful";

        // Act
        ManagerChangeResponseDTO responseDTO = new ManagerChangeResponseDTO(expectedMessage);

        // Assert
        assertEquals(expectedMessage, responseDTO.getMessage(), "The message should be correctly set by the constructor.");
    }
}
