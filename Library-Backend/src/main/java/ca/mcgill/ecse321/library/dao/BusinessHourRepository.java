package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.BusinessHour;
import ca.mcgill.ecse321.library.model.BusinessHour.DayOfWeek;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Business repository.
 * containing methods that can interact with the businessHour table in the database.
 * findBusinessHourById methods can return a business object when the corresponding id is passed.
 * findBusinessHourByDayOfWeek can return a business object when the corresponding dayOfWeek (ex. Monday) is passed.
 */
@RepositoryRestResource(collectionResourceRel = "businesshour_data",path = "businesshour_data")
public interface BusinessHourRepository extends CrudRepository<BusinessHour,Integer> {
    BusinessHour findBusinessHourById(int id);
}