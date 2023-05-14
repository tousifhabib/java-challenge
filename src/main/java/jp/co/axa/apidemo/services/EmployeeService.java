package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.EmployeeNotFoundException;

import java.util.List;

/**
 * Service for managing employees.
 */
public interface EmployeeService {

    /**
     * Retrieve all employees.
     *
     * @return the list of all employees
     */
    public List<Employee> retrieveEmployees();

    /**
     * Retrieve an employee by their id.
     *
     * @param employeeId the id of the employee
     * @return the employee if found
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    public Employee getEmployee(Long employeeId);

    /**
     * Save an employee.
     *
     * @param employee the employee to save
     * @return
     */
    public Employee saveEmployee(Employee employee);

    /**
     * Delete an employee by their id.
     *
     * @param employeeId the id of the employee
     */
    public void deleteEmployee(Long employeeId);

    /**
     * Update an existing employee.
     *
     * @param employee the employee with updated details
     */
    public void updateEmployee(Employee employee);
}
