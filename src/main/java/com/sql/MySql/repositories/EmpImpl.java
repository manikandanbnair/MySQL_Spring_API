package com.sql.MySql.repositories;

import com.sql.MySql.models.EmployeeManagerModel;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EmpImpl implements EmployeeManagerCustomRepository {

    private final EntityManager entityManager;

    public EmpImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EmployeeManagerModel> getAllEmployees() {
        String query = "SELECT e FROM EmployeeManagerModel e";
        TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
        return typedQuery.getResultList();
    }

    @Override
    public EmployeeManagerModel findByIdCustom(String id) {
        try {
            String query = "SELECT e FROM EmployeeManagerModel e WHERE e.id = :id";
            TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
            typedQuery.setParameter("id", id);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle as needed
        }
    }

    @Override
    public EmployeeManagerModel findByExistingManagerId(String managerId) {
        try {
            String query = "SELECT e FROM EmployeeManagerModel e WHERE e.id = :managerId";
            TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
            typedQuery.setParameter("managerId", managerId);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle as needed
        }
    }

    @Override
    public EmployeeManagerModel findManagerByDepartment(String department) {
        try {
            String query = "SELECT e FROM EmployeeManagerModel e WHERE e.department = :department AND e.managerId = '0'";
            TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
            typedQuery.setParameter("department", department);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle as needed
        }
    }

    @Override
    public List<EmployeeManagerModel> getEmployeesByManagerIdAndJoiningDate(String managerId,
            LocalDateTime minJoiningDate) {
        // Calculate the minimum date of joining based on the minimum years of experience

        String query = "SELECT e FROM EmployeeManagerModel e WHERE e.managerId = :managerId AND e.dateOfJoining <= :minJoiningDate";
        TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
        typedQuery.setParameter("managerId", managerId);
        typedQuery.setParameter("minJoiningDate", minJoiningDate);
        return typedQuery.getResultList();
    }

    @Override
    public List<EmployeeManagerModel> findByManagerId(String managerId) {
        String query = "SELECT e FROM EmployeeManagerModel e WHERE e.managerId = :managerId";
        TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
        typedQuery.setParameter("managerId", managerId);
        return typedQuery.getResultList();
    }

    @Override
    public List<EmployeeManagerModel> findByDepartment(String department) {
        String query = "SELECT e FROM EmployeeManagerModel e WHERE e.department = :department";
        TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
        typedQuery.setParameter("department", department);
        return typedQuery.getResultList();
    }

    @Override
    public List<EmployeeManagerModel> findByDateOfJoiningBefore(LocalDate minJoiningDate) {
        String query = "SELECT e FROM EmployeeManagerModel e WHERE e.dateOfJoining < :minJoiningDate";
        TypedQuery<EmployeeManagerModel> typedQuery = entityManager.createQuery(query, EmployeeManagerModel.class);
        typedQuery.setParameter("minJoiningDate", minJoiningDate);
        return typedQuery.getResultList();
    }

    // @Override
    // public List<EmployeeManagerModel>
    // findByManagerIdAndDateOfJoiningBeforeOrEqual(String managerId, LocalDate
    // minJoiningDate) {
    // String query = "SELECT e FROM EmployeeManagerModel e WHERE e.managerId =
    // :managerId AND e.dateOfJoining <= :minJoiningDate";
    // TypedQuery<EmployeeManagerModel> typedQuery =
    // entityManager.createQuery(query, EmployeeManagerModel.class);
    // typedQuery.setParameter("managerId", managerId);
    // typedQuery.setParameter("minJoiningDate", minJoiningDate);
    // return typedQuery.getResultList();
    // }
}
