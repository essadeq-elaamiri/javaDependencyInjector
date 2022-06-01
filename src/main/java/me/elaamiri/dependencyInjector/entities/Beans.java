package me.elaamiri.dependencyInjector.entities;

import lombok.Data;
import me.elaamiri.dependencyInjector.exceptions.BeanNotFoundException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Beans implements Serializable {
    @XmlElement(name="Bean")
    private List<Bean> beans = new ArrayList<Bean>();

    public Bean addBean(Bean bean){
        this.beans.add(bean);
        return  bean;
    }

    /***
     * Getting the bean with the given name
     * @param name
     * @return Bean
     * @throws BeanNotFoundException
     */
    public Bean getBeanByName(String name) throws BeanNotFoundException{
        Optional<Bean> bean2 = Optional.of(this.beans.stream().filter(bean -> bean.getName().equals(name)).collect(Collectors.toList()).get(0));
        return bean2.orElseThrow(() -> {
            return new BeanNotFoundException();
        });
    }

    public Bean getBeanByIndex(String name) throws BeanNotFoundException{
        Optional<Bean> bean2 = Optional.of(this.beans.stream().filter(bean -> bean.getName().equals(name)).collect(Collectors.toList()).get(0));
        return bean2.orElseThrow(() -> {
            return new BeanNotFoundException();
        });
    }



}
