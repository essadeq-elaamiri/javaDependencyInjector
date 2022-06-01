package me.elaamiri.dependencyInjector.entities;

import lombok.Data;

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
}
