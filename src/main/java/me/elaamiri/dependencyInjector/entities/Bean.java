package me.elaamiri.dependencyInjector.entities;

import lombok.Data;
import me.elaamiri.dependencyInjector.exceptions.BeanFieldExistsException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public BeanField addBeanField(BeanField beanField) throws BeanFieldExistsException {
        if(this.beanFields.contains(beanField)) throw new BeanFieldExistsException();
        this.beanFields.add(beanField);
        return beanField;
    }



}
