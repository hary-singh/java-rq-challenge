package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeRequest;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.example.rqchallenge.employees.repository.EmployeeRepository;
import com.example.rqchallenge.util.HttpHeaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private static final String ALL_EMPLOYEES_ENDPOINT = "/employees";
    private static final String EMPLOYEE_BY_ID_ENDPOINT = "/employee/";
    private static final String CREATE_EMPLOYEE_ENDPOINT = "/create";
    private static final String DELETE_EMPLOYEE_ENDPOINT = "/delete/";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;

    public EmployeeService(RestTemplate restTemplate,
                           @Value("${employee.service.endpoint}") String baseUrl,
                           EmployeeRepository employeeRepository,
                           ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * Fetches all employees from the external API and saves them to the repository.
     *
     * @return a list of all employees, or an empty list if an error occurs
     */
    public List<Employee> getAllEmployees() {
        final String requestUrl = baseUrl + ALL_EMPLOYEES_ENDPOINT;
        HttpHeaders headers = HttpHeaderUtil.createHeadersWithCookie();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<EmployeeResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, EmployeeResponse.class);
            EmployeeResponse response = responseEntity.getBody();
            List<Employee> employees = Optional.ofNullable(response).map(EmployeeResponse::getData).orElse(Collections.emptyList());

            if (!employees.isEmpty()) {
                employeeRepository.saveAll(employees);
            }
            return employees;
        } catch (Exception e) {
            log.error("Error while fetching employees from External API", e);
            return Collections.emptyList();
        }
    }


    /**
     * Searches for employees by name.
     *
     * @param name the name to search for
     * @return a list of employees whose names contain the search term, or an empty list if an error occurs
     */
    public List<Employee> getEmployeesByNameSearch(String name) {
        final String requestUrl = baseUrl + ALL_EMPLOYEES_ENDPOINT;
        HttpHeaders headers = HttpHeaderUtil.createHeadersWithCookie();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<EmployeeResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, EmployeeResponse.class);
            EmployeeResponse response = responseEntity.getBody();
            List<Employee> employees = Optional.ofNullable(response).map(EmployeeResponse::getData).orElse(Collections.emptyList());

            return employees.stream()
                    .filter(employee -> employee.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error while fetching employees from External API", e);
            return Collections.emptyList();
        }
    }


    /**
     * Fetches an employee by ID.
     *
     * @param id the ID of the employee
     * @return the employee with the given ID, or null if not found
     */
    public Employee getEmployeeById(String id) {
        final String requestUrl = baseUrl + EMPLOYEE_BY_ID_ENDPOINT + id;
        HttpHeaders headers = HttpHeaderUtil.createHeadersWithCookie();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<EmployeeResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, EmployeeResponse.class);
            EmployeeResponse response = responseEntity.getBody();
            List<Employee> employees = Optional.ofNullable(response).map(EmployeeResponse::getData).orElse(null);
            return employees != null && !employees.isEmpty() ? employees.get(0) : null;
        } catch (Exception e) {
            log.error("Error while fetching employee by ID from External API", e);
            return null;
        }
    }


    /**
     * Gets the highest salary of all employees.
     *
     * @return the highest salary, or null if an error occurs
     */
    public Long getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = getAllEmployees();
            return employees.stream()
                    .map(Employee::getSalary)
                    .max(Long::compare)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error while fetching the highest salary of employees", e);
            return null;
        }

//        Alternative method: Querying the database directly since we have the data stored
//            Employee employee = employeeRepository.findTopByOrderBySalaryDesc();
//            return employee != null ? employee.getSalary() : null;
    }


    /**
     * Gets the top ten highest earning employee names.
     *
     * @return a list of the top ten highest earning employee names, or an empty list if an error occurs
     */
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> employees = getAllEmployees();
            return employees.stream()
                    .sorted((e1, e2) -> Long.compare(e2.getSalary(), e1.getSalary()))
                    .map(Employee::getName)
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error while fetching the top ten highest earning employee names", e);
            return Collections.emptyList();
        }

        // Alternative approach: Querying the database directly for the top 10 highest earning employees
        // This can be done using a distinct top 10 query on the database via the repository.
        // Example: SELECT DISTINCT name FROM employees ORDER BY salary DESC LIMIT 10;
    }


    /**
     * Creates a new employee.
     *
     * @param employeeRequest the request containing employee details
     * @return the created employee, or null if an error occurs
     */
    public Employee createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .setId(employeeRequest.getId())
                .setName(employeeRequest.getName())
                .setSalary(Long.parseLong(employeeRequest.getSalary()))
                .setAge(Integer.parseInt(employeeRequest.getAge()))
                .build();

        final String requestUrl = baseUrl + CREATE_EMPLOYEE_ENDPOINT;
        HttpHeaders headers = HttpHeaderUtil.createHeadersWithCookie();
        HttpEntity<String> entity;

        try {
            String requestBody = objectMapper.writeValueAsString(employee);
            entity = new HttpEntity<>(requestBody, headers);
        } catch (Exception e) {
            log.error("Error occurred while creating request entity: {}", e.getMessage());
            return null;
        }

        try {
            ResponseEntity<Employee> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Employee.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to create employee, status code: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Error occurred while creating employee: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Deletes an employee by ID.
     *
     * @param id the ID of the employee to delete
     * @return the name of the deleted employee, or null if an error occurs
     */
    public String deleteEmployee(String id) {
    final String requestUrl = baseUrl + DELETE_EMPLOYEE_ENDPOINT + id;
    HttpHeaders headers = HttpHeaderUtil.createHeadersWithCookie();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    try {
        // Fetch the employee by ID
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            log.error("Employee with ID {} not found", id);
            return null;
        }

        // Delete the employee
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.DELETE, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return employee.getName();
        } else {
            log.error("Failed to delete employee, status code: {}", response.getStatusCode());
            return null;
        }
    } catch (Exception e) {
        log.error("Error occurred while deleting employee: {}", e.getMessage());
        return null;
    }
}
}