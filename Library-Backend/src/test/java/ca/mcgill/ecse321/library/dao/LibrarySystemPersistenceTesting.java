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
    public void testPersistAndLoadLibrarySystem() {
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

        Event event = new Event();
        String name = "PARTY";
        event.setId(12345678);
        event.setName(name);
        event.setTimeSlot(timeSlot);
        eventRepository.save(event);
        Set<Event> events = new HashSet<>();
        events.add(event);
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

        User user = new User();
        user.setId(id++);
        user.setName("Micheal");
        user.setAddress("Montreal");
        OnlineAccount account = new OnlineAccount();
        account.setUser(user);
        account.setId(1111);
        account.setUsername("ECSE321");
        account.setPassword("qwerty");
        account.setEmail("qqmail");
        user.setOnlineAccount(account);
        userRepository.save(user);
        personRepository.save(user);
        onlineAccountRepository.save(account);
        Set<User> users = new HashSet<>();
        users.add(user);
        librarySystem.setUsers(users);
        Set<OnlineAccount> onlineAccounts = new HashSet<>();
        onlineAccounts.add(account);
        librarySystem.setOnlineAccounts(onlineAccounts);

        Item item = new Item();
        item.setId(id++);
        item.setIsInLibrary(false);
        item.setItemCategory(Item.ItemCategory.Book);
        item.setIsReserved(true);
        itemRepository.save(item);
        Set<Item> items = new HashSet<>();
        items.add(item);
        librarySystem.setItems(items);

        Set<Person> persons = new HashSet<>();
        persons.add(user);
        persons.add(librarian);
        librarySystem.setPersons(persons);

        EventRegistration eventRegistration = new EventRegistration();
        eventRegistration.setId(id++);
        eventRegistration.setEvent(event);
        eventRegistration.setPerson(user);
        eventRegistrationRepository.save(eventRegistration);
        Set<EventRegistration> eventRegistrations = new HashSet<>();
        eventRegistrations.add(eventRegistration);
        librarySystem.setEventRegistrations(eventRegistrations);

        ItemReservation itemReservation = new ItemReservation();
        itemReservation.setId(++id);
        itemReservation.setItem(item);
        itemReservation.setPerson(user);
        itemReservation.setTimeSlot(timeSlot);
        itemReservationRepository.save(itemReservation);
        Set<ItemReservation> itemReservations = new HashSet<>();
        itemReservations.add(itemReservation);
        librarySystem.setItemReservations(itemReservations);

        librarySystemRepository.save(librarySystem);

        LibrarySystem librarySystem1 = librarySystemRepository.findLibrarySystemById(librarySystem.getId());
        assertNotNull(librarySystem1);

        Set<Event> eventSet = librarySystem1.getEvents();
        assertNotNull(eventSet);
        boolean flag = false;
        for (Event event1 : eventSet) {
            if (event1.getId() == event.getId() && event1.getName().equals(event.getName())) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);

        Set<User> userSet = librarySystem1.getUsers();
        assertNotNull(userSet);
        flag = false;
        for (User user1 : userSet) {
            if (user1.getId() == user.getId() && user1.getName().equals(user.getName())) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);

        Set<OnlineAccount> onlineAccountSet = librarySystem1.getOnlineAccounts();
        assertNotNull(onlineAccountSet);
        flag = false;
        for (OnlineAccount account1 : onlineAccountSet) {
            if (account1.getId() == account.getId() && account1.getUsername().equals(account.getUsername())) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);

        Set<ItemReservation> itemReservationSet = librarySystem1.getItemReservations();
        assertNotNull(itemReservationSet);
        flag = false;
        for (ItemReservation itemReservation1 : itemReservationSet) {
            if (itemReservation1.getId() == itemReservation.getId() && itemReservation1.getItem().getId() == (itemReservation.getItem().getId())) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);

    }
}
