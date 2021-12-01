package ca.mcgill.ecse321.library.dao;


import ca.mcgill.ecse321.library.model.BusinessHour;
import ca.mcgill.ecse321.library.model.Librarian;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test the persistence of librarian model
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LibrarianPersistenceTesting {
    @Autowired
    BusinessHourRepository businessHourRepository;
    @Autowired
    LibrarianRepository librarianRepository;

    @AfterEach
    public void clearDatabase() {
        librarianRepository.deleteAll();

        businessHourRepository.deleteAll();

    }


    /**
     * create a librarian instance, store it into the database, and check whether
     * the librarian instance has same attributes comparing to the librarian
     * model in the database
     */
    @Test
    public void testAndPersistLibrarian() {
        Librarian librarian = new Librarian();
        String name = "someName";
        String address = "someAddress";
        librarian.setId(123444);
        librarian.setIsHeadLibrarian(false);
        librarian.setName(name);
        librarian.setAddress(address);

        BusinessHour bh = new BusinessHour();
        int id = 12223;
        BusinessHour.DayOfWeek dayOfWeek = BusinessHour.DayOfWeek.Monday;
        Time startTime = Time.valueOf(LocalTime.of(11, 20));
        Time endTime = Time.valueOf(LocalTime.of(11, 35));
        bh.setId(id);
        bh.setDayOfWeek(dayOfWeek);
        bh.setEndTime(endTime);
        bh.setStartTime(startTime);
        businessHourRepository.save(bh);

        Set<BusinessHour> bs = new HashSet<BusinessHour>();
        bs.add(bh);
        librarian.setBusinessHours(bs);

        librarianRepository.save(librarian);

        Librarian librarianInDB = librarianRepository.findLibrarianById(librarian.getId());

        assertNotNull(librarianInDB);
        assertEquals(librarianInDB.getIsHeadLibrarian(), false);
        assertEquals(librarianInDB.getName(), librarian.getName());
        assertEquals(librarianInDB.getAddress(), librarian.getAddress());
        assertNotNull(librarianInDB.getBusinessHours());
        boolean flag = false;
        for(BusinessHour b : librarianInDB.getBusinessHours()){
            if (b.getId()==bh.getId()) flag=true;
        }
        assertTrue(flag);
    }
}
