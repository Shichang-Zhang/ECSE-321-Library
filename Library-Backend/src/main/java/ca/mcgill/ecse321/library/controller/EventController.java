package ca.mcgill.ecse321.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.dto.*;

import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ca.mcgill.ecse321.library.service.EventService;

/**
 * this class contains the controller methods to call perform event database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    EventService eventService;

    /**
     * get all event records in database and return the data transfer object for these event records
     * @return a list of data transfer objects for existing event records
     */
    @GetMapping("/eventList")
    public List<EventDto> getAllEvents(){
        return eventService.getAllEvents().stream().map(event -> HelperMethods.convertToDto(event)).collect(Collectors.toList());
    }

    /**
     * create the event with given parameters
     * @param name
     * @param startDate holding start date
     * @param endDate holding end date
     * @param startTime
     * @param endTime
     * @return the transfer object of the created event
     * @throws IllegalArgumentException
     */
    @PostMapping("/createEvent")
    public EventDto createEvent(@RequestParam("name") String name, @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime)
    throws IllegalArgumentException{
        Event event=eventService.createEvent(name,Date.valueOf(startDate),Date.valueOf(endDate),Time.valueOf(startTime),Time.valueOf(endTime));
        return HelperMethods.convertToDto(event);
    }

    /**
     * find event by id
     * @param id event id
     * @return the transfer object of the event with given id
     * @throws IllegalArgumentException
     */
    @GetMapping("/findEventById")
    public EventDto getEventById(@RequestParam("id") int id)
            throws IllegalArgumentException{
       Event event= eventService.getEvent(id);
        return HelperMethods.convertToDto(event);
    }

    /**
     * find events that have a certain name
     * @param name
     * @return the list of transfer objects of the events that have the input name
     * @throws IllegalArgumentException
     */
    @GetMapping("/findEventByName")
    public List<EventDto> getEventByName(@RequestParam("name") String name)
            throws IllegalArgumentException{
        List<Event> eventList= eventService.getEventByName(name);
        return eventDtoToList(eventList);
    }


    /**
     * update the event name
     * @param id event id
     * @param name new event name
     * @return the data transfer object with the new name
     * @throws IllegalArgumentException
     */
    @PutMapping ("/updateEventName")
    public EventDto updateEventName(@RequestParam("id") int id,@RequestParam("name") String name)
            throws IllegalArgumentException{
       Event event= eventService.updateEventName(id,name);
       return HelperMethods.convertToDto(event);
    }

    /**
     * update the event holding time
     * @param id event id
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return the data transfer object with the new holding time
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateEventTimeSlot")
    public EventDto updateEventTimeSlot(@RequestParam("id") int id,@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime)
            throws IllegalArgumentException{
        Event event= eventService.updateEventTime(id,Date.valueOf(startDate),Date.valueOf(endDate),Time.valueOf(startTime),Time.valueOf(endTime));
        return HelperMethods.convertToDto(event);
    }

    /**
     * delete event by id
     * @param id event id
     * @return the data transfer object of the delete event
     * @throws IllegalArgumentException
     */
    @DeleteMapping("/deleteEvent")
    public EventDto deleteEvent(@RequestParam("id") int id)
            throws IllegalArgumentException{
        Event event= eventService.deleteEvent(id);
        return HelperMethods.convertToDto(event);
    }

    /**
     * transfer a list of events to a list of data transfer objects of events
     * @param events
     * @return a list of data transfer objects of the events
     */
    private List<EventDto> eventDtoToList(List<Event> events){
        if(events==null){
            return null;
        }else{
            List<EventDto> eventDtoList=new ArrayList<EventDto>();
            for(Event e:events){
                eventDtoList.add(HelperMethods.convertToDto(e));
            }
            return eventDtoList;
        }
    }

}
