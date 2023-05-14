package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of the EmployeeService interface.
 * This service is responsible for the CRUD operations for employees.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieve all employees.
     *
     * @return the list of all employees
     */
    @Override
    public List<Employee> retrieveEmployees() {
        LOGGER.info("Retrieving all employees");
        return employeeRepository.findAll();
    }

    /**
     * Retrieve an employee by their id.
     *
     * @param employeeId the id of the employee
     * @return the employee if found
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    @Override
    public Employee getEmployee(Long employeeId) {
        Objects.requireNonNull(employeeId, "Employee id must not be null");
        LOGGER.info("Retrieving employee with id: " + employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " was not found"));
    }

    /**
     * Save an employee.
     *
     * @param employee the employee to save
     */
    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        try {
            LOGGER.info("Saving employee: " + employee);
            employeeRepository.save(employee);
        } catch (Exception e) {
            LOGGER.error("Error occurred while saving employee: ", e);
            throw e;
        }
    }

    /**
     * Delete an employee by their id.
     *
     * @param employeeId the id of the employee
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    @Override
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Objects.requireNonNull(employeeId, "Employee id must not be null");
        LOGGER.info("Deleting employee with id: " + employeeId);
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " was not found");
        }
    }

    /**
     * Update an existing employee.
     *
     * @param employee the employee with updated details
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    @Override
    @Transactional
    public void updateEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee must not be null");
        Objects.requireNonNull(employee.getId(), "Employee id must not be null");
        LOGGER.info("Updating employee: " + employee);
        if (employeeRepository.existsById(employee.getId())) {
            employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFoundException("Employee with id " + employee.getId() + " was not found");
        }

    }
}