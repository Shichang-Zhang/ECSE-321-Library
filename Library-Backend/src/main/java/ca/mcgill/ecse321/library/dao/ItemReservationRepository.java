package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * item reservation repository.
 * containing methods that can interact with the itemReservation table in the database.
 * findByPersonAndItem method will return a list of item reservation objects that recorded the interaction between the input person and item.
 * findItemReservationById method returns an item reservation object with the input id.
 * findByPerson method returns a list of item reservation that was done by the input person.
 * findByItem method returns a list of item reservation that was related to the input item.
 * deleteItemReservationsByItem method will delete the records of the item reservation that were related to the input item.
 */
@RepositoryRestResource(collectionResourceRel = "itemReservation_data", path = "itemReservation_data")
public interface ItemReservationRepository extends CrudRepository<ItemReservation, Integer> {
    List<ItemReservation> findByPersonAndItem(Person p, Item i);

    ItemReservation findItemReservationById(int id);

    ItemReservation findItemReservationByPersonAndItemAndTimeSlot(Person person, Item item, TimeSlot timeSlot);

    List<ItemReservation> findByPerson(Person p);

    List<ItemReservation> findByItem(Item i);

    List<ItemReservation> deleteItemReservationsByItem(Item i);
}
