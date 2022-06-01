package me.elaamiri.testSample.presentation;


import me.elaamiri.dependencyInjector.DependencyInjector;
import me.elaamiri.dependencyInjector.entities.Beans;
import me.elaamiri.dependencyInjector.entities.Context;
import me.elaamiri.dependencyInjector.exceptions.BeanNotFoundException;
import me.elaamiri.dependencyInjector.exceptions.BeansCouldNotBeLoadedException;
import me.elaamiri.testSample.dao.EmployeeDaoImpl1;
import me.elaamiri.testSample.entities.Employee;
import me.elaamiri.testSample.service.EmployeeService;
import me.elaamiri.testSample.service.EmployeeServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Application {
    public static void main(String[] args) throws BeanNotFoundException, BeansCouldNotBeLoadedException {
        /**
         * TODO: show the  first Employee from the list, get his salary value in $, after that find the employee with name contains 'z' in the list
         * To that, This application needs an instance of EmployeeServiceImp, which present these functionalities
         *
         */

        final List<String> names = List.of("Ahmed Zaytoun", "Khalid Rachid", "Aziza lahrach");

        List<Employee> employees = names.stream().map(name -> {
            Employee employee = new Employee();
            employee.setName(name);
            employee.setSalary((Math.random() * 20000)+5000);
            employee.setEmail(name.replace(" ", "_") + "@gmail.com");
            return employee;
        }).toList();

        System.out.println(employees.toString());
        /*
        [Employee(name=Ahmed Zaytoun, email=Ahmed_Zaytoun@gmail.com, salary=20468.14008039223, department=null), Employee(name=Khalid Rachid, email=Khalid_Rachid@gmail.com, salary=15002.383245338471, department=null), Employee(name=Aziza lahrach, email=Aziza_lahrach@gmail.com, salary=22051.042755038638, department=null)]
         */
        //A. // Here is the solution with the bad way (Strong dependency).
        EmployeeService employeeService = new EmployeeServiceImpl();
        // show the first
        System.out.println(employeeService.showEmployee(employees.get(0)));


        // using our framework.
        /*
        In this case we will inject an instance of EmployeeServiceImpl to our Application.
        1. By XML configuration file (in Field, contractor, and via setter)
        In the other application
        2. By Annotations (in Field, contractor, and via setter) (@InjectTo) (@Injectable)
         */

        // lets test marshamming
        //DependencyInjector.fun();

        // test
        Context context = DependencyInjector.runInjector(null);
        EmployeeServiceImpl service = (EmployeeServiceImpl) context.getBeanByName("employeeServiceImpl");
        System.out.println(service.getServiceMessage());
        service.setEmployeeDao(new EmployeeDaoImpl1());
    }

}

