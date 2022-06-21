package me.elaamiri.dependencyInjector;

import lombok.extern.slf4j.Slf4j;
import me.elaamiri.dependencyInjector.annotations.DIBean;
import me.elaamiri.dependencyInjector.annotations.DIInjected;
import me.elaamiri.dependencyInjector.constantes.Constants;
import me.elaamiri.dependencyInjector.entities.Bean;
import me.elaamiri.dependencyInjector.entities.BeanField;
import me.elaamiri.dependencyInjector.entities.Beans;
import me.elaamiri.dependencyInjector.entities.Context;
import me.elaamiri.dependencyInjector.enums.FieldInjectionType;
import me.elaamiri.dependencyInjector.exceptions.BeanExistsException;
import me.elaamiri.dependencyInjector.exceptions.BeanFieldExistsException;
import me.elaamiri.dependencyInjector.exceptions.BeansCouldNotBeLoadedException;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class DependencyInjector {

    /***
     *
     * @param configFilePath
     * @return
     * @throws BeansCouldNotBeLoadedException
     */
    public static Context runXMLInjector(String configFilePath) throws BeansCouldNotBeLoadedException {

        /**
         * TODO:
         * 1- read xml file; create a beans, adding them to beans list,[done]
         * 2- do injection  (properties)
         * 3- do injection (constructor)
         * 4- validating file use xsd
         *
         */

        Beans beans = loadBeansFromConfigFile(Constants.DEFAULT_CONFIG_FILE);
        Context context = new Context();
        HashMap<String, Object> contextBeans = new HashMap<>();
        // load classes
        beans.getBeans().forEach(bean -> {
            try {
                Class beanClass = Class.forName(bean.getClassName());
                // create instances
                Object beanInstance = beanClass.getDeclaredConstructor().newInstance();
                contextBeans.put(bean.getName(), beanInstance);
                // check if bean has properties to
                if (!bean.getBeanFields().isEmpty()) {
                    bean.getBeanFields().forEach(beanField -> {
                                Method beanFieldSetter = null;
                                if (beanField.getInjectInto().equals(FieldInjectionType.SETTER)) {
                                    try {
                                        // get the setter
                                        beanFieldSetter = beanClass.getDeclaredMethod(beanField.getDefaultSetterName(), contextBeans.get(beanField.getValue()).getClass().getInterfaces());
                                        // pass the value (injection) (invoking setter)
                                        beanFieldSetter.invoke(beanInstance, contextBeans.get(beanField.getValue()));

                                    } catch (NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    } catch (InvocationTargetException e) {
                                        throw new RuntimeException(e);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else if (beanField.getInjectInto().equals(FieldInjectionType.FIELD)) {

                                    try {
                                        // if there is no setter methode use field based injection
                                        Field field = beanClass.getDeclaredField(beanField.getName());
                                        // Set the accessibility as true
                                        field.setAccessible(true);
                                        field.set(beanInstance, contextBeans.get(beanField.getValue()));
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    } catch (NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                }


                            }
                    );
                }

                context.addToInstancesMap(bean.getName(), beanInstance);


            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (BeanExistsException e) {
                throw new RuntimeException(e);
            }
        });
        return context;


    }

    /***
     *
     * @param filePath
     * @return
     * @throws BeansCouldNotBeLoadedException
     */
    private static Beans loadBeansFromConfigFile(String filePath) throws BeansCouldNotBeLoadedException {
        JAXBContext jaxbContext;
        Optional<Beans> beansOptional = null;
        try {
            jaxbContext = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Beans beans = (Beans) unmarshaller.unmarshal(new File(filePath));
            beansOptional = Optional.of(beans);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return beansOptional.orElseThrow(() -> {
            return new BeansCouldNotBeLoadedException();
        });
    }


    /***
     *
     * @throws BeanExistsException
     */


    // test write to the file
    public static void fun() throws BeanExistsException, BeanFieldExistsException {
        {

            Beans diBeans = new Beans();
            Bean diBean;
            for (int i = 1; i <= 4; i++) {
                diBean = new Bean();
                diBean.setName("namei" + i);
                diBean.setClassName("className" + i);
                diBeans.addBean(diBean);

                BeanField beanField = new BeanField();
                beanField.setName("k" + i);
                beanField.setValue("fhfh" + i);
                beanField.setInjectInto(FieldInjectionType.FIELD);

                diBean.addBeanField(beanField);
            }


            try {
                //serializing object
                // Task.class => give JAXB the structure
                //of the objects that will be serialized
                JAXBContext jaxbContext =
                        JAXBContext.newInstance(me.elaamiri.dependencyInjector.entities.Beans.class);

                //  convert object to XML
                Marshaller marshaller = jaxbContext.createMarshaller();

                //formating xml output
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(diBeans, new File("test.xml"));


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("Hello World!");
        }
    }


    public static Context runAnnotationsInjector(String scopePackage) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, BeanExistsException {
        Context context = new Context();
        HashMap<String, Object> beansInstances = new HashMap<>();
        // default scope is the root package
        if (scopePackage == null) scopePackage =  DependencyInjector.class.getPackageName().substring(0, DependencyInjector.class.getPackageName().indexOf('.'));
        //**** logging
        System.out.println("(1) Processing annotations in package: ["+scopePackage+"] ....");

        // Search all the annotated @DIBean classes and instantiate them
        Set<Class<?>> beansToBeInjected =  getAllBeans(scopePackage);
        //**** logging
        System.out.println("(2) Classes found : ["+beansToBeInjected.size()+"]");
        beansToBeInjected.forEach(aClass -> System.out.println(aClass.getName()));

        beansInstances = instantiateAllBeans(beansToBeInjected);
        //**** logging
        System.out.println("(3) Created instances : ["+beansInstances.size()+"]");
        beansInstances.forEach((name, object) -> System.out.println(name));

        // adding them to context
        beansInstances.forEach((s, o) -> {
            try {
                context.addToInstancesMap(s, o);
            } catch (BeanExistsException e) {
                throw new RuntimeException(e);
            }
        });


        for (Class bean : beansToBeInjected){
            //**** logging
            System.out.println("(4) Processing class : ["+bean.getName()+"]");

            // finding annotated fields @DIInjected and do injection
            for (Field field : bean.getDeclaredFields()){
                //**** logging
                System.out.println("(5) Processing class field : ["+field.getName()+"]");

                if(field.isAnnotationPresent(DIInjected.class)){
                    field.setAccessible(true);
                    // get annotation
                    DIInjected fieldAnnotation = field.getAnnotation(DIInjected.class);
                    // check the type
                    Class fieldType = field.getType();
                    // check name if exist find bean with the same name if exists
                    // if not throw exception
                    // if no name search all context beans of that type if one inject it (if the same name)
                    String beanInstanceName = fieldAnnotation.value(); // bean to be injected in this field

                    if(beanInstanceName.equals("n/a") || beanInstanceName.equals("")){ // default, so use the instance name
                        // find a bean with the same type from the list
                        for (Object instance: beansInstances.values()){
                            // check if the instance can be affected to the field
                            if(fieldType.isAssignableFrom(instance.getClass())){
                                // affect it to it
                                //**** logging
                                System.out.println("(6) Do injection by type... : ["+field.getName()+"]");

                                field.set(bean, instance);
                            }
                        }
                        // find all if > 1 error


                        // if 1 do injection
                    }else{ // there is a name
                        // find with name in context beans list
                        // if < 1 error
                        // if name ok but type not error

                        // name ok and type ok do injection

                        for (String instanceName: beansInstances.keySet()){
                            // check same names
                            if(beanInstanceName.equals(instanceName)){
                                // affect it to it

                                //**** logging
                                System.out.println("(7) Do injection by name... : ["+field.getName()+"]");
                                field.set(bean, beansInstances.get(instanceName));
                            }
                        }

                    }
                }
            }


        }
        return context;
    }

    private static HashMap<String, Object> instantiateAllBeans(Set<Class<?>> beans) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HashMap<String, Object> instances = new HashMap<>();

        for (Class bean : beans) {
            // if there is @DIBean anootation
            if (bean.isAnnotationPresent(DIBean.class)) {
                // get annotation
                DIBean beanAnnotation = (DIBean) bean.getAnnotation(DIBean.class);
                // Instanciation
                Object beanInstance = bean.getDeclaredConstructor().newInstance();
                String instanceName = beanAnnotation.name().equals("n/a") || beanAnnotation.name().equals("") ? bean.getName() : beanAnnotation.name();

                instances.put(instanceName, beanInstance);
            }
        }
        return instances;
    }

    private static Set<Class<?>> getAllBeans(String scopePackage){
        //Reflections reflections = new Reflections(".*");
        Reflections reflections = new Reflections(scopePackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(DIBean.class);
        return annotated;
    }

}

