package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Event;
import ca.mcgill.ecse321.library.model.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * test the persistence of event model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventPersistenceTesting {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @AfterEach
    public void clearDatabase() {

        eventRepository.deleteAll();

        timeSlotRepository.deleteAll();

    }


    /**
     * create an event instance, store it into the database, and check whether
     * the event instance has same attributes comparing to the event
     * model in the database
     */
    @Test
    public void testPersistAndLoadEvent(){
        Event e = new Event();
        String name = "PARTY";
        e.setId(123444);
        e.setName(name);

        TimeSlot timeSlot = new TimeSlot();
        Date startDate = Date.valueOf(LocalDate.of(2020, Month.JANUARY, 31));
        Date endDate = Date.valueOf(LocalDate.of(2020, Month.FEBRUARY, 28));
        Time startTime = Time.valueOf(LocalTime.of(11, 35));
        Time endTime = Time.valueOf(LocalTime.of(13, 25));
        timeSlot.setId(1212322);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setStartDate(startDate);
        timeSlot.setEndDate(endDate);
        e.setTimeSlot(timeSlot);
        timeSlotRepository.save(timeSlot);

        eventRepository.save(e);

        Event eInDB = null;

        eInDB = eventRepository.findEventById(e.getId());

        assertNotNull(eInDB);
        assertEquals(eInDB.getId(),e.getId());
        assertEquals(eInDB.getName(), e.getName());

        assertNotNull(eInDB.getTimeSlot());
        assertEquals(eInDB.getTimeSlot().getId(), e.getTimeSlot().getId());
        assertEquals(eInDB.getTimeSlot().getStartTime(), e.getTimeSlot().getStartTime());
    }
}
