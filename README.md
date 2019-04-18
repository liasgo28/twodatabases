# SpringBoot With Two DataBases
Create SpringBoot Application connected in two databases

## Let's start:

Enjoy this!

### Create a new Maven Project

	* Click: File -> New -> MavenProject

	* Check: Create a simple project(skp archtype selection)

	* Click Next

	* Type Group id, Artifact Id and click Finish

### POM Dependencies

Open pom.xml file and add the next code after the </project> tag

```
<dependencies>
  	<dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-web</artifactId>
	    <version>2.1.3.RELEASE</version>
	</dependency>
	<dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-data-jpa</artifactId>
	    <version>2.1.3.RELEASE</version>
	</dependency>
	<dependency>
	    <groupId>mysql</groupId>
	    <artifactId>mysql-connector-java</artifactId>
	    <version>5.1.6</version>
	</dependency>
   <dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-test</artifactId>
	    <version>2.1.3.RELEASE</version>
	    <scope>test</scope>
	</dependency>
		
</dependencies>
```

### Create start class
* create new class file 
* add the annotation @SpringBootApplication before the class
* create a new main method and push this SpringApplication.run(Application.class, args);
* the final code will look like this

```
package br.com.diego.twodatabases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

Run main method and see in console application was been started!

...
  o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
...

### Create Models
We will need two models in separates packages this is necessary because 
in the configuration database classes]

```
package br.com.diego.twodatabases.model.dbone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String email;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
	public Person(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}
	public Person() {
		super();
	}
}


package br.com.diego.twodatabases.model.dbtwo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Animal {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Animal [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
	
	
}
```

### Create Repositories

```
package br.com.diego.twodatabases.repositories.dbone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.diego.twodatabases.model.dbone.Person;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Integer>{

}


package br.com.diego.twodatabases.repositories.dbtwo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.diego.twodatabases.model.dbtwo.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

}

```

### Create a Application properties
In src/main/resources create a new file application.properties and insert yours configurations like this:

```

spring.jpa.database=default
spring.jpa.generate-ddl=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect

dbone.datasource.jdbcUrl=jdbc:mysql://localhost:3306/dbone
dbone.datasource.username=yourusername
dbone.datasource.password=yourpassoword
dbone.datasource.driverClassName=com.mysql.jdbc.Driver


#second dbtwo ...
dbtwo.datasource.jdbcUrl=jdbc:mysql://localhost:3306/dbtwo
dbone.datasource.username=yourusernamedbtwo
dbone.datasource.password=yourpassoworddbtwo
dbtwo.datasource.driverClassName=com.mysql.jdbc.Driver

```

### Let's make the magic
To connect in two databases we need create two connections like this:

* The principal difference between this class is the @Primary annotation this annotation is
necessary to indicate that a bean should be given preference when multiple candidates are qualified to autowire a single valued dependency

```
package br.com.diego.twodatabases.configurations;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	 entityManagerFactoryRef = "dbOneEntityManagerFactory",
	 transactionManagerRef = "dbOneTransactionManager",
	 basePackages = {
	  "br.com.diego.twodatabases.repositories.dbone"
	 }
)
public class DbOneConfiguration {
	@Primary
	@Bean(name = "dbOneDataSource")
	@ConfigurationProperties(prefix = "dbone.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name = "dbOneEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean dbOneEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dbOneDataSource") DataSource dataSource) {
		return builder
		   .dataSource(dataSource)
		   .packages("br.com.diego.twodatabases.model.dbone")
		   .persistenceUnit("dbone")
		   .build();
	}

	@Primary
	@Bean(name = "dbOneTransactionManager")
	public PlatformTransactionManager dbOneTransactionManager(@Qualifier("dbOneEntityManagerFactory") EntityManagerFactory dbOneEntityManagerFactory) {
		return new JpaTransactionManager(dbOneEntityManagerFactory);
	}
	
}

package br.com.diego.twodatabases.configurations;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	 entityManagerFactoryRef = "dbTwoEntityManagerFactory",
	 transactionManagerRef = "dbTwoTransactionManager",
	 basePackages = {
	  "br.com.diego.twodatabases.repositories.dbtwo"
	 }
)
public class DbTwoConfiguration {

	@Bean(name = "dbTwoDataSource")
	@ConfigurationProperties(prefix = "dbtwo.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "dbTwoEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean dbTwoEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dbTwoDataSource") DataSource dataSource) {
		return builder
		   .dataSource(dataSource)
		   .packages("br.com.diego.twodatabases.model.dbtwo")
		   .persistenceUnit("dbtwo")
		   .build();
	}

	@Bean(name = "dbTwoTransactionManager")
	public PlatformTransactionManager dbTwoTransactionManager(@Qualifier("dbTwoEntityManagerFactory") EntityManagerFactory dbTwoEntityManagerFactory) {
		return new JpaTransactionManager(dbTwoEntityManagerFactory);
	}
	
}

 
```


### Test
To test this you could use this codes:

```
package br.com.diego.twodatabases.repositories.dbone;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.diego.twodatabases.model.dbone.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestDBOne {
	@Autowired
    private PersonRepository personRepository;
    
    @Test
    public void create_check_product() {
    	Person aaa = new Person("AAA", "bbb");
    	personRepository.save(aaa);
    	
    	List<Person> persons = personRepository.findAll();
    	assertNotNull(persons);
    	System.out.println("---");
    	System.out.println(persons.size());
    	System.out.println("---");
    }
}


package br.com.diego.twodatabases.repositories.dbtwo;


import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.diego.twodatabases.model.dbtwo.Animal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestDBTwo {
	@Autowired
    private AnimalRepository animalRepository;
    
    @Test
    @Transactional("dbTwoTransactionManager")
    public void create_check_product() {
    	List<Animal> animals = animalRepository.findAll();
    	assertNotNull(animals);
    	System.out.println("---");
    	System.out.println(animals.size());
    	System.out.println("---");
    	/*
        ProductModel product = new ProductModel("228781","Running Shoes", 20.0);
        product = productRepository.save(product);

        assertNotNull(productRepository.findById(product.getId()));*/
    }
}

```

	
# YWC -> You are Welcome!
