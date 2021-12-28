package ca.mcgill.ecse321.library.servicetest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import ca.mcgill.ecse321.library.service.*;
import ca.mcgill.ecse321.library.dao.*;

/**
 * unit test of event registration service
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class EventRegistrationServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventRegistrationRepository eventRegistrationRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private LibrarySystemRepository librarySystemRepository;
    @InjectMocks
    private EventRegistrationService eventRegistrationService;
    @InjectMocks
    private EventService eventService;
    @InjectMocks
    private PersonService personService;

    private static final int personId = 11;
    private static final int eventId = 12345;
    private static final Time startTime = Time.valueOf(LocalTime.parse("09:00"));
    private static final Date startDate = Date.valueOf(LocalDate.parse("2021-12-09"));
    private static final Time endTime = Time.valueOf(LocalTime.parse("09:00"));
    private static final Date endDate = Date.valueOf(LocalDate.parse("2021-12-22"));

    @BeforeEach
    public void setMockOutput() {
        //        Set mock output: find a person by id
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
        //        set mock output: find an event by id
        lenient().when(eventRepository.findEventById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(eventId)) {
                Event event = new Event();
                event.setId(eventId);
                event.setName("Event Name");
                TimeSlot timeslot = new TimeSlot();
                timeslot.setStartDate(startDate);
                timeslot.setStartTime(startTime);
                timeslot.setEndDate(endDate);
                timeslot.setEndTime(endTime);
                event.setTimeSlot(timeslot);
                return event;

            } else {
                return null;
            }
        });
        //        set mock output: get a library system
        lenient().when(librarySystemRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<LibrarySystem> librarySystemList = new ArrayList<>();
            LibrarySystem librarySystem = new LibrarySystem();
            librarySystemList.add(librarySystem);
            return librarySystemList;
        });
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(eventRepository.save(any(Event.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(personRepository.save(any(Person.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(eventRegistrationRepository.save(any(EventRegistration.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * The method tests the output of the method attendEvent when the input are all valid.
     */
    @Test
    public void testAttendEvent() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        personService.createPerson("The Name", "Address", "User");
        eventService.createEvent("Event", startDate, endDate, startTime, endTime);
        EventRegistration eventRegistration = null;
        try {
            eventRegistration = eventRegistrationService.attendEvent(personId, eventId);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(eventRegistration);
        assertEquals(personId, eventRegistration.getPerson().getId());
        assertEquals(eventId, eventRegistration.getEvent().getId());
    }

    /**
     * The method tests the output of the method attendEVent when the person is already registered to the event.
     */
    @Test
    public void testPersonSAlreadyAttended() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        EventRegistration eventRegistration = null;
        String error = null;
        lenient().when(eventRegistrationRepository.existsByPersonAndEvent(any(), any())).thenReturn(true);
        try {
            eventRegistration = eventRegistrationService.attendEvent(personId, eventId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(eventRegistration);
        assertEquals("Person is already registered to this event!", error);
    }

    /**
     * The method tests the output of the method attendEvent when a person wants to register an event in the past.
     */
    @Test
    public void testRegisterPastEventDate() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        Event event = new Event();
        event.setId(112414);
        event.setName("Event Name");
        TimeSlot timeslot = new TimeSlot();
        Date endDate = Date.valueOf(LocalDate.parse("2021-08-09"));
        timeslot.setStartDate(startDate);
        timeslot.setStartTime(startTime);
        timeslot.setEndDate(endDate);
        timeslot.setEndTime(endTime);
        event.setTimeSlot(timeslot);
        lenient().when(eventRepository.findEventById(112414)).thenReturn(event);
        EventRegistration eventRegistration = null;
        String error = null;
        try {
            eventRegistration = eventRegistrationService.attendEvent(personId, 112414);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(eventRegistration);
        assertEquals("cannot register an event in the past", error);
    }

    /**
     * The method tests the output of the method attendEvent when the input person id is not valid.
     */
    @Test
    public void testRegisterWrongPersonInput() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        int invalidPersonId = 1212;
        EventRegistration eventRegistration = null;
        String error = null;
        try {
            eventRegistration = eventRegistrationService.attendEvent(invalidPersonId, eventId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(eventRegistration);
        assertEquals("Person needs to be selected for registration!", error);
    }

    /**
     * The method tests the output of the method atteneEvent when the input event id is invalid.
     */
    @Test
    public void testRegisterWrongEventInput() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        int invalidEventId = 1212;
        EventRegistration eventRegistration = null;
        String error = null;
        try {
            eventRegistration = eventRegistrationService.attendEvent(personId, invalidEventId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(eventRegistration);
        assertEquals("Event does not exist!", error);
    }

    /**
     * The method tests the delete of the registration.
     */
    @Test
    public void testDeleteEventRegistration() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        Person person = personRepository.findPersonById(personId);
        Event event = eventRepository.findEventById(eventId);
        EventRegistration eventRegistration =  eventRegistrationService.attendEvent(personId, eventId);
        lenient().when(eventRegistrationRepository.findByPersonAndEvent(any(), any())).thenReturn(eventRegistration);
        try {
            eventRegistrationService.deleteEventRegistration(personId, eventId);
        } catch (IllegalArgumentException e) {
            fail();
        }
        boolean check = eventRegistrationRepository.existsByPersonAndEvent(person, event);
        assertFalse(check);
    }

    /**
     * The method tests the output when deleting a non-existing registration.
     */
    @Test
    public void testDeleteNonExistingRegistration() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        String error = null;
        try {
            eventRegistrationService.deleteEventRegistration(personId, eventId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("no such registration exist", error);
    }

    /**
     * The method tests the method that all the event registered by the person.
     */
    @Test
    public void testGetEventByPerson() {
        EventRegistration eventRegistration = eventRegistrationService.attendEvent(personId, eventId);
        List<EventRegistration> eventRegistrations = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        eventRegistrations.add(eventRegistration);
        lenient().when(eventRegistrationRepository.findByPerson(any())).thenReturn(eventRegistrations);
        try {
            events = eventRegistrationService.getEventsByPerson(personId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assertNotNull(events);
        assertEquals(events.get(0).getId(), eventRegistrations.get(0).getEvent().getId());
    }

    /**
     * The method tests the method that get all the participants of an event.
     */
    @Test
    public void testGetPersonByEvent() {
        EventRegistration eventRegistration = eventRegistrationService.attendEvent(personId, eventId);
        List<EventRegistration> eventRegistrations = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        eventRegistrations.add(eventRegistration);
        lenient().when(eventRegistrationRepository.findByEvent(any())).thenReturn(eventRegistrations);
        try {
            persons = eventRegistrationService.getPersonsByEvent(personId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        assertNotNull(persons);
        assertEquals(persons.get(0).getId(), eventRegistrations.get(0).getPerson().getId());
    }

    /**
     * The method tests the output of finding a registration of event with the id of the event and the id of the person regisered to the event.
     */
    @Test
    public void testDeleteEventRegistrationByEvent() {
        assertEquals(0, eventRegistrationService.getAllEventRegistration().size());
        Person person = personRepository.findPersonById(personId);
        EventRegistration eventRegistration = eventRegistrationService.attendEvent(personId, eventId);
        List<EventRegistration> eventRegistrations = new ArrayList<>();
        eventRegistrations.add(eventRegistration);
        lenient().when(eventRegistrationRepository.findByEvent(any())).thenReturn(eventRegistrations);
        try {
            eventRegistrationService.deleteEventRegistrationByEvent(eventId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        List<EventRegistration> check = eventRegistrationRepository.findByPerson(person);
        assertEquals(0, check.size());
    }
}
