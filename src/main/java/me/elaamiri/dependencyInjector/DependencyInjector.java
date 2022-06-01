package me.elaamiri.dependencyInjector;

import me.elaamiri.dependencyInjector.constantes.Constants;
import me.elaamiri.dependencyInjector.entities.Bean;
import me.elaamiri.dependencyInjector.entities.BeanField;
import me.elaamiri.dependencyInjector.entities.Beans;
import me.elaamiri.dependencyInjector.entities.Context;
import me.elaamiri.dependencyInjector.exceptions.BeanExistsException;
import me.elaamiri.dependencyInjector.exceptions.BeansCouldNotBeLoadedException;
import me.elaamiri.testSample.dao.EmployeeDao;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;

public class DependencyInjector {

    public static Context runInjector(String configFilePath) throws BeansCouldNotBeLoadedException {

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
                if (!bean.getBeanFields().isEmpty()){
                    bean.getBeanFields().forEach(beanField -> {
                        // get the setter
                        try {
                            //Method beanFieldSetter = beanClass.getMethod(beanField.getDefaultSetterName(), contextBeans.get(beanField.getValue()).getClass());
                            //Method beanFieldSetter = beanClass.getDeclaredMethod(beanField.getDefaultSetterName(), contextBeans.get(beanField.getValue()).getClass());
                            Method beanFieldSetter = beanClass.getDeclaredMethod("setEmployeeDao", EmployeeDao.class);
                            // pass the value (injection) (invoking setter)
                            beanFieldSetter.invoke(beanInstance, contextBeans.get(beanField.getValue()));


                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }

                    });
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

    /*
    // test write to the file
    public static void fun() throws BeanExistsException {
        {

            Beans diBeans = new Beans();
            Bean diBean;
            for (int i = 1; i <= 4; i++) {
                diBean = new Bean();
                diBean.setName("name");
                diBean.setClassName("className");
                diBeans.addBean(diBean);

                BeanField beanField = new BeanField();
                beanField.setName("k");
                beanField.setValue("fhfh");

                //diBean.addBeanField(beanField);
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
    */
}

