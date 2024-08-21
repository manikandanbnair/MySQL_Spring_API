package com.sql.MySql.services;

import com.sql.MySql.models.EmployeeManagerModel;
import com.sql.MySql.repositories.EmployeeManagerCustomRepository;
import com.sql.MySql.repositories.EmployeeManagerRepository;
import com.sql.MySql.response.EmployeeResponseDTO;
import com.sql.MySql.response.ManagerChangeResponseDTO;
import com.sql.MySql.response.ResponseDTO;
import com.sql.MySql.response.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmployeeManagerService {

    @Autowired
    private EmployeeManagerCustomRepository employeeManagerRepository;

    @Autowired
    private EmployeeManagerRepository employeeManagerMainRepository;

    public List<EmployeeManagerModel> getAllEmployees() {
        return employeeManagerRepository.getAllEmployees();
    }

    // POST
    @Transactional
    public EmployeeManagerModel addEmployee(EmployeeManagerModel employee) {
        String id = employee.getId();
        String email = employee.getEmail();
        String designation = employee.getDesignation();
        String mobileNumber = employee.getMobile();
        String department = employee.getDepartment();
        String managerId = employee.getManagerId(); // Changed to String

        // Validate employee data
        validateEmployeeData(id, email, designation, mobileNumber, department);

        // Set creation and update times
        LocalDateTime now = LocalDateTime.now();
        employee.setCreatedTime(now);
        employee.setUpdatedTime(now);

      
        // Handle employee as manager or regular employee
        if ("0".equals(managerId)) { // Changed to String comparison
            if ("Account Manager".equalsIgnoreCase(designation)) {
                handleEmployeeAsManager(employee, department);
            } else {
                throw new IllegalArgumentException("Designation must be Account Manager for a new manager.");
            }
        } else {
            EmployeeManagerModel managerOpt = employeeManagerRepository.findByExistingManagerId(managerId);
           
            if (managerOpt != null) {
                EmployeeManagerModel manager = managerOpt;
                if ("0".equals(manager.getManagerId())) {
                    String managerDepartment = manager.getDepartment();

                    if ("Account Manager".equalsIgnoreCase(designation)) {
                        throw new IllegalArgumentException(
                                "Designation cannot be Account Manager for an employee with a valid manager ID.");
                    } else if (!department.equalsIgnoreCase(managerDepartment)) {
                        throw new IllegalArgumentException(
                                "Employee's department does not match the manager's department.");
                    }
                  }
            } else {
                throw new IllegalArgumentException("Manager with ID " + managerId + " does not exist.");
            }
        }

        employeeManagerMainRepository.save(employee);
        return employee;
    }

    public void validateEmployeeData(String id, String email, String designation, String mobileNumber,
            String department) throws IllegalArgumentException {

        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null.");
        }

        if (employeeManagerMainRepository.existsById(id)) {
            throw new IllegalArgumentException("Employee with id " + id + " already exists.");
        }
        if (!"Account Manager".equalsIgnoreCase(designation) && !"associate".equalsIgnoreCase(designation)) {
            throw new IllegalArgumentException("Designation can only be Account Manager or associate.");
        }

        if (employeeManagerMainRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Employee with email " + email + " already exists.");
        }

        if (!"sales".equalsIgnoreCase(department) &&
                !"delivery".equalsIgnoreCase(department) &&
                !"QA".equalsIgnoreCase(department) &&
                !"engineering".equalsIgnoreCase(department) &&
                !"BA".equalsIgnoreCase(department)) {
            throw new IllegalArgumentException(
                    "Invalid department. Must be one of: sales, delivery, QA, engineering, BA.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (mobileNumber.length() != 10 || !mobileNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid mobile number. It must be a 10-digit number.");
        }
    }

    private void handleEmployeeAsManager(EmployeeManagerModel employee, String department) {
        EmployeeManagerModel existingManager = employeeManagerRepository.findManagerByDepartment(department);

        if (existingManager != null) {
            throw new IllegalArgumentException("Department " + department + " already has a manager.");
        } else {

        }
        
    }

    // GET
    public ResponseDTO managerWithExperience(String managerId, Integer minYearsOfExperience) {

        List<ResponseDTO.Details> detailsList = new ArrayList<>();
        boolean check = true;

        if (managerId != null && minYearsOfExperience != null) {
            if (Integer.parseInt(managerId) < 0 || minYearsOfExperience < 0) {
                throw new IllegalArgumentException("Manager ID and Minimum Years of Experience must be non-negative.");
            }

            EmployeeManagerModel managerOpt = employeeManagerRepository.findByExistingManagerId(managerId);
            if (managerOpt != null) {
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime minJoiningDate = currentDate.minusYears(minYearsOfExperience);
                EmployeeManagerModel manager = managerOpt;
                List<EmployeeManagerModel> employeeList = employeeManagerRepository
                        .getEmployeesByManagerIdAndJoiningDate(managerId, minJoiningDate);
                List<EmployeeResponseDTO> employees = convertToEmployeeResponseDTO(employeeList);

                ResponseDTO.Details details = new ResponseDTO.Details();
                details.setAccountManager(manager.getName());
                details.setDepartment(manager.getDepartment());
                details.setId(manager.getId());
                details.setEmployeeList(employees);
                detailsList.add(details);
            } else {
                check = false;
                throw new IllegalArgumentException("Invalid Manager ID");
            }
        } else if (managerId != null) {
            if (Integer.parseInt(managerId) < 0) {
                throw new IllegalArgumentException("Invalid Manager ID");
            }

            EmployeeManagerModel managerOpt = employeeManagerRepository.findByExistingManagerId(managerId);
            if (managerOpt != null) {
                EmployeeManagerModel manager = managerOpt;
                if ("0".equals(manager.getManagerId())) {
                    List<EmployeeManagerModel> employeeList = employeeManagerRepository.findByManagerId(managerId);
                    List<EmployeeResponseDTO> employees = convertToEmployeeResponseDTO(employeeList);

                    ResponseDTO.Details details = new ResponseDTO.Details();
                    details.setAccountManager(manager.getName());
                    details.setDepartment(manager.getDepartment());
                    details.setId(manager.getId());
                    details.setEmployeeList(employees);
                    detailsList.add(details);
                }
            } else {
                throw new IllegalArgumentException("Invalid Manager ID");
            }
        } else if (minYearsOfExperience != null) {
            if (minYearsOfExperience < 0) {
                throw new IllegalArgumentException("Minimum Years of Experience must be non-negative.");
            }
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime minJoiningDate = currentDate.minusYears(minYearsOfExperience);

            List<EmployeeManagerModel> allManagers = employeeManagerRepository.findByManagerId("0");
            for (EmployeeManagerModel manager : allManagers) {
                List<EmployeeManagerModel> employeeList = employeeManagerRepository
                        .getEmployeesByManagerIdAndJoiningDate(manager.getId(), minJoiningDate);
                List<EmployeeResponseDTO> employees = convertToEmployeeResponseDTO(employeeList);

                ResponseDTO.Details details = new ResponseDTO.Details();
                details.setAccountManager(manager.getName());
                details.setDepartment(manager.getDepartment());
                details.setId(manager.getId());
                details.setEmployeeList(employees);
                detailsList.add(details);
            }
        } else {
            List<EmployeeManagerModel> allManagers = employeeManagerRepository.findByManagerId("0");
            for (EmployeeManagerModel manager : allManagers) {
                List<EmployeeManagerModel> employeeList = employeeManagerRepository.findByManagerId(manager.getId());
                List<EmployeeResponseDTO> employees = convertToEmployeeResponseDTO(employeeList);

                ResponseDTO.Details details = new ResponseDTO.Details();
                details.setAccountManager(manager.getName());
                details.setDepartment(manager.getDepartment());
                details.setId(manager.getId());
                details.setEmployeeList(employees);
                detailsList.add(details);
            }
        }

        ResponseDTO responseDTO = new ResponseDTO(managerId);
        if (check) {
            responseDTO.setMessage("Successfully fetched");
            if (!detailsList.isEmpty()) {
                responseDTO.setDetails(detailsList);
            }
        } else {
            responseDTO.setMessage("Invalid Manager ID");
        }

        return responseDTO;
    }

    // PUT
    @Transactional
    public ManagerChangeResponseDTO changeManager(String empId, String newManagerId) {
        Optional<EmployeeManagerModel> employeeOpt = employeeManagerMainRepository.findById(empId);

        String name = null;
        String oldManagerName = null;
        String newManagerName = null;

        ManagerChangeResponseDTO responseMessage = new ManagerChangeResponseDTO();

        if (employeeOpt != null) {
            EmployeeManagerModel employee = employeeOpt.get();

            String oldManagerId = employee.getManagerId();

            Optional<EmployeeManagerModel> newManagerOpt = employeeManagerMainRepository.findById(newManagerId);
            if (newManagerOpt.isPresent()) {

                EmployeeManagerModel newManager = newManagerOpt.get();

                if (!"0".equals(newManager.getManagerId())) { // Changed to String comparison
                    throw new IllegalArgumentException("New manager must be a valid manager of the department.");
                }

                if ("0".equals(employee.getManagerId())) { // Changed to String comparison
                    throw new IllegalArgumentException(
                            "Employee is already a manager. Cannot assign manager to another manager");
                }

                Optional<EmployeeManagerModel> oldManagerOpt = employeeManagerMainRepository.findById(oldManagerId);

                EmployeeManagerModel  oldManager = oldManagerOpt.get();
                oldManagerName = oldManager.getName();
                name = employee.getName();
                newManagerName = newManager.getName();

                if (oldManagerId.equals(newManagerId)) {
                    throw new IllegalArgumentException(
                            "Employee is currently working under the manager with id " + newManagerId);
                }

                newManagerName = newManager.getName();

                employee.setManagerId(newManagerId);
                employee.setDepartment(newManager.getDepartment());
                employee.setUpdatedTime(LocalDateTime.now());
                employeeManagerMainRepository.save(employee);

                name = employee.getName();

                responseMessage.setMessage(name + "'s manager has been succesfully changed from " + oldManagerName
                        + " to " + newManagerName);
            } else {
                responseMessage.setMessage("Cannot find new Manager with id " + newManagerId + " in the organisation");
            }
        } else {
            responseMessage.setMessage("Cannot find employee with id " + empId + " in the organisation");
        }

        return responseMessage;
    }

    // DELETE
    @Transactional
    public ResponseMessage deleteEmployee(String id) {
        ResponseMessage responseMessage = new ResponseMessage();
        EmployeeManagerModel employeeOpt = employeeManagerRepository.findByIdCustom(id);
        String name = null;
        if (employeeOpt != null) {
            EmployeeManagerModel employee = employeeOpt;
            String managerId = employee.getManagerId();
            name = employee.getName();

            if ("0".equals(managerId)) {
                List<EmployeeManagerModel> employeesUnderManager = employeeManagerRepository.findByManagerId(id);
                if (!employeesUnderManager.isEmpty()) {
                    throw new IllegalArgumentException("Cannot delete employee as they have employees to manage.");
                }
            }

            employeeManagerMainRepository.deleteById(id);
            responseMessage.setMessage("Successfully deleted " + name + " from the organization.");
        } else {
            responseMessage.setMessage("Cannot find employee with ID " + id + " in the organization.");

        }
        return responseMessage;
    }

    // Helper method to check if email format is valid
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public List<EmployeeResponseDTO> convertToEmployeeResponseDTO(List<EmployeeManagerModel> employeeList) {
        return employeeList.stream()
                .map(employee -> {
                    EmployeeResponseDTO dto = new EmployeeResponseDTO();
                    dto.setId(employee.getId());
                    dto.setName(employee.getName());
                    dto.setDesignation(employee.getDesignation());
                    dto.setEmail(employee.getEmail());
                    dto.setDepartment(employee.getDepartment());
                    dto.setMobile(employee.getMobile());
                    dto.setLocation(employee.getLocation());
                    dto.setDateOfJoining(employee.getDateOfJoining());
                    dto.setCreatedTime(employee.getCreatedTime());
                    dto.setUpdatedTime(employee.getUpdatedTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
