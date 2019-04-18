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
