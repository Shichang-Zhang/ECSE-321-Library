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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * this class test the library system model persistence
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LibrarySystemPersistenceTesting {

    @Autowired
    private BusinessHourRepository businessHourRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private LibrarySystemRepository librarySystemRepository;
    @Autowired
    private OnlineAccountRepository onlineAccountRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemReservationRepository itemReservationRepository;
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @AfterEach
    public void clearDatabase() {

        librarySystemRepository.deleteAll();

        itemReservationRepository.deleteAll();

        eventRegistrationRepository.deleteAll();

        eventRepository.deleteAll();

        timeSlotRepository.deleteAll();

        personRepository.deleteAll();

        userRepository.deleteAll();

        librarianRepository.deleteAll();

        itemRepository.deleteAll();

        businessHourRepository.deleteAll();

        onlineAccountRepository.deleteAll();

    }


    /**
     * test librarySystem persistence
     * -----------------------------------------------------------------------------------
     */
    @Test
    public void testPersistAndLoadLibrarySystem(){
        int id = 112;
        LibrarySystem librarySystem = new LibrarySystem();
        librarySystem.setId(id);

        BusinessHour businessHour = new BusinessHour();
        BusinessHour.DayOfWeek dayOfWeek = BusinessHour.DayOfWeek.Monday;
        Time startTime = Time.valueOf(LocalTime.of(11, 20));
        Time endTime = Time.valueOf(LocalTime.of(11, 35));
        businessHour.setId(id++);
        businessHour.setDayOfWeek(dayOfWeek);
        businessHour.setEndTime(endTime);
        businessHour.setStartTime(startTime);
        businessHourRepository.save(businessHour);
        Set<BusinessHour> businessHours = new HashSet<>();
        businessHours.add(businessHour);
        librarySystem.setBusinessHours(businessHours);

        TimeSlot timeSlot = new TimeSlot();
        Date startDate = Date.valueOf(LocalDate.of(2020, Month.JANUARY, 31));
        Date endDate = Date.valueOf(LocalDate.of(2020, Month.FEBRUARY, 28));
        timeSlot.setId(123456);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setStartDate(startDate);
        timeSlot.setEndDate(endDate);
        timeSlotRepository.save(timeSlot);
        Set<TimeSlot> timeSlots = new HashSet<>();
        timeSlots.add(timeSlot);
        librarySystem.setTimeSlots(timeSlots);

        Event e = new Event();
        String name = "PARTY";
        e.setId(12345678);
        e.setName(name);
        e.setTimeSlot(timeSlot);
        eventRepository.save(e);
        Set<Event> events = new HashSet<>();
        events.add(e);
        librarySystem.setEvents(events);

        Librarian librarian = new Librarian();
        name = "someName";
        String address = "someAddress";
        librarian.setId(1234567);
        librarian.setIsHeadLibrarian(false);
        librarian.setName(name);
        librarian.setAddress(address);
        librarian.setBusinessHours(businessHours);
        librarianRepository.save(librarian);
        personRepository.save(librarian);
        Set<Librarian> librarians = new HashSet<>();
        librarians.add(librarian);
        librarySystem.setLibrarians(librarians);

        User u=new User();
        u.setId(id++);
        u.setName("Micheal");
        u.setAddress("Montreal");
        OnlineAccount account=new OnlineAccount();
        account.setUser(u);
        account.setId(1111);
        account.setUsername("ECSE321");
        account.setPassword("qwerty");
        account.setEmail("qqmail");
        u.setOnlineAccount(account);
        userRepository.save(u);
        personRepository.save(u);
        onlineAccountRepository.save(account);
        Set<User> users = new HashSet<>();
        users.add(u);
        librarySystem.setUsers(users);
        Set<OnlineAccount> onlineAccounts = new HashSet<>();
        onlineAccounts.add(account);
        librarySystem.setOnlineAccounts(onlineAccounts);

        Item i=new Item();
        i.setId(id++);
        i.setIsInLibrary(false);
        i.setItemCategory(Item.ItemCategory.Book);
        i.setIsReserved(true);
        itemRepository.save(i);
        Set<Item> items = new HashSet<>();
        items.add(i);
        librarySystem.setItems(items);

        Set<Person> persons = new HashSet<>();
        persons.add(u);
        persons.add(librarian);
        librarySystem.setPersons(persons);

        EventRegistration eventRegistration = new EventRegistration();
        eventRegistration.setId(id++);
        eventRegistration.setEvent(e);
        eventRegistration.setPerson(u);
        eventRegistrationRepository.save(eventRegistration);
        Set<EventRegistration> eventRegistrations = new HashSet<>();
        eventRegistrations.add(eventRegistration);
        librarySystem.setEventRegistrations(eventRegistrations);

        ItemReservation itemReservation  = new ItemReservation();
        itemReservation.setId(id++);
        itemReservation.setItem(i);
        itemReservation.setPerson(u);
        itemReservation.setTimeSlot(timeSlot);
        itemReservationRepository.save(itemReservation);
        Set<ItemReservation> itemReservations = new HashSet<>();
        itemReservations.add(itemReservation);
        librarySystem.setItemReservations(itemReservations);

        librarySystemRepository.save(librarySystem);

        LibrarySystem librarySystem1 = null;
        librarySystem1 = librarySystemRepository.findLibrarySystemById(librarySystem.getId());
        assertNotNull(librarySystem1);

        Set<Event> events1 = null;
        events1 = librarySystem1.getEvents();
        assertNotNull(events1);
        boolean flag = false;
        for(Event ee : events1){
            if(ee.getId()==e.getId() && ee.getName().equals(e.getName())) flag=true;
        }
        assertTrue(flag);

        Set<User> users1 = null;
        users1 = librarySystem1.getUsers();
        assertNotNull(users1);
        flag = false;
        for(User uu : users1){
            if(uu.getId()==u.getId() && uu.getName().equals(u.getName())) flag=true;
        }
        assertTrue(flag);

        Set<OnlineAccount> onlineAccounts1 = null;
        onlineAccounts1 = librarySystem1.getOnlineAccounts();
        assertNotNull(onlineAccounts1);
        flag = false;
        for(OnlineAccount account11 : onlineAccounts1){
            if(account11.getId()==account.getId() && account11.getUsername().equals(account.getUsername())) flag=true;
        }
        assertTrue(flag);

        Set<ItemReservation> itemReservations1 = null;
        itemReservations1 = librarySystem1.getItemReservations();
        assertNotNull(itemReservations1);
        flag = false;
        for(ItemReservation iii : itemReservations1){
            if(iii.getId()==itemReservation.getId() && iii.getItem().getId()==(itemReservation.getItem().getId())) flag=true;
        }
        assertTrue(flag);

    }
}
