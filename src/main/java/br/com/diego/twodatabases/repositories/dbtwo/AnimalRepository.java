package br.com.diego.twodatabases.repositories.dbtwo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.diego.twodatabases.model.dbtwo.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

}
