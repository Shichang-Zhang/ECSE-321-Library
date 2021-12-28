package ca.mcgill.ecse321.library.servicetest;

import ca.mcgill.ecse321.library.dao.EventRegistrationRepository;
import ca.mcgill.ecse321.library.dao.EventRepository;
import ca.mcgill.ecse321.library.dao.LibrarySystemRepository;
import ca.mcgill.ecse321.library.dao.TimeSlotRepository;
import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.service.EventService;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * unit test of the event service
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class EventServiceTest {
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private LibrarySystemRepository librarySystemRepository;
    //although it is shown as unused, but without the variable, the test will fail
    @Mock
    private EventRegistrationRepository eventRegistrationRepository;
    @InjectMocks
    private EventService eventService;

    private static final int EVENT1_KEY = 1;
    private static final int NON_EXISTING_KEY = 114514;
    private static final String EVENT1_NAME = "NAME1";
    private static final String EVENT2_NAME = "NAME2";
    private static final String TIME = "00:00:00";
    private static final String LATER_TIME = "23:00:00";
    private static final String DATE = "2025-11-11";
    private static final String LATER_DATE = "2026-11-11";

    @BeforeEach
    public void setMockOutput() {
        //Set mock output: find an event by id
        lenient().when(eventRepository.findEventById(anyInt())).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(EVENT1_KEY)) {
                Event event = new Event();
                event.setId(EVENT1_KEY);
                event.setName(EVENT1_NAME);
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartDate(Date.valueOf(DATE));
                timeSlot.setEndDate(Date.valueOf(DATE));
                timeSlot.setStartTime(Time.valueOf(TIME));
                timeSlot.setEndTime(Time.valueOf(TIME));
                event.setTimeSlot(timeSlot);
                return event;
            } else {
                return null;
            }
        });
        //set mock output: get the library system
        lenient().when(librarySystemRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<LibrarySystem> librarySystemList = new ArrayList<>();
            LibrarySystem librarySystem = new LibrarySystem();
            librarySystemList.add(librarySystem);
            return librarySystemList;
        });
        //set mock output: find all events
        lenient().when(eventRepository.findAll()).thenAnswer((InvocationOnMock invocation) ->
        {
            Event event1 = new Event();
            Event event2 = new Event();
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(event1);
            eventList.add(event2);
            return eventList;
        });
//set mock output: find events by name
        lenient().when(eventRepository.findByName(anyString())).thenAnswer((InvocationOnMock invocation) ->
        {
            Event event1 = new Event();
            event1.setName(EVENT1_NAME);
            Event event1_copy = new Event();
            event1_copy.setName(EVENT1_NAME);
            ArrayList<Event> eventList = new ArrayList<>();
            eventList.add(event1);
            eventList.add(event1_copy);
            return eventList;
        });
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(eventRepository.save(any(Event.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * test create event
     */
    @Test
    public void testCreateEvent() {
        String name = "Party1";
        Event event = null;
        Date startDate = Date.valueOf(DATE);
        Date endDate = Date.valueOf(DATE);
        Time startTime = Time.valueOf(TIME);
        Time endTime = Time.valueOf(TIME);
        try {
            event = eventService.createEvent(name, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(event);
        assertEquals(name, event.getName());
        assertEquals(endDate, event.getTimeSlot().getEndDate());
        assertEquals(endTime, event.getTimeSlot().getEndTime());
    }

    /**
     * test create event with null parameters
     */
    @Test
    public void testCreateEventWithInvalidEventNameAndTime() {
        String name = null;
        Event event = null;
        Date startDate = null;
        Date endDate = null;
        Time startTime = null;
        Time endTime = null;
        String error = "";
        try {
            event = eventService.createEvent(name, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(event);
        assertTrue(error.contains("Event name cannot be empty!"));
        assertTrue(error.contains("Timeslot endDate cannot be empty! "));
        assertTrue(error.contains("Timeslot startDate cannot be empty! "));
        assertTrue(error.contains("Timeslot start time cannot be empty! "));
        assertTrue(error.contains("Timeslot end time cannot be empty!"));
    }

    /**
     * test create event with invalid date parameters
     */
    @Test
    public void testCreateEventWithInvalidDate() {
        String name = "Party1";
        Event event = null;
        Date startDate = Date.valueOf(LATER_DATE);
        Date endDate = Date.valueOf(DATE);
        Time startTime = Time.valueOf(LATER_TIME);
        Time endTime = Time.valueOf(TIME);
        String error = "";
        try {
            event = eventService.createEvent(name, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(event);
        assertTrue(error.contains("Timeslot end date cannot be before event start date!"));
    }

    /**
     * test create event with invalid time parameters
     */
    @Test
    public void testCreateEventWithInvalidTime() {
        String name = "Party1";
        Event event = null;
        Date startDate = Date.valueOf(DATE);
        Date endDate = Date.valueOf(DATE);
        Time startTime = Time.valueOf(LATER_TIME);
        Time endTime = Time.valueOf(TIME);
        String error = "";
        try {
            event = eventService.createEvent(name, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(event);
        assertTrue(error.contains("Timeslot end time cannot be before event start time!"));
    }

    /**
     * test get event by id
     */
    @Test
    public void testGetEventById() {
        assertEquals(EVENT1_KEY, eventService.getEvent(EVENT1_KEY).getId());
        assertEquals(EVENT1_NAME, eventService.getEvent(EVENT1_KEY).getName());
    }

    /**
     * test get non-existing event
     */
    @Test
    public void testGetNonExistingEvent() {
        assertNull(eventService.getEvent(NON_EXISTING_KEY));
    }

    /**
     * test get events by valid name
     */
    @Test
    public void testGetEventsByName() {
        assertEquals(2, eventService.getEventByName(EVENT1_NAME).size());
        assertEquals(EVENT1_NAME, eventService.getEventByName(EVENT1_NAME).get(0).getName());
        assertEquals(EVENT1_NAME, eventService.getEventByName(EVENT1_NAME).get(1).getName());
    }

    /**
     * test get events by null name
     */
    @Test
    public void testGetEventsByNameWithNullName() {
        String error = "";
        try {
            eventService.getEventByName(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Event name cannot be empty!", error);
    }

    /**
     * test update event by valid name
     */
    @Test
    public void testUpdateEventName() {
        Event event = null;
        try {
            event = eventService.updateEventName(EVENT1_KEY, EVENT2_NAME);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(EVENT2_NAME, event.getName());
    }

    /**
     * test update event by null name
     */
    @Test
    public void testUpdateEventNameWithNullName() {
        Event event = null;
        String error = "";
        try {
            event = eventService.updateEventName(NON_EXISTING_KEY, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(event);
        assertTrue(error.contains("New name cannot be empty!"));
        assertTrue(error.contains("Event does not exist!"));
    }

    /**
     * test update event with valid date adn time parameters
     */
    @Test
    public void testUpdateEventTime() {
        Event event = null;
        Date startDate = Date.valueOf(LATER_DATE);
        Date endDate = Date.valueOf(LATER_DATE);
        Time startTime = Time.valueOf(LATER_TIME);
        Time endTime = Time.valueOf(LATER_TIME);
        try {
            event = eventService.updateEventTime(EVENT1_KEY, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(endTime, event.getTimeSlot().getEndTime());
        assertEquals(endDate, event.getTimeSlot().getEndDate());
        assertEquals(startTime, event.getTimeSlot().getStartTime());
        assertEquals(startDate, event.getTimeSlot().getStartDate());
    }

    /**
     * test update event with null date and time parameters
     */
    @Test
    public void testUpdateEventTimeWithNullTime() {
        Event event = null;
        Date startDate = null;
        Date endDate = null;
        Time startTime = null;
        Time endTime = null;
        String error = "";
        try {
            event = eventService.updateEventTime(NON_EXISTING_KEY, startDate, endDate, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(event);
        assertTrue(error.contains("Event does not exist!"));
    }

    /**
     * test normally delete event
     */
    @Test
    public void testDeleteEvent() {
        assertNotNull(eventService);
        assertEquals(EVENT1_KEY, eventService.deleteEvent(EVENT1_KEY).getId());
        assertEquals(EVENT1_NAME, eventService.deleteEvent(EVENT1_KEY).getName());
    }

    /**
     * test delete non-existing event
     */
    @Test
    public void testDeleteEventWithNonExistingEvent() {
        String error = "";
        try {
            eventService.deleteEvent(NON_EXISTING_KEY);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Event does not exist!", error);

    }

    /**
     * test get all events
     */
    @Test
    public void testGetAllEvent() {
        assertEquals(2, eventService.getAllEvents().size());
    }

}