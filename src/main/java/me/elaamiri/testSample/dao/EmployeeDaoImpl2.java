package me.elaamiri.testSample.dao;

import me.elaamiri.dependencyInjector.annotations.DIBean;

@DIBean(name = "impl2")

public class EmployeeDaoImpl2 implements EmployeeDao{
    @Override
    public String getDAOAction() {
        return "Impl 2";
    }
}
