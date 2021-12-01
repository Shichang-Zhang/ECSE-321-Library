package ca.mcgill.ecse321.library.service;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse321.library.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.*;

/**
 * event service methods to manipulate data in backend, used in controller.
 */
@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;
    @Autowired
    private LibrarySystemRepository librarySystemRepository;

    /**
     * create an event with the event name and event holding time period parameters.
     * @param name event name
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return created event object
     */
    @Transactional
    public Event createEvent(String name,Date startDate,Date endDate,Time startTime,Time endTime){
        String errorMessage = checkInputNull(name,endDate,startDate,startTime,endTime);
        if (!errorMessage.equals("")) throw new IllegalArgumentException(errorMessage);

        String error = "";
        if (endDate != null && startDate != null && endDate.before(startDate)) {
            error = error + "Timeslot end date cannot be before event start date!";
        }
        if (startDate.compareTo(endDate)==0 &&endTime != null && startTime != null && endTime.before(startTime)) {
            error = error + "Timeslot end time cannot be before event start time!";
        }
        //avoid create event with past time
        error=error+checkEventTimeIsInPast(startDate,startTime);

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(timeSlot.hashCode()*startTime.hashCode());
        timeSlot.setStartDate(startDate);
        timeSlot.setEndDate(endDate);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlotRepository.save(timeSlot);
        Event event=new Event();
        event.setId(event.hashCode()+name.hashCode()*timeSlot.hashCode());
        event.setName(name);
        event.setTimeSlot(timeSlot);
        eventRepository.save(event);
        return event;
    }

    /**
     * get all events in record
     * @return a list of events that were held and will be held in the library
     */
    @Transactional
    public List<Event> getAllEvents(){
        return HelperMethods.toList(eventRepository.findAll());
    }

    /**
     * find event by id
     * @param id event id
     * @return an event with the input id, otherwise null
     */
    @Transactional
    public Event getEvent(int id){
        Event event=eventRepository.findEventById(id);
        return event;
    }

    /**
     * find events by name
     * @param name event name
     * @return a list of events in record that have the input name
     */
    @Transactional
    public List<Event> getEventByName(String name){
        if(name ==null|| name.trim().length() == 0){
            throw new IllegalArgumentException("Event name cannot be empty!");
        }
        List<Event> events=eventRepository.findByName(name);
        return events;
    }

    /**
     * update the created event name
     * @param id event id
     * @param newName the new name
     * @return the event with the new asserted name
     */
    @Transactional
    public Event updateEventName(int id,String newName){
        String error = "";

        if(newName ==null|| newName.trim().length() == 0){
            error=error+("New name cannot be empty!");
        }
        Event event=eventRepository.findEventById(id);
        if(event==null){
            error=error+"Event does not exist!";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        event.setName(newName);
        eventRepository.save(event);
        return event;
    }

    /**
     * update the holding time of the event
     * @param id event id
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return event with the update time slot
     */
    @Transactional
    public Event updateEventTime(int id,Date startDate,Date endDate,Time startTime,Time endTime){
        String error = "";

        Event event=eventRepository.findEventById(id);
        if(event==null) error = error + "Event does not exist!";
        error = error + HelperMethods.checkStartTimeAndEndTime(startTime,endTime);
        error = error + HelperMethods.checkStartDateAndEndDate(startDate,endDate);
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        //avoid create event with past time
        error=error+checkEventTimeIsInPast(startDate,startTime);

        TimeSlot timeSlot=event.getTimeSlot();
        if (timeSlot.getStartDate().compareTo(timeSlot.getEndDate())==0&&endTime != null && startTime != null && endTime.before(startTime)) {
            error = error + "Timeslot end time cannot be before event start time!";
        }

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        timeSlot.setStartDate(startDate);
        timeSlot.setEndDate(endDate);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        event.setTimeSlot(timeSlot);
        timeSlotRepository.save(timeSlot);
        eventRepository.save(event);
        return event;
    }

    /**
     * delete the event so that the records related with the event in the database will be cleared.
     * @param id event id
     * @return original event object
     */
    @Transactional
    public Event deleteEvent(int id){
        String error = "";
        Event event=eventRepository.findEventById(id);
        if(event==null) {
            error = error + "Event does not exist!";
        }
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        eventRegistrationRepository.deleteEventRegistrationsByEvent(event);
        eventRepository.deleteById(id);
        return event;
    }

    /**
     * check whether the input parameters are null
     * @param name
     * @param endDate
     * @param startDate
     * @param startTime
     * @param endTime
     * @return error message if input parameters are null
     */
    private String checkInputNull(String name,Date endDate,Date startDate,Time startTime,Time endTime){
        String error="";
        if(name ==null|| name.trim().length() == 0){
            error=error+("Event name cannot be empty!");
        }
        if (endDate == null) {
            error = error + "Timeslot endDate cannot be empty! ";
        }
        if (startDate == null) {
            error = error + "Timeslot startDate cannot be empty! ";
        }
        if (startTime == null) {
            error = error + "Timeslot start time cannot be empty! ";
        }
        if (endTime == null) {
            error = error + "Timeslot end time cannot be empty! ";
        }
        return error;
    }

    /**
     * check whether the input date is in the past. Cannot create the event or update the event time to be in the past
     * @param startDate
     * @param startTime
     * @return empty message if input date is not in the past, otherwise an error message.
     */
    private String checkEventTimeIsInPast(Date startDate, Time startTime){
        String error = "";
        if (startDate.before(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate())){
            error = error+"can not create event with past time";
        }
        if (startDate.compareTo(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate())==0){
            if (startTime.before(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTTime())){
                error = error+"can not create event with past time";
            }
        }
        return error;
    }

}
