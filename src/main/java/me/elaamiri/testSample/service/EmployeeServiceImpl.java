package me.elaamiri.testSample.service;

import me.elaamiri.dependencyInjector.annotations.DIBean;
import me.elaamiri.dependencyInjector.annotations.DIInjected;
import me.elaamiri.testSample.dao.EmployeeDao;


@DIBean(name = "service")
public class EmployeeServiceImpl implements EmployeeService{

    @DIInjected(value = "impl1")
    private EmployeeDao employeeDao; // to be injected

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    /*
        public void setEmployeeDao(EmployeeDao employeeDao) {
            this.employeeDao = employeeDao;
        }
       */
    @Override
    public String getServiceMessage(){
        return employeeDao.getDAOAction();
        //return "Done";
    }



}
