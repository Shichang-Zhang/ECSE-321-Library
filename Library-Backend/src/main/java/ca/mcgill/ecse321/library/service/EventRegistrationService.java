package ca.mcgill.ecse321.library.service;

import ca.mcgill.ecse321.library.HelperMethods;
import ca.mcgill.ecse321.library.dao.EventRegistrationRepository;
import ca.mcgill.ecse321.library.dao.EventRepository;
import ca.mcgill.ecse321.library.dao.LibrarySystemRepository;
import ca.mcgill.ecse321.library.dao.PersonRepository;
import ca.mcgill.ecse321.library.model.Event;
import ca.mcgill.ecse321.library.model.EventRegistration;
import ca.mcgill.ecse321.library.model.LibrarySystem;
import ca.mcgill.ecse321.library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * event registration service methods to manipulate data in backend, used in controller.
 */
@Service
public class EventRegistrationService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LibrarySystemRepository librarySystemRepository;

    /**
     * get all event registration records
     * @return a list of all event registration
     */
    @Transactional
    public List<EventRegistration> getAllEventRegistration(){
        return HelperMethods.toList(eventRegistrationRepository.findAll());
    }

    /**
     * a member wants to attend an event
     * @param pid person id
     * @param eid event id
     * @return the registration record if successful
     */
    @Transactional
    public EventRegistration attendEvent(int pid, int eid) {
        String error = "";

        Person person = personRepository.findPersonById(pid);
        Event event = eventRepository.findEventById(eid);
        error= error + checkPersonAndEventInput(person,event);
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);
        if (eventRegistrationRepository.existsByPersonAndEvent(person, event)) {
            error = error + "Person is already registered to this event!";
        }
        //check whether it is a late event register
        error=error+checkRegisterAvailability(event);
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        EventRegistration eventRegistration=new EventRegistration();
        eventRegistration.setId((eventRegistration.hashCode()*(person.hashCode()+event.hashCode())));
        eventRegistration.setPerson(person);
        eventRegistration.setEvent(event);
        eventRegistrationRepository.save(eventRegistration);
        return eventRegistration;
    }

    /**
     * a person wants to unregister an event
     * @param pid person id
     * @param eid event id
     */
    @Transactional
    public void deleteEventRegistration(int pid, int eid){
        String error = "";
        Person person = personRepository.findPersonById(pid);
        Event event = eventRepository.findEventById(eid);
        error= error + checkPersonAndEventInput(person,event);

        //check whether it is a late event unregister
        error=error+checkRegisterAvailability(event);

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        EventRegistration eventRegistration = this.getEventRegistrationByPersonAndEvent(pid,eid);
        if (eventRegistration==null){
            throw new IllegalArgumentException("no such registration exist");
        }
        eventRegistrationRepository.deleteById(eventRegistration.getId());
    }

    /**
     * find members who attend the event
     * @param eid event id
     * @return a list of people attending the event
     */
    @Transactional
    public List<Person> getPersonsByEvent(int eid){
        ArrayList<Person> personList = new ArrayList<>();
        Event event = eventRepository.findEventById(eid);
        //find the event registration related with the event
        List<EventRegistration> eventRegistrations=eventRegistrationRepository.findByEvent(event);
        //add the person to the person list
        for (EventRegistration eventRegistration : eventRegistrations){
            Person person = eventRegistration.getPerson();
            if (person==null || personList.contains(person)) {
                throw new IllegalArgumentException("lack person information in the event registration");
            }
            personList.add(person);
        }
        return personList;
    }

    /**
     * find the events that the person has attended
     * @param pid person id
     * @return a list of events that the person attended
     */
    @Transactional
    public List<Event> getEventsByPerson(int pid){
        ArrayList<Event> eventList = new ArrayList<>();
        Person person = personRepository.findPersonById(pid);
        //get the event registration related with the person
        List<EventRegistration> eventRegistrations=eventRegistrationRepository.findByPerson(person);
        //add the event to the event list
        for (EventRegistration eventRegistration : eventRegistrations){
            Event event = eventRegistration.getEvent();
            if (event==null || eventList.contains(event)) {
                throw new IllegalArgumentException("lack event information in the event registration");
            }
            eventList.add(event);
        }
        return eventList;
    }

    /**
     * find a person's the event registration record to an event
     * @param pid person id
     * @param eid event id
     * @return the eventRegistration if successful
     */
    @Transactional
    public EventRegistration getEventRegistrationByPersonAndEvent(int pid, int eid){
        Person person = personRepository.findPersonById(pid);
        if (person==null){
            throw new IllegalArgumentException("person does not exist");
        }

        Event event = eventRepository.findEventById(eid);
        if (event==null){
            throw new IllegalArgumentException("event does not exist");
        }

        return eventRegistrationRepository.findByPersonAndEvent(person,event);
    }

    /**
     * when an event is cancelled, all related event registration records should be deleted
     * @param eid cancelled event id
     */
    @Transactional
    public void deleteEventRegistrationByEvent(int eid){
        Event event = eventRepository.findEventById(eid);
        if (event==null){
            throw new IllegalArgumentException("no such event");
        }
        //get all event registrations with the event and delete them.
        List<EventRegistration> eventRegistrations = eventRegistrationRepository.findByEvent(event);
        for (EventRegistration e : eventRegistrations){
            eventRegistrationRepository.deleteById(e.getId());
        }
    }

    /**
     * check whether the input person or event is null
     * @param person person
     * @param event event
     * @return empty error message if parameters are not null, otherwise an error message will be returned
     */
    private String checkPersonAndEventInput(Person person,Event event){
        String error = "";
        if (person == null) {
            error = error + "Person needs to be selected for registration! ";
        }
        if (event == null) {
            error = error + "Event does not exist!";
        }
        return error;
    }

    /**
     * check whether the person register/unregister the event after the event ends
     * @param event event
     * @return empty error message if normal register, otherwise an error message will be send to alert the late register
     */
    private String checkRegisterAvailability(Event event){
        String error = "";
        //people should register before event ends, event date>= register date
        LibrarySystem librarySystem = HelperMethods.toList(librarySystemRepository.findAll()).get(0);
        if(event.getTimeSlot().getEndDate().before(librarySystem.getCurrenTDate())){
            error = error + "cannot register an event in the past";
        }
        // at the event end date, cannot register after event close
        if (event.getTimeSlot().getEndDate().compareTo(librarySystem.getCurrenTDate())==0 &&
                event.getTimeSlot().getEndTime().before(librarySystem.getCurrenTTime())){
            error = error + "cannot register an event in the past";
        }
        return error;
    }
}
