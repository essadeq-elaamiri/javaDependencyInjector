package me.elaamiri.dependencyInjector;

import me.elaamiri.dependencyInjector.constantes.Constants;
import me.elaamiri.dependencyInjector.entities.Bean;
import me.elaamiri.dependencyInjector.entities.BeanField;
import me.elaamiri.dependencyInjector.entities.Beans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

public class DependencyInjector {

    public static Beans runInjector(String configFilePath){

        /**
         * TODO:
         * 1- read xml file; create a beans, adding them to beans list,
         * 2- do injection  (properties)
         * 3- do injection (constructor)
         * 4- validating file use xsd
         *
         */




        return null;

    }

    // test write to the file
    public static void fun(){
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
}
