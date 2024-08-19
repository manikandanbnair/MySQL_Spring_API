package com.sql.MySql;

import com.sql.MySql.models.EmployeeManagerModel;
import com.sql.MySql.repositories.EmpImpl;
import com.sql.MySql.repositories.EmployeeManagerCustomRepository;
import com.sql.MySql.repositories.EmployeeManagerRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepoTest {

    @Mock
    private EmployeeManagerRepository employeeManagerRepository;

    @Mock
    private EmployeeManagerCustomRepository employeeManagerCustomRepository;

    @InjectMocks
    private EmpImpl empImpl;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empImpl = new EmpImpl(entityManager);  
    }

    @Test
    void testGetAllEmployees() {
        // Arrange
        EmployeeManagerModel employee = new EmployeeManagerModel();
        employee.setId("10");
    
        List<EmployeeManagerModel> employees = Arrays.asList(employee);
    
        TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
        when(mockTypedQuery.getResultList()).thenReturn(employees);
    
        when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
            .thenReturn(mockTypedQuery);
    
        // Act
        List<EmployeeManagerModel> result = empImpl.getAllEmployees();
    
        // Assert
        assertEquals(1, result.size());
        assertEquals("10", result.get(0).getId());
        verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
        verify(mockTypedQuery).getResultList();
    }
    
    @Test
void testGetEmployeeById() {
    // Arrange
    String id = "10";
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId(id);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getSingleResult()).thenReturn(employee);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    EmployeeManagerModel result = empImpl.findByIdCustom(id);

    // Assert
    assertEquals(id, result.getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getSingleResult();
}

@Test
void testFindByExistingManagerId() {
    // Arrange
    String managerId = "10";
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setId(managerId);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getSingleResult()).thenReturn(manager);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    EmployeeManagerModel result = empImpl.findByExistingManagerId(managerId);

    // Assert
    assertEquals(managerId, result.getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getSingleResult();
}

@Test
void testFindManagerByDepartment() {
    // Arrange
    String department = "Engineering";
    EmployeeManagerModel manager = new EmployeeManagerModel();
    manager.setDepartment(department);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getSingleResult()).thenReturn(manager);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    EmployeeManagerModel result = empImpl.findManagerByDepartment(department);

    // Assert
    assertEquals(department, result.getDepartment());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getSingleResult();
}


@Test
void testGetEmployeesByManagerIdAndJoiningDate() {
    // Arrange
    String managerId = "10";
    LocalDateTime minJoiningDate = LocalDateTime.now().minusYears(2);

    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("20");

    List<EmployeeManagerModel> employees = Arrays.asList(employee);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getResultList()).thenReturn(employees);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    List<EmployeeManagerModel> result = empImpl .getEmployeesByManagerIdAndJoiningDate(managerId, minJoiningDate);

    // Assert
    assertEquals(1, result.size());
    assertEquals("20", result.get(0).getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getResultList();
}

@Test
void testFindByManagerId() {
    // Arrange
    String managerId = "10";
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("20");

    List<EmployeeManagerModel> employees = Arrays.asList(employee);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getResultList()).thenReturn(employees);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    List<EmployeeManagerModel> result = empImpl.findByManagerId(managerId);

    // Assert
    assertEquals(1, result.size());
    assertEquals("20", result.get(0).getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getResultList();
}

@Test
void testGetEmployeesByDepartment() {
    // Arrange
    String department = "Engineering";
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");
    employee.setDepartment(department);

    List<EmployeeManagerModel> employees = Arrays.asList(employee);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getResultList()).thenReturn(employees);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    List<EmployeeManagerModel> result = empImpl.findByDepartment(department);

    // Assert
    assertEquals(1, result.size());
    assertEquals("10", result.get(0).getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getResultList();
}

@Test
void testFindByDateOfJoiningBefore() {
    // Arrange
    LocalDate minJoiningDate = LocalDate.now().minusYears(2);
    EmployeeManagerModel employee = new EmployeeManagerModel();
    employee.setId("10");

    List<EmployeeManagerModel> employees = Arrays.asList(employee);

    TypedQuery<EmployeeManagerModel> mockTypedQuery = mock(TypedQuery.class);
    when(mockTypedQuery.getResultList()).thenReturn(employees);

    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenReturn(mockTypedQuery);

    // Act
    List<EmployeeManagerModel> result = empImpl.findByDateOfJoiningBefore(minJoiningDate);

    // Assert
    assertEquals(1, result.size());
    assertEquals("10", result.get(0).getId());
    verify(entityManager).createQuery(anyString(), eq(EmployeeManagerModel.class));
    verify(mockTypedQuery).getResultList();
}

@Test
void testFindByIdCustom_NoResultException() {
    // Arrange
    String id = "nonexistent-id";
    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenThrow(new NoResultException("No entity found"));

    // Act
    EmployeeManagerModel result = empImpl.findByIdCustom(id);

    // Assert
    assertNull(result, "The result should be null when no entity is found.");
}

@Test
void testFindByExistingManagerId_NoResultException() {
    // Arrange
    String managerId = "nonexistent-manager-id";
    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenThrow(new NoResultException("No entity found"));

    // Act
    EmployeeManagerModel result = empImpl.findByExistingManagerId(managerId);

    // Assert
    assertNull(result, "The result should be null when no entity is found.");
}

@Test
void testFindManagerByDepartment_NoResultException() {
    // Arrange
    String department = "Nonexistent Department";
    when(entityManager.createQuery(anyString(), eq(EmployeeManagerModel.class)))
        .thenThrow(new NoResultException("No entity found"));

    // Act
    EmployeeManagerModel result = empImpl.findManagerByDepartment(department);

    // Assert
    assertNull(result, "The result should be null when no manager is found.");
}


}
