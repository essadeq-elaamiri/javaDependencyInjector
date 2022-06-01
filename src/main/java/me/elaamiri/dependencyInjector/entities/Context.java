package me.elaamiri.dependencyInjector.entities;

import me.elaamiri.dependencyInjector.exceptions.BeanExistsException;

import java.util.HashMap;

public class Context {
    private  final HashMap<String, Object> instancesMap = new HashMap<>();

    public  Object addToInstancesMap(String name, Object object) throws BeanExistsException {
        if(instancesMap.containsKey(name) || instancesMap.containsValue(object)) throw new BeanExistsException();
        instancesMap.put(name, object);
        return object;
    }

    public  Object getBeanByName(String name){
        return instancesMap.getOrDefault(name, null);
    }



}
