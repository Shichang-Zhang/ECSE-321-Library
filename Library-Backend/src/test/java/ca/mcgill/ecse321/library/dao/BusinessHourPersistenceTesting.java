package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.BusinessHour;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test the persistence of business hour model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BusinessHourPersistenceTesting {
    @Autowired
    BusinessHourRepository businessHourRepository;

    @AfterEach
    public void clearDatabase() {
        businessHourRepository.deleteAll();
    }

    /**
     * create a business hour instance, store it into the database, and check whether
     * the business hour instance has same attributes comparing to the business hour
     * model in the database
     */
    @Test
    public void testPersistAndLoadBusinessHour() {
        BusinessHour businessHour = new BusinessHour();
        int id = 1111;
        BusinessHour.DayOfWeek dayOfWeek = BusinessHour.DayOfWeek.Monday;
        Time startTime = Time.valueOf(LocalTime.of(11, 20));
        Time endTime = Time.valueOf(LocalTime.of(11, 35));
        businessHour.setId(id);
        businessHour.setDayOfWeek(dayOfWeek);
        businessHour.setEndTime(endTime);
        businessHour.setStartTime(startTime);

        businessHourRepository.save(businessHour);

        BusinessHour businessHourInDateBase = businessHourRepository.findBusinessHourById(businessHour.getId());

        assertNotNull(businessHourInDateBase);
        assertEquals(businessHourInDateBase.getDayOfWeek(), businessHour.getDayOfWeek());
        assertEquals(businessHourInDateBase.getEndTime(), endTime);
        assertEquals(businessHourInDateBase.getStartTime(), startTime);
    }
}
