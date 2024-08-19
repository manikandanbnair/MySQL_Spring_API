package com.sql.MySql.repositories;

import com.sql.MySql.models.EmployeeManagerModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeManagerCustomRepository {

    List<EmployeeManagerModel> getAllEmployees();
    
    EmployeeManagerModel findByIdCustom(String id);

    EmployeeManagerModel findByExistingManagerId(String managerId);
    
    EmployeeManagerModel findManagerByDepartment(String department);
    
    List<EmployeeManagerModel> getEmployeesByManagerIdAndJoiningDate(String managerId, LocalDateTime dateOfJoining);
    
    List<EmployeeManagerModel> findByManagerId(String managerId);
    
    List<EmployeeManagerModel> findByDepartment(String department);

    List<EmployeeManagerModel> findByDateOfJoiningBefore(LocalDate minJoiningDate);
    
    //List<EmployeeManagerModel> findByManagerIdAndDateOfJoiningBeforeOrEqual(String managerId, LocalDate minJoiningDate);

}
