package com.sql.MySql.response;

import java.util.List;

public class ResponseDTO {
    private String message;
    private List<Details> details;

    public ResponseDTO(String managerId) {
        // Constructor logic if needed
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public static class Details {
        private String accountManager;
        private String department;
        private String id; // Changed to String
        private List<EmployeeResponseDTO> employeeList;

        public String getAccountManager() {
            return accountManager;
        }

        public void setAccountManager(String accountManager) {
            this.accountManager = accountManager;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getId() { // Changed to String
            return id;
        }

        public void setId(String id) { // Changed to String
            this.id = id;
        }

        public List<EmployeeResponseDTO> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(List<EmployeeResponseDTO> employeeList) {
            this.employeeList = employeeList;
        }
    }
}
