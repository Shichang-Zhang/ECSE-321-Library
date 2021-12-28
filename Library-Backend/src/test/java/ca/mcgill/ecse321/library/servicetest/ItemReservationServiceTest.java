package ca.mcgill.ecse321.library.servicetest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.model.Item.ItemCategory;
import ca.mcgill.ecse321.library.service.*;
import ca.mcgill.ecse321.library.dao.*;

/**
 * unit test of the item reservation services
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class ItemReservationServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemReservationRepository itemReservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LibrarySystemRepository librarySystemRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @InjectMocks
    private ItemReservationService itemReservationService;
    @InjectMocks
    private ItemService itemService;
    @InjectMocks
    private PersonService personService;
    private static final int personId = 11;
    private static final int ItemId = 12;


    private static final int checkedOutItemId = 1234;
    private static final int newspaperId = 123145;
    private static final int archiveId = 12885672;
    private static final int reservedItemId = 1892794567;
    private static final int reservationId = 111;
    private static final int testRenewNotCheckedOutItemId = 1837651;
    private static final Time startTime = Time.valueOf(LocalTime.parse("09:00"));
    private static final Date startDate = Date.valueOf(LocalDate.parse("2021-12-09"));
    private static final Time endTime = Time.valueOf(LocalTime.parse("09:00"));
    private static final Date endDate = Date.valueOf(LocalDate.parse("2021-12-22"));

    @BeforeEach
    public void setMockOutput() {
        lenient().when(itemRepository.findItemById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ItemId)) {
                Item item = new Item();
                item.setId(ItemId);
                item.setIsInLibrary(true);
                item.setIsReserved(false);
                item.setItemCategory(ItemCategory.Book);
                return item;
            } else if (invocation.getArgument(0).equals(checkedOutItemId)) {
                Item item = new Item();
                item.setId(checkedOutItemId);
                item.setIsInLibrary(false);
                item.setIsReserved(false);
                item.setItemCategory(ItemCategory.Book);
                return item;
            } else if (invocation.getArgument(0).equals(newspaperId)) {
                Item item = new Item();
                item.setId(newspaperId);
                item.setIsInLibrary(true);
                item.setIsReserved(false);
                item.setItemCategory(ItemCategory.Newspaper);
                return item;
            } else if (invocation.getArgument(0).equals(archiveId)) {
                Item item = new Item();
                item.setId(archiveId);
                item.setIsInLibrary(true);
                item.setIsReserved(false);
                item.setItemCategory(ItemCategory.Archive);
                return item;
            } else if (invocation.getArgument(0).equals(reservedItemId)) {
                Item item = new Item();
                item.setId(reservedItemId);
                item.setIsInLibrary(true);
                item.setIsReserved(true);
                item.setItemCategory(ItemCategory.Book);
                return item;
            } else {
                return null;
            }
        });

        lenient().when(itemReservationRepository.findItemReservationById(anyInt()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(reservationId)) {
                        Item item = itemRepository.findItemById(checkedOutItemId);
                        item.setIsInLibrary(false);
                        Person person = personRepository.findPersonById(personId);
                        ItemReservation itemReservation = new ItemReservation();
                        itemReservation.setId(reservationId);
                        itemReservation.setItem(item);
                        itemReservation.setPerson(person);
                        TimeSlot timeslot = new TimeSlot();
                        timeslot.setStartDate(startDate);
                        timeslot.setEndDate(endDate);
                        timeslot.setStartTime(startTime);
                        timeslot.setEndTime(endTime);
                        itemReservation.setTimeSlot(timeslot);
                        return itemReservation;
                    } else if (invocation.getArgument(0).equals(testRenewNotCheckedOutItemId)) {
                        Item item = itemRepository.findItemById(ItemId);
                        item.setIsInLibrary(true);
                        Person person = personRepository.findPersonById(personId);
                        ItemReservation itemReservation = new ItemReservation();
                        itemReservation.setId(testRenewNotCheckedOutItemId);
                        itemReservation.setItem(item);
                        itemReservation.setPerson(person);
                        TimeSlot timeslot = new TimeSlot();
                        timeslot.setStartDate(startDate);
                        timeslot.setEndDate(endDate);
                        timeslot.setStartTime(startTime);
                        timeslot.setEndTime(endTime);
                        itemReservation.setTimeSlot(timeslot);
                        return itemReservation;
                    } else {
                        return null;
                    }
                });

        lenient().when(personRepository.findPersonById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(personId)) {
                User person = new User();
                person.setId(personId);
                person.setAddress("1200 This street");
                person.setName("The name");
                person.setIsLocal(true);
                return person;
            } else {
                return null;
            }
        });


        lenient().when(librarySystemRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<LibrarySystem> librarySystemList = new ArrayList<>();
            LibrarySystem librarySystem = new LibrarySystem();
            librarySystemList.add(librarySystem);
            return librarySystemList;
        });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(itemRepository.save(any(Item.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(personRepository.save(any(Person.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(itemReservationRepository.save(any(ItemReservation.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(userRepository.save(any(User.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * The method tests the valid output of the checkOutItem.
     */
    @Test
    public void testCheckOut() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);

        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(itemReservation);
        assertEquals(item.getId(), itemReservation.getItem().getId());
        assertEquals(item.getName(), itemReservation.getItem().getName());
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals(person.getId(), itemReservation.getPerson().getId());
        assertEquals(person.getName(), itemReservation.getPerson().getName());
        assertEquals(startTime, itemReservation.getTimeSlot().getStartTime());
        assertEquals(startDate, itemReservation.getTimeSlot().getStartDate());

    }

    /**
     * The method test the output of checkOutItem when the input person id belongs to person that does not exist in the database.
     */
    @Test
    public void testNonExistingPerson() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        int invalid = 124142124;
        String error = null;
        try {
            itemReservation = itemReservationService.checkOutItem(invalid, item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Person does not exist!", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to check out newspaper.
     */
    @Test
    public void testInvalidItemTypeNewspaper() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(newspaperId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Newspaper cannot be checked out!", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to checkout archives.
     */
    @Test
    public void testInvalidItemTypeArchive() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(archiveId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Archive cannot be checked out!", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to checkout an item that has been checked out.
     */
    @Test
    public void testCheckOut_CheckedOutItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Item is not in library!", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to checkout an item with a starttime in the past.
     */
    @Test
    public void testCheckOutInPastStartTime() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        Date startDate = Date.valueOf(LocalDate.parse("2021-10-09"));
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("can not register an item reservation with past time", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to checkout an it that has been reserved.
     */
    @Test
    public void testCheckOutReservedItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        String error = null;
        Item item = itemService.createItem("Book", "book");
        item.setId(reservedItemId);
        item.setIsInLibrary(true);
        item.setIsReserved(true);
        Person person = personRepository.findPersonById(personId);
        List<ItemReservation> reservationList = new ArrayList<>();
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation existingItemReservation =
                itemReservationService.reserveItem(personId, reservedItemId, startDate, endDate, startTime, endTime);
        reservationList.add(existingItemReservation);
        lenient().when(itemRepository.findItemById(item.getId())).thenReturn(item);
        lenient().when(itemReservationRepository.findByItem(itemRepository.findItemById(item.getId())))
                .thenReturn(reservationList);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), item.getId(), startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("item is reserved in this period", error);
    }

    /**
     * The method tests the output of the checkOutItem method when a person wants to checkout an item with an id that does not exist.
     */
    @Test
    public void testInvalidItemId() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        int invalidItemId = 1241234124;
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation = itemReservationService.checkOutItem(person.getId(), invalidItemId, startDate, startTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("item does not exist", error);
    }

    /**
     * The method tests the output when a perso wants to reserve an item.
     */
    @Test
    public void testReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);

        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        Calendar c = Calendar.getInstance();
        c.set(2021, Calendar.SEPTEMBER, 9, 8, 0, 0);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(itemReservation);
        assertEquals(item.getId(), itemReservation.getItem().getId());
        assertEquals(item.getName(), itemReservation.getItem().getName());
        assertTrue(itemReservation.getItem().getIsReserved());
        assertEquals(person.getId(), itemReservation.getPerson().getId());
        assertEquals(person.getName(), itemReservation.getPerson().getName());
        assertEquals(startTime, itemReservation.getTimeSlot().getStartTime());
        assertEquals(startDate, itemReservation.getTimeSlot().getStartDate());
        assertEquals(endTime, itemReservation.getTimeSlot().getEndTime());
        assertEquals(endDate, itemReservation.getTimeSlot().getEndDate());

    }

    /**
     * The method tests the output of reserveItem when the input person id does not exist.
     */
    @Test
    public void testReserveNonExistingPerson() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        int invalid = 124142124;
        String error = null;
        try {
            itemReservation =
                    itemReservationService.reserveItem(invalid, item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Person does not exist!", error);
    }

    /**
     * The method tests the output of reserveItem when the input item id does not exist.
     */
    @Test
    public void testReserveInvalidItemId() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        int invalidItemId = 1241234124;
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), invalidItemId, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("item does not exist", error);
    }

    /**
     * The method tests the output of reserveItem when the type of the item is newspaper.
     */
    @Test
    public void testReserveInvalidItemTypeNewspaper() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(newspaperId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Newspaper cannot be reserved to checkout!", error);
    }

    /**
     * The method tests the output of reserveItem when the type of the item is archive.
     */
    @Test
    public void testReserveInvalidItemTypeArchive() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(archiveId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Archive cannot be reserved to checkout!", error);
    }

    /**
     * The method tests the output of reserveItem when the input endDate is before the input startDate.
     */
    @Test
    public void testReserveEndDateBeforeStartDate() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        Date endDate = Date.valueOf(LocalDate.parse("2021-12-01"));
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Timeslot end date cannot be before reservation start date!", error);
    }

    /**
     * The method tests the output of reserveItem when the input endTime is before the input startTime.
     */
    @Test
    public void testReserveStartTimeBeforeEndTime() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        Time endTime = Time.valueOf(LocalTime.parse("08:00"));
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Timeslot end time cannot be before reservation start time!", error);
    }

    /**
     * The method tests the output of reserveItem when the input startDate is in the past.
     */
    @Test
    public void testReservePastTime() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        Date startDate = Date.valueOf(LocalDate.parse("2021-01-01"));
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        try {
            itemReservation =
                    itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("can not register an item reservation with past time", error);
    }

    /**
     * The method tests the output of reserveItem when the item is reserved during the input period.
     */
    @Test
    public void testReserveReservedItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemService.createItem("Book", "book");
        item.setId(reservedItemId);
        item.setIsInLibrary(true);
        item.setIsReserved(true);
        Person person = personRepository.findPersonById(personId);
        List<ItemReservation> reservationList = new ArrayList<>();
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation existingItemReservation =
                itemReservationService.reserveItem(personId, reservedItemId, startDate, endDate, startTime, endTime);
        reservationList.add(existingItemReservation);
        lenient().when(itemRepository.findItemById(item.getId())).thenReturn(item);
        lenient().when(itemReservationRepository.findByItem(itemRepository.findItemById(item.getId())))
                .thenReturn(reservationList);
        try {
            itemReservation = itemReservationService.reserveItem(person.getId(), item.getId(), startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assertNull(itemReservation);
    }

    /**
     * The method tests the renewal of checked out items.
     */
    @Test
    public void testRenew() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);

        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation =
                    itemReservationService.renewItem(person.getId(), item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(itemReservation);
        assertEquals(item.getId(), itemReservation.getItem().getId());
        assertEquals(item.getName(), itemReservation.getItem().getName());
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals(person.getId(), itemReservation.getPerson().getId());
        assertEquals(person.getName(), itemReservation.getPerson().getName());
        assertEquals(endTime, itemReservation.getTimeSlot().getEndTime());
        assertEquals(newEndDate, itemReservation.getTimeSlot().getEndDate());

    }

    /**
     * The method tests the output of the renewItem method when the input person id does not exist.
     */
    @Test
    public void testRenewNonExistingPerson() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        int invalid = 124142124;
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        String error = null;
        try {
            itemReservation = itemReservationService.renewItem(invalid, item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Person does not exist!", error);
    }

    /**
     * The method tests the output of the renewItem method when the input item id does not exist.
     */
    @Test
    public void testRenewInvalidItemId() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        int invalidItemId = 1142415;
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), invalidItemId, existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("item does not exist!", error);
    }

    /**
     * The method tests the output of the renewItem method when the input reservation id does not exist.
     */
    @Test
    public void testRenewInvalidReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        int invalidReservationId = 121219247;
        String error = null;
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation =
                    itemReservationService.renewItem(person.getId(), item.getId(), invalidReservationId, newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("ItemReservation does not exist", error);
    }

    /**
     * The method tests the output of the renewItem method when the input item id does not match the id saved in the reservation.
     */
    @Test
    public void testRenewWrongItemIdWithReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Item item2 = itemRepository.findItemById(reservedItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item2.getId())).thenReturn(true);
        String error = null;
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), item2.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Item id does not match!", error);
    }

    /**
     * The method tests the output of the renewItem method when the input person id does not match the id saved in the reservation.
     */
    @Test
    public void testRenewWrongPersonIdWithReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Person person2 = personService.createPerson("name", "address", "Librarian");
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(personRepository.existsById(person2.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation = itemReservationService.renewItem(person2.getId(), item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Person id does not match!", error);
    }

    /**
     * The method tests the output of the renewItem method when the input item id has not been checked out.
     */
    @Test
    public void testRenewItemNotCheckedOut() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(testRenewNotCheckedOutItemId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("Item is not checked out!", error);
    }

    /**
     * The method tests the output of the renewItem method when the input start date is invalid.
     */
    @Test
    public void testRenewInvalidStartDate() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-08"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("invalid renew date", error);
    }

    /**
     * The method tests the output of the renewItem method when the input end date is longer than 14 days after the old enddate.
     */
    @Test
    public void testRenewDateTooLong() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = "";
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2022-02-10"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertTrue(error.contains("The new end date can only be 14 days after the origin checkout timeslot"));
    }

    /**
     * The method tests the output of the renewItem method when the input end date is before the old end date.
     */
    @Test
    public void testNoNeedToRenew() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        ItemReservation itemReservation = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        String error = null;
        ItemReservation existingItemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(existingItemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-10"));
        try {
            itemReservation = itemReservationService.renewItem(person.getId(), item.getId(), existingItemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservation);
        assertEquals("you have sufficient check out period, no need to renew", error);
    }

    /**
     * The method tests the output of the method returnItem when every input is valid.
     */
    @Test
    public void testReturnItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);

        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-19"));
        try {
            itemReservation =
                    itemReservationService.returnItem(person.getId(), item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(itemReservation);
        assertEquals(item.getId(), itemReservation.getItem().getId());
        assertEquals(item.getName(), itemReservation.getItem().getName());
        assertTrue(itemReservation.getItem().getIsInLibrary());
        assertEquals(person.getId(), itemReservation.getPerson().getId());
        assertEquals(person.getName(), itemReservation.getPerson().getName());
        assertEquals(endTime, itemReservation.getTimeSlot().getEndTime());
        assertEquals(newEndDate, itemReservation.getTimeSlot().getEndDate());
    }

    /**
     * The method tests the output of the method returnItem when the input person id does not exist.
     */
    @Test
    public void testReturnByNonExistingPerson() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);

        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-19"));
        try {
            itemReservation =
                    itemReservationService.returnItem(123123123, item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals("Person does not exist!", error);
    }

    /**
     * The method tests the output of the method returnItem when the input person id does not match the one saved in the reservation.
     */
    @Test
    public void testReturnWrongPersonIf() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Person person2 = personService.createPerson("The name", "The address", "User");
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(personRepository.existsById(person2.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);

        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-19"));
        try {
            itemReservation = itemReservationService.returnItem(person2.getId(), item.getId(), itemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals("Person id does not match!", error);
    }

    /**
     * The method tests the output of the method returnItem when the input item id does not exist.
     */
    @Test
    public void testReturnWrongItemId() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Item item2 = itemRepository.findItemById(reservedItemId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item2.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-19"));
        try {
            itemReservation = itemReservationService.returnItem(person.getId(), item2.getId(), itemReservation.getId(),
                    newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals("Item id does not match!", error);
    }

    /**
     * The method tests the output of the method returnItem when the input item id does not match the one saved in the reservation.
     */
    @Test
    public void testReturnNonExistingItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Item item2 = itemRepository.findItemById(reservedItemId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item2.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-19"));
        try {
            itemReservation =
                    itemReservationService.returnItem(person.getId(), 2122441, itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals("item does not exist", error);
    }

    /**
     * The method tests the output of the method returnItem when the input end date is before the start date in the reservation.
     */
    @Test
    public void testReturnInvalidReturnDate() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-08"));
        try {
            itemReservation =
                    itemReservationService.returnItem(person.getId(), item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertFalse(itemReservation.getItem().getIsInLibrary());
        assertEquals("invalid return date", error);
    }

    /**
     * The method tests the output of the method returnItem when the input end date is late.
     */
    @Test
    public void testReturnLateDate() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-31"));
        try {
            itemReservationService.returnItem(person.getId(), item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("not return on time, need penalty", error);
    }

    /**
     * The method tests the output of the method returnItem when the input end time is late.
     */
    @Test
    public void testReturnLateTime() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = null;
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-22"));
        Time newEndTime = Time.valueOf(LocalTime.parse("10:00"));
        try {
            itemReservationService.returnItem(person.getId(), item.getId(), itemReservation.getId(),
                    newEndDate, newEndTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("not return on time, need penalty", error);
    }

    /**
     * The method tests the output of the method returnItem when the input item is not checked out.
     */
    @Test
    public void testReturnItemNotCheckedOut() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        String error = "";
        Item item = itemRepository.findItemById(ItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(testRenewNotCheckedOutItemId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        Date newEndDate = Date.valueOf(LocalDate.parse("2021-12-22"));
        try {
            itemReservationService.returnItem(person.getId(), item.getId(), itemReservation.getId(), newEndDate, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertTrue(error.contains("Item is not checked out!"));
    }

    /**
     * The method tests delete reservation service.
     */

    @Test
    public void testDeleteReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(person.getId(), item.getId(), itemReservation.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNull(itemReservationRepository.findItemReservationByPersonAndItemAndTimeSlot(person, item,
                itemReservation.getTimeSlot()));
    }

    /**
     * The method test the output of deleteItemReservation when the input person id does not exist.
     */
    @Test
    public void testDeleteReservationNonExistingPerson() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(124314124, item.getId(), itemReservation.getId());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Person does not exist!", error);
    }

    /**
     * The method test the output of deleteItemReservation when the input item id does not exist.
     */
    @Test
    public void testDeleteReservationNonExistingItem() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(person.getId(), 12828734, itemReservation.getId());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("item does not exist", error);
    }

    /**
     * The method test the output of deleteItemReservation when the input reservation id does not exist.
     */
    @Test
    public void testDeleteReservationNonExistingReservation() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(person.getId(), item.getId(), 187255433);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("ItemReservation does not exist", error);
    }

    /**
     * The method test the output of deleteItemReservation when the input item id does not match the one in the reservation.
     */
    @Test
    public void testDeleteReservationWrongItemId() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Item item2 = itemRepository.findItemById(reservedItemId);
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item2.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(person.getId(), item2.getId(), itemReservation.getId());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Item id does not match!", error);
    }

    /**
     * The method test the output of deleteItemReservation when the input person id does not match the one in the reservation.
     */
    @Test
    public void testDeleteReservationWrong() {
        assertEquals(0, itemReservationService.getAllItemReservations().size());
        Item item = itemRepository.findItemById(checkedOutItemId);
        Person person = personRepository.findPersonById(personId);
        Person person2 = personService.createPerson("Name", "address", "Librarian");
        String error = null;
        lenient().when(personRepository.existsById(person.getId())).thenReturn(true);
        lenient().when(personRepository.existsById(person2.getId())).thenReturn(true);
        lenient().when(itemRepository.existsById(item.getId())).thenReturn(true);
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(reservationId);
        lenient().when(itemReservationRepository.existsById(itemReservation.getId())).thenReturn(true);
        try {
            itemReservationService.deleteItemReservation(person2.getId(), item.getId(), itemReservation.getId());
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Person id does not match!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if all inputs are valid.
     */
    @Test
    public void testCreateTimeslot() {
        TimeSlot timeslot = null;
        try {
            timeslot = itemReservationService.createTimeSlot(startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(startTime, timeslot.getStartTime());
        assertEquals(endTime, timeslot.getEndTime());
        assertEquals(startDate, timeslot.getStartDate());
        assertEquals(endDate, timeslot.getEndDate());
    }

    /**
     * The method tests the output of the method createTimeSlot if the input enddate is null.
     */
    @Test
    public void testCreateTimeslotNullEndDate() {
        TimeSlot timeslot = null;
        String error = null;
        try {
            timeslot = itemReservationService.createTimeSlot(startDate, null, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeslot);
        assertEquals("Timeslot end date cannot be empty!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if the input start time is null.
     */
    @Test
    public void testCreateTimeslotNullStartItem() {
        TimeSlot timeSlot = null;
        String error = null;
        try {
            timeSlot = itemReservationService.createTimeSlot(startDate, endDate, null, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeSlot);
        assertEquals("Timeslot start time cannot be empty!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if the input end time is null.
     */
    @Test
    public void testCreateTimeslotNullEndTime() {
        TimeSlot timeSlot = null;
        String error = null;
        try {
            timeSlot = itemReservationService.createTimeSlot(startDate, endDate, startTime, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeSlot);
        assertEquals("Timeslot end time cannot be empty!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if the input start date is null.
     */
    @Test
    public void testCreateTimeslotNullStartDate() {
        TimeSlot timeSlot = null;
        String error = null;
        try {
            timeSlot = itemReservationService.createTimeSlot(null, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeSlot);
        assertEquals("Timeslot start date cannot be empty!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if the input end date is before the input startdate.
     */
    @Test
    public void testCreateTimeslotEndDateBeforeStartDate() {
        TimeSlot timeSlot = null;
        String error = null;
        try {
            timeSlot = itemReservationService.createTimeSlot(endDate, startDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeSlot);
        assertEquals("Timeslot end date cannot be before event start date!", error);
    }

    /**
     * The method tests the output of the method createTimeSlot if the input end time is before the input starttime.
     */
    @Test
    public void testCreateTimeSlotEndTimeBeforeStarTime() {
        TimeSlot timeSlot = null;
        String error = null;
        Time endTime = Time.valueOf(LocalTime.parse("08:00"));
        try {
            timeSlot = itemReservationService.createTimeSlot(startDate, startDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timeSlot);
        assertEquals("Timeslot end time cannot be before event start time!", error);
    }

    /**
     * The method test the output of getItemReservationByPersonAndItm when the input person id and input item id does not exist.
     */
    @Test
    public void testGetItemReservationByPersonAndItemWithNonExistingPersonIdAndItemId() {
        List<ItemReservation> itemReservationList = null;
        String error = "";
        try {
            itemReservationList = itemReservationService.getItemReservationByPersonAndItem(123456, 123456);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemReservationList);
        assertTrue(error.contains("item does not exist"));
        assertTrue(error.contains("person does not exist"));

    }

    /**
     * The method tests the output of the method getPersonsReserveItem when the input item does not exist.
     */
    @Test
    public void testGetPersonReserveItemWithNonExistingPerson() {
        List<Person> personList = null;
        String error = "";
        try {
            personList = itemReservationService.getPersonsReserveItem(123456);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(personList);
        assertEquals("item not exist!", error);
    }

    /**
     * The method tests the output of the method getItemsReservedByPeron when the input person does not exist.
     */
    @Test
    public void testGetItemReservedByPersonWithNonExistingPerson() {
        List<Item> itemList = null;
        String error = "";
        try {
            itemList = itemReservationService.getItemsReservedByPerson(123456);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(itemList);
        assertEquals("person not exist!", error);
    }
}