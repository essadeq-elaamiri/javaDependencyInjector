package me.elaamiri.testSample.dao;

import me.elaamiri.dependencyInjector.annotations.DIBean;

@DIBean(name = "impl1")
public class EmployeeDaoImpl1 implements EmployeeDao{
    @Override
    public String getDAOAction() {
        return "Impl 1";
    }
}
