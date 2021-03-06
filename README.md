
| ENSET-M | II-BDCC2 | Architecture distribuée et Middlewares | ELAAMIRI essadeq |
| ---- |----------|---- | ---- |

# javaDependencyInjector


# Into
### The project
This project represent the basic concept of dependency injection by creating a basic dependency injection framework, based on xml and annotations configuration.
### Objective
The aim from this project is to learn what is dependency injection and how it works.

# Dependency injection


# Project structure


# Coding
## Used Dependencies 
```xml
    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api -->
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.4.0-b180830.0359</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.activation/activation -->
        <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>activation</artifactId>
            <version>1.1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime -->
        <dependency>
            <groupId>org.glassfish.jaxb</groupId>
            <artifactId>jaxb-runtime</artifactId>
            <version>2.3.0-b170127.1453</version>
        </dependency>

    </dependencies>

```
## Code

Configuration file 

`depinjectior.config.xml`
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<beans>
    <Bean name="employeeDaoImpl1" className="me.elaamiri.testSample.dao.EmployeeDaoImpl1">
    </Bean>
    <Bean name="employeeServiceImpl" className="me.elaamiri.testSample.service.EmployeeServiceImpl">
        <bean-field name="employeeDao" value="employeeDaoImpl1" injectInto="FIELD"/>
    </Bean>
</beans>

```

In this file, we specify the classes to be injected, where to be injected and how (FIELD, SETTER).
In this example I want to inject `EmployeeDaoImpl1` object in `EmployeeServiceImpl` object, which is
depending on the first to do some actions.
I choose the injection to be via a field in `EmployeeServiceImpl`, called `employeeDao`

Here is code example:

`EmployeeDao.java`
```java
public interface EmployeeDao {
    public String getDAOAction();
}
```

`EmployeeDaoImpl1.java`
```java
public class EmployeeDaoImpl1 implements EmployeeDao{
    @Override
    public String getDAOAction() {
        return "Impl 1";
    }
}
```

`EmployeeDaoImpl2.java`
```java
public class EmployeeDaoImpl2 implements EmployeeDao{
    @Override
    public String getDAOAction() {
        return "Impl 2";
    }
}
```


`EmployeeService.java`
```java
public interface EmployeeService {
    String getServiceMessage();
}
```

`EmployeeServiceImpl.java`
```java
public class EmployeeServiceImpl implements EmployeeService{
    private EmployeeDao employeeDao; // to be injected
    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }
    /*
        public void setEmployeeDao(EmployeeDao employeeDao) {
            this.employeeDao = employeeDao;
        }
       */
    @Override
    public String getServiceMessage(){
        return employeeDao.getDAOAction();
        //return "Done";
    }
}
```

## Tests

```java
void public static void main(String[]args){
        // test
        Context context = DependencyInjector.runInjector(null);
        EmployeeServiceImpl service = (EmployeeServiceImpl) context.getBeanByName("employeeServiceImpl");
        System.out.println(service.getServiceMessage());
        //service.setEmployeeDao(new EmployeeDaoImpl1());
        System.out.println(service.getEmployeeDao());        
}
```

## What just happened ?
It is easy-peasy  `/iːzɪˈpiːzi/`:
Here is the xml based framework structure:

![sc1](./screenshots/1.png)

Entities to represent the config xml file elements:

`BeanFiled.java` represent class property
```java
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
```

`Bean.java` represent the object 
```java
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
```


`Beans.java` represent the objects list
```java
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Beans implements Serializable {
    @XmlElement(name="Bean")
    private List<Bean> beans = new ArrayList<Bean>();

    public Bean addBean(Bean bean) throws BeanExistsException {
        if(this.beans.contains(bean)) throw new BeanExistsException();
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

    public Bean getBeanByIndex(int index) throws BeanNotFoundException{
        Optional<Bean> bean2 = Optional.of(this.beans.get(index));
        return bean2.orElseThrow(() -> {
            return new BeanNotFoundException();
        });
    }
}
```

And the main object which is the context, where I stock all the injectable instances in a hashMap.

`Context.java` represent the context
```java
public class Context {
    private  final HashMap<String, Object> instancesMap = new HashMap<>();

    public  Object addToInstancesMap(String name, Object object) throws BeanExistsException {
        if(instancesMap.containsKey(name) || instancesMap.containsValue(object)) throw new BeanExistsException();
        instancesMap.put(name, object);
        return object;
    }

    public  Object getBeanByName(String name){
        return instancesMap.getOrDefault(name, null);
    }
}
```

#### The main point, where the magic happens


`Context.java` represent the context
Retrieving Beans from xml file:
```java
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
```

Creating the instances and do the injection, after that return the context please, (which have been used in the test Application)

`Context.java` represent the context
```java
/***
 *
 * @param configFilePath
 * @return
 * @throws BeansCouldNotBeLoadedException
 */
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
```


TODO: XML
- [ ] validate config xml file (xsd)
- [X] on startup load the file create the beans 
- [X] inject every bean where it should be injected


## Annotations based Dependency injection

- [X] Processing package classes
- [X] create objects , add them to the app context
- [X] do field based injection
- [ ] do constructor based inj
- [ ] do setters based injection
- [ ] validations on names and types of context objects, (Check to dos in code)

- [ ] Refactoring
- [ ] Jar file exportation 