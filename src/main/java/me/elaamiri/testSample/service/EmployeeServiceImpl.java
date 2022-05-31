package me.elaamiri.testSample.service;

import me.elaamiri.testSample.entities.Employee;
import me.elaamiri.testSample.enumerations.CurrencyType;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeServiceImpl implements EmployeeService{
    @Override
    public List<Employee> getEmployeesByName(List<Employee> employees,String keyword) {
        return employees.stream().filter(employee -> {
           return employee.getName().contains(keyword);
        }).collect(Collectors.toList());
    }

    @Override
    public double getSalaryInCurrency(Employee employee, CurrencyType currency) {
        if(currency.equals(CurrencyType.DOLLAR)) return employee.getSalary()* 8.16;
        if (currency.equals(CurrencyType.EURO)) return employee.getSalary() * 9.88;
        return 0;
    }

    @Override
    public String showEmployee(Employee employee) {
        String base = """
                Employee : ${NAME}
                Contact: ${EMAIL}
                Salary: ${INMDH} MDH
                Department: ${DEP}
                """;
        String empStr = base.replace("${NAME}", employee.getName())
                .replace("${EMAIL}", employee.getEmail())
                .replace("${INMDH}", String.valueOf(employee.getSalary()))
                .replace("${DEP}", employee.getDepartment()==null? "Undefined" : employee.getDepartment());
        return empStr;
    }
}
