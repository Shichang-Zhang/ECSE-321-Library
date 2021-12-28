package ca.mcgill.ecse321.library.dao;


import ca.mcgill.ecse321.library.model.TimeSlot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * timeslot repository.
 * containing methods that can interact with the timeSlot table in the database.
 * findTimeSlotById method can return a time slot object that has the input id
 */
@RepositoryRestResource(collectionResourceRel = "timeSlot_data", path = "timeSlot_data")
public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer> {
    TimeSlot findTimeSlotById(int id);
}
