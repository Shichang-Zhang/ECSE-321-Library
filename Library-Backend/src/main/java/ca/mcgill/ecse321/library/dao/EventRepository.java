package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * event repository.
 * containing methods that can interact with the event table in the database.
 * findEventById method can find the event when the event id is passed.
 * findByName method can return a list of events that have the input name.
 */
@RepositoryRestResource(collectionResourceRel = "event_data", path = "event_data")
public interface EventRepository extends CrudRepository<Event, Integer> {
    Event findEventById(int id);

    List<Event> findByName(String name);
}
