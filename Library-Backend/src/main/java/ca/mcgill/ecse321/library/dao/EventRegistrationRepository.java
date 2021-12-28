package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Event;
import ca.mcgill.ecse321.library.model.EventRegistration;
import ca.mcgill.ecse321.library.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * event registration repository.
 * containing methods that can interact with the eventRegistration table in the database.
 * findByPersonAndEvent method will return an event registration object when person and event are the inputs.
 * findByPerson method will return a list of event registration that the input person has registered.
 * findByEvent method will return a list of people that have registered the input event.
 * existsByPersonAndEvent will return whether the person has registered the event.
 * deleteEventRegistrationsByEvent will delete all event registration records that related to the event.
 */
@RepositoryRestResource(collectionResourceRel = "eventRegistration_data", path = "eventRegistration_data")
public interface EventRegistrationRepository extends CrudRepository<EventRegistration, Integer> {
    EventRegistration findByPersonAndEvent(Person person, Event event);

    List<EventRegistration> findByPerson(Person p);

    List<EventRegistration> findByEvent(Event e);

    boolean existsByPersonAndEvent(Person person, Event event);

    void deleteEventRegistrationsByEvent(Event event);
}
