package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * person repository.
 * containing methods that can interact with the person table in the database.
 * findPersonById method will return the person object with the given id.
 */
@RepositoryRestResource(collectionResourceRel = "person_data", path = "person_data")
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findPersonById(int id);
}
