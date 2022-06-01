package me.elaamiri.dependencyInjector.entities;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Bean implements Serializable {
    @XmlAttribute
    private String name; // unique
    @XmlAttribute
    private String className;
    @XmlElement(name="bean-field")
    private List<BeanField> beanFields = new ArrayList<>();

    public BeanField addBeanField(BeanField beanField){
        this.beanFields.add(beanField);
        return beanField;
    }
}
