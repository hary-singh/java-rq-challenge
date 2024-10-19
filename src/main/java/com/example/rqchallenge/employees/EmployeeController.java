package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeRequest;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error occurred while fetching all employees: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@RequestParam("searchString") String searchString) {
        try {
            List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error occurred while searching employees by name: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error occurred while fetching employee by ID: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Long> getHighestSalaryOfEmployees() {
        try {
            Long highestSalary = employeeService.getHighestSalaryOfEmployees();
            return ResponseEntity.ok(highestSalary);
        } catch (Exception e) {
            log.error("Error occurred while fetching the highest salary of employees: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @GetMapping("/topTenHighestEarning")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
            return ResponseEntity.ok(topTenNames);
        } catch (Exception e) {
            log.error("Error occurred while fetching the top ten highest earning employee names: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        try {
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .setAge((String) employeeInput.get("age"))
                    .setName((String) employeeInput.get("name"))
                    .setSalary((String) employeeInput.get("salary"))
                    .build();

            Employee createdEmployee = employeeService.createEmployee(employeeRequest);

            if (createdEmployee != null) {
                return ResponseEntity.ok(createdEmployee);
            } else {
                return ResponseEntity.status(500).build();
            }
        } catch (Exception e) {
            log.error("Error occurred while creating employee: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try {
            String employeeName = employeeService.deleteEmployee(id);
            if (employeeName != null) {
                return ResponseEntity.ok("Employee " + employeeName + " deleted successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to delete employee");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting employee: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to delete employee");
        }
    }
}
