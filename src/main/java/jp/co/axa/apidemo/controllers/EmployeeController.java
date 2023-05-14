package jp.co.axa.apidemo.controllers;

import javax.validation.Valid;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.validators.ValidEmployeeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The EmployeeController class handles HTTP requests related to employee data.
 */
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    /**
     * Constructs an EmployeeController with the given EmployeeService.
     *
     * @param employeeService the EmployeeService to be used for employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a list of all employees.
     *
     * @return the list of employees
     */
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        LOG.info("Retrieving all employees");
        return employeeService.retrieveEmployees();
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return the employee with the specified ID
     * @throws ResourceNotFoundException if the employee with the given ID is not found
     */
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") @ValidEmployeeId Long employeeId) {
        LOG.info("Retrieving employee with ID: {}", employeeId);
        try {
            return employeeService.getEmployee(employeeId);
        } catch (EmployeeNotFoundException e) {
            LOG.error("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    /**
     * Saves a new employee.
     *
     * @param employee the employee to be saved
     * @return ResponseEntity with status and body message
     */
    @PostMapping("/employees")
    public ResponseEntity<String> saveEmployee(@Valid @RequestBody Employee employee) {
        LOG.info("Saving new employee");
        employeeService.saveEmployee(employee);
        LOG.info("Employee Saved Successfully");
        return ResponseEntity.status(201).body("Employee Created Successfully");
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     * @return ResponseEntity with status and body message
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(name = "employeeId") @ValidEmployeeId Long employeeId) {
        LOG.info("Deleting employee with ID: {}", employeeId);
        try {
            Employee employee = employeeService.getEmployee(employeeId);
            employeeService.deleteEmployee(employeeId);
            LOG.info("Employee Deleted Successfully");
            return ResponseEntity.ok().body("Employee Deleted Successfully");
        } catch (EmployeeNotFoundException e) {
            LOG.error("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<String> updateEmployee(@Valid @RequestBody Employee employee,
                                                 @PathVariable(name = "employeeId") @ValidEmployeeId Long employeeId) {
        LOG.info("Updating employee with ID: {}", employeeId);
        if (!employeeId.equals(employee.getId())) {
            LOG.error("Mismatched employee IDs in request. Path variable ID: {}, Employee object ID: {}", employeeId, employee.getId());
            throw new IllegalArgumentException("Mismatched employee IDs in request");
        }

        try {
            employeeService.updateEmployee(employee);
            LOG.info("Employee Updated Successfully");
            return ResponseEntity.ok().body("Employee Updated Successfully");
        } catch (EmployeeNotFoundException e) {
            LOG.error("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
    }
}