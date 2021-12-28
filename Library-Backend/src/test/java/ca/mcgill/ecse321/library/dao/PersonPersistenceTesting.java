package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Librarian;
import ca.mcgill.ecse321.library.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test the persistence of person model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PersonPersistenceTesting {
    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void clearDatabase() {
        personRepository.deleteAll();
    }

    /**
     * create a person instance, store it into the database, and check whether
     * the person instance has same attributes comparing to the person
     * model in the database
     */
    @Test
    public void testPersistAndLoadPerson(){
        Person person=new Librarian();
        person.setName("Joe");
        person.setId(1234);
        person.setAddress("luna");
        personRepository.save(person);

        person=personRepository.findPersonById(1234);
        assertNotNull(person);
        assertEquals("Joe",person.getName());
        assertEquals(person.getAddress(),"luna");
    }

}
