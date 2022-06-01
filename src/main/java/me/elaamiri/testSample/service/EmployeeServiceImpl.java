package me.elaamiri.testSample.service;

import me.elaamiri.testSample.dao.EmployeeDao;


public class EmployeeServiceImpl implements EmployeeService{

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
