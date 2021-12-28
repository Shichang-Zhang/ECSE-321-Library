package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Event;
import ca.mcgill.ecse321.library.model.EventRegistration;
import ca.mcgill.ecse321.library.model.Person;
import ca.mcgill.ecse321.library.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * test the persistence of event registration model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventRegistrationPersistenceTesting {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventRegistrationRepository eventRegistrationRepository;

    @AfterEach
    public void clearDatabase() {

        eventRegistrationRepository.deleteAll();

        eventRepository.deleteAll();

        personRepository.deleteAll();

    }

    /**
     * create a event registration instance, store it into the database, and check whether
     * the event registration instance has same attributes comparing to the event registration
     * model in the database
     */
    @Test
    public void testPersistAndLoadEventRegistration(){
        //Create Person
        Person person=new User();
        person.setName("Joe");
        person.setId(4343);
        person.setAddress("luna");
        personRepository.save(person);
        //Create Event
        Event event=new Event();
        event.setId(2222);
        event.setName("Party");
        eventRepository.save(event);
        //Create
        EventRegistration eventRegistration=new EventRegistration();
        eventRegistration.setId(3333);
        eventRegistration.setPerson(person);
        eventRegistration.setEvent(event);
        eventRegistrationRepository.save(eventRegistration);
        EventRegistration eventRegistrationInDataBase=eventRegistrationRepository.findByPersonAndEvent(person,event);
        assertEquals(3333,eventRegistrationInDataBase.getId());
    }

}
