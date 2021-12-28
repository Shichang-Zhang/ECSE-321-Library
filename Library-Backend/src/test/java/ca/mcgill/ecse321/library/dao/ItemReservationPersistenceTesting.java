package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.*;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * test the persistence of item reservation model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemReservationPersistenceTesting {
    @Autowired
    private ItemReservationRepository itemReservationRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @AfterEach
    public void clearDatabase() {

        itemReservationRepository.deleteAll();

        timeSlotRepository.deleteAll();

        personRepository.deleteAll();

        itemRepository.deleteAll();

    }

    /**
     * create a item reservation instance, store it into the database, and check whether
     * the item reservation instance has same attributes comparing to the item reservation
     * model in the database
     */
    @Test
    public void testItemReservation() {
        int id = 1024;
        ItemReservation itemReservation = new ItemReservation();
        itemReservation.setId(id);

        //Set person who borrows the book
        Person p = new User();
        p.setName("Joe");
        p.setId(1234);
        p.setAddress("luna");
        itemReservation.setPerson(p);
        personRepository.save(p);


        //Set item
        Item i = new Item();
        i.setItemCategory(Item.ItemCategory.Book);
        i.setIsReserved(true);
        i.setIsInLibrary(false);
        i.setId(1111);
        itemReservation.setItem(i);
        itemRepository.save(i);

        //Set the borrow timeslot
        TimeSlot timeSlot = new TimeSlot();
        Date date = Date.valueOf(LocalDate.of(2020, Month.JANUARY, 31));
        Time startTime = Time.valueOf(LocalTime.of(11, 35));
        Time endTime = Time.valueOf(LocalTime.of(13, 25));
        timeSlot.setId(1);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setStartDate(date);
        timeSlot.setEndDate(date);
        itemReservation.setTimeSlot(timeSlot);
        timeSlotRepository.save(timeSlot);

        itemReservationRepository.save(itemReservation);

        //get from the database
        ItemReservation itemReservation1 = itemReservationRepository.findItemReservationById(id);

        assertNotNull(itemReservation1);
        assertEquals(1024, itemReservation1.getId());

        Person person1 = itemReservation1.getPerson();
        Item item1 = itemReservation1.getItem();
        TimeSlot timeSlot1 = itemReservation1.getTimeSlot();
        assertNotNull(person1);
        assertNotNull(item1);
        assertNotNull(timeSlot1);

        assertEquals("Joe", person1.getName());
        assertEquals("luna", person1.getAddress());
        assertEquals(Item.ItemCategory.Book, item1.getItemCategory());
        assertTrue(item1.getIsReserved());
        assertEquals(date, timeSlot1.getEndDate());
        assertEquals(startTime, timeSlot1.getStartTime());
    }
}
