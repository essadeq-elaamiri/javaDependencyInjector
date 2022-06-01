package me.elaamiri.dependencyInjector.entities;

import lombok.Data;
import me.elaamiri.dependencyInjector.enums.FieldInjectionType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BeanField implements Serializable {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String value;

    @XmlAttribute
    private FieldInjectionType injectInto;


    public String getDefaultSetterName(){
        String setterName = "set".concat(String.valueOf(name.charAt(0)).toUpperCase().concat(name.substring(1)));
        System.out.println(setterName);
        return setterName;
    }
}
