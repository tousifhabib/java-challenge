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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller for managing employees.
 */
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    /**
     * Constructs an EmployeeController with the given EmployeeService.
     *
     * @param employeeService the service to be used for employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves a list of all employees.
     *
     * @return ResponseEntity containing a list of all employees
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        LOG.info("Retrieving all employees");
        List<Employee> employees = employeeService.retrieveEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return ResponseEntity containing the employee with the specified ID
     * @throws ResourceNotFoundException if the employee with the given ID is not found
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") @ValidEmployeeId Long employeeId) {
        LOG.info("Retrieving employee with ID: {}", employeeId);
        try {
            Employee employee = employeeService.getEmployee(employeeId);
            return ResponseEntity.ok(employee);
        } catch (EmployeeNotFoundException e) {
            LOG.error("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    /**
     * Saves an employee.
     *
     * @param employee the employee to be saved
     * @return the saved employee object
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        LOG.info("Saving new employee");
        Employee savedEmployee = employeeService.saveEmployee(employee);
        LOG.info("Employee Saved Successfully");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmployee.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedEmployee);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     * @return ResponseEntity with status 200 (OK) and a success message in the body
     * @throws ResourceNotFoundException if the employee with the given ID is not found
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

    /**
     * Updates an existing employee.
     *
     * @param employee   the employee object with updated details
     * @param employeeId the ID of the employee to be updated
     * @return ResponseEntity containing the updated employee
     * @throws IllegalArgumentException if the provided employeeId does not match the ID of the employee object
     * @throws ResourceNotFoundException if the employee with the given ID is not found
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee,
                                                   @PathVariable(name = "employeeId") @ValidEmployeeId Long employeeId) {
        LOG.info("Updating employee with ID: {}", employeeId);
        if (!employeeId.equals(employee.getId())) {
            LOG.error("Mismatched employee IDs in request. Path variable ID: {}, Employee object ID: {}", employeeId, employee.getId());
            throw new IllegalArgumentException("Mismatched employee IDs in request");
        }

        try {
            employeeService.updateEmployee(employee);
            Employee updatedEmployee = employeeService.getEmployee(employeeId);
            LOG.info("Employee Updated Successfully");
            return ResponseEntity.ok(updatedEmployee);
        } catch (EmployeeNotFoundException e) {
            LOG.error("Employee not found with ID: {}", employeeId);
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }
    }
}