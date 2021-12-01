package ca.mcgill.ecse321.library.dao;

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
 * test the persistence of timeslot model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TimeSlotPersistenceTesting {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @AfterEach
    public void clearDatabase() {
        timeSlotRepository.deleteAll();
    }

    /**
     * create a timeSlot instance, store it into the database, and check whether
     * the timeSlot instance has same attributes comparing to the timeSlot
     * model in the database
     */
    @Test
    public void testPersistAndLoadTimeSlot() {
        TimeSlot timeSlot = new TimeSlot();
        Date date = java.sql.Date.valueOf(LocalDate.of(2020, Month.JANUARY, 31));
        Time startTime = java.sql.Time.valueOf(LocalTime.of(11, 35));
        Time endTime = java.sql.Time.valueOf(LocalTime.of(13, 25));
        timeSlot.setId(1);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setStartDate(date);
        timeSlot.setEndDate(date);
        timeSlotRepository.save(timeSlot);
        TimeSlot timeSlot1 = null;
        timeSlot1 = timeSlotRepository.findTimeSlotById(1);
        assertNotNull(timeSlot1);
        assertEquals(date,timeSlot1.getEndDate());
        assertEquals(startTime,timeSlot1.getStartTime());
    }
}
