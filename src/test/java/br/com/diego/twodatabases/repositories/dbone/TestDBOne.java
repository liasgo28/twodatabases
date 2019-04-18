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
