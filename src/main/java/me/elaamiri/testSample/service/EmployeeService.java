package me.elaamiri.testSample.service;

import me.elaamiri.testSample.entities.Employee;
import me.elaamiri.testSample.enumerations.CurrencyType;

import java.util.List;

public interface EmployeeService {
    List<Employee> getEmployeesByName(List<Employee> employees ,String keyword);
    double getSalaryInCurrency(Employee employee, CurrencyType currency);
    String showEmployee(Employee employee);
}
