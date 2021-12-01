package ca.mcgill.ecse321.library.controller;

import ca.mcgill.ecse321.library.dto.*;
import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform event database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/eventRegistrations")
public class EventRegistrationController {

    @Autowired
    private EventRegistrationService eventRegistrationService;

    /**
     * get all event registration records
     * @return a list of data transfer objects of the event registration records
     */
    @GetMapping(value = {"/getEventRegistrationList","/getEventRegistrationList/"})
    public List<EventRegistrationDto> getAllEventRegistration(){
        List<EventRegistration> eventRegistrationList= eventRegistrationService.getAllEventRegistration();
        return eventRegistrationList.stream().map(eventRegistration->HelperMethods.convertToDto(eventRegistration)).collect(Collectors.toList());
    }

    /**
     * a person tries to attend an existing event
     * @param pid
     * @param eid
     * @return the data transfer object of event registration
     * @throws IllegalArgumentException
     */
    @PostMapping(value = {"/attend","/attend/"})
    public EventRegistrationDto attendEventRegistration(@RequestParam(name="pid") int pid, @RequestParam(name="eid") int eid) throws IllegalArgumentException {

        EventRegistration eventRegistration = eventRegistrationService.attendEvent(pid,eid);

        return HelperMethods.convertToDto(eventRegistration);
    }

    /**
     * a person wants to cancel the event that he/she has registered before
     * @param pid
     * @param eid registered event id
     * @return a flag indicating the cancel operation status, true if it is cancelled.
     */
    @DeleteMapping(value = {"/cancel","/cancel/"})
    public boolean cancelEventRegistration(@RequestParam(name="pid") int pid, @RequestParam(name="eid") int eid){
        boolean flag = false;
        try{
            eventRegistrationService.deleteEventRegistration(pid,eid);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * find members who attend the given event
     * @param eid
     * @return a list of data transfer object of people who attended or will attend the event
     */
    @GetMapping(value = {"/getPersonByEvent","/getPersonByEvent/"})
    public List<PersonDto> getPersonByEvents(@RequestParam(name="eid") int eid){
        List<Person> personList = eventRegistrationService.getPersonsByEvent(eid);
        List<PersonDto> personDtoList = new ArrayList<>();
        for (Person person : personList){
            PersonDto personDto = new PersonDto(person.getId(),person.getName(),person.getAddress());
            personDtoList.add(personDto);
        }
        return personDtoList;
    }


    /**
     * get all events that registered by the person
     * @param pid
     * @return a list of data transfer object of events registered by the person
     */
    @GetMapping(value = {"/getEventsByPerson","/getEventsByPerson/"})
    public List<EventDto> getEventsByPerson(@RequestParam(name="pid") int pid){
        List<Event> events = eventRegistrationService.getEventsByPerson(pid);
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event e : events){
            EventDto eventDto = new EventDto(e.getId(),e.getName(),HelperMethods.convertToDto(e.getTimeSlot()));
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    /**
     * find the event registration records done by a person to an event
     * @param pid
     * @param eid
     * @return a data transfer object of event registration
     */
    @GetMapping(value = {"/getEventRegistrationByPersonAndEvent","/getEventRegistrationByPersonAndEvent/"})
    public EventRegistrationDto getEventRegistrationByPersonAndEvent(@RequestParam(name="pid") int pid,@RequestParam(name="eid") int eid){
        EventRegistration eventRegistration = eventRegistrationService.getEventRegistrationByPersonAndEvent(pid,eid);
        EventRegistrationDto eventRegistrationDto = new EventRegistrationDto(eventRegistration.getId(),HelperMethods.convertToDto(eventRegistration.getPerson()),HelperMethods.convertToDto(eventRegistration.getEvent()));
        return eventRegistrationDto;
    }


    /**
     * find the number of people who registered the given event
     * @param eid
     * @return the number of people who registered the given event
     */
    @GetMapping(value = {"/getParticipantsNumber","/getParticipantsNumber/"})
    public int findParticipantsNumber(@RequestParam(name="eid") int eid){
        return this.getPersonByEvents(eid).size();
    }


}
