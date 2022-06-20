package me.elaamiri.dependencyInjector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DIInjected {
    String value() default "n/a"; // the name of the instances; if more than instance,
    // if one instance inject it
}
