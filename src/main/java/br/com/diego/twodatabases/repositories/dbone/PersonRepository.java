package br.com.diego.twodatabases.repositories.dbone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.diego.twodatabases.model.dbone.Person;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Integer>{

}
