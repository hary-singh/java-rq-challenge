package com.example.rqchallenge.employees.repository;

import com.example.rqchallenge.employees.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Employee findTopByOrderBySalaryDesc();
}
