package me.elaamiri.dependencyInjector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DIBean {
    final  String  NAME =  DIBean.class.getName();
    String name() default "n/a"; // the instance name

}
