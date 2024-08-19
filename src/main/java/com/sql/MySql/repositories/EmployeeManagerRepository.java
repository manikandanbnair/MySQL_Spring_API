package com.sql.MySql.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sql.MySql.models.EmployeeManagerModel;

@Repository
public interface EmployeeManagerRepository extends JpaRepository<EmployeeManagerModel, String> {

    boolean existsByEmail(String email);
   
}
