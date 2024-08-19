package com.sql.MySql.controllers;

import com.sql.MySql.helpers.ManagerChangeRequest;
import com.sql.MySql.models.EmployeeManagerModel;
import com.sql.MySql.response.ManagerChangeResponseDTO;
import com.sql.MySql.response.ResponseDTO;
import com.sql.MySql.response.ResponseMessage;
import com.sql.MySql.services.EmployeeManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeManagerController {

    @Autowired
    private EmployeeManagerService employeeService;

    //Get all employees
    @GetMapping("/employees")
    public List<EmployeeManagerModel> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Get manager with experience
    @GetMapping("/managerWithYear")
    public ResponseEntity<ResponseDTO> getManagerWithExperience(
            @RequestParam(value = "managerId", required = false) String managerId,
            @RequestParam(value = "year", required = false) Integer minYearsOfExperience) {

        ResponseDTO responseDTO = employeeService.managerWithExperience(managerId, minYearsOfExperience);

        if (responseDTO != null) {
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add new employee
    @PostMapping("/newEmployee")
    public ResponseEntity<ResponseMessage> addEmployee(@RequestBody EmployeeManagerModel employee) {
        employeeService.addEmployee(employee);
        return new ResponseEntity<>(new ResponseMessage("Successfully created."), HttpStatus.CREATED);
    }

    // Change manager
    @PutMapping("/newManager")
    public ResponseEntity<ManagerChangeResponseDTO> changeManager(@RequestBody ManagerChangeRequest request) {
        String employeeId = request.getEmployeeId();
        String newManagerId = request.getManagerId();

        ManagerChangeResponseDTO responseDTO = employeeService.changeManager(employeeId, newManagerId);
        return ResponseEntity.ok(responseDTO);
    }

    // Delete employee
    @DeleteMapping("/oldEmployee")
    public ResponseEntity<ResponseMessage> deleteEmployee(@RequestParam String employeeId) {
        ResponseMessage response = employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(response);
    }
}
