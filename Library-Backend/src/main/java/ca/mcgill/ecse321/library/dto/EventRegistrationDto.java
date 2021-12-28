package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of event registration model.
 * have association with person DTO and event DTO.
 * have getter and setter methods for private fields.
 */
public class EventRegistrationDto {

    private int id;
    private PersonDto personDto;
    private EventDto eventDto;

    public EventRegistrationDto(int id) {
        this.id = id;
    }

    public EventRegistrationDto(int id, PersonDto personDto, EventDto eventDto) {
        this.id = id;
        this.personDto = personDto;
        this.eventDto = eventDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonDto getPersonDto() {
        return personDto;
    }

    public void setPersonDto(PersonDto personDto) {
        this.personDto = personDto;
    }

    public EventDto getEventDto() {
        return eventDto;
    }

    public void setEventDto(EventDto eventDto) {
        this.eventDto = eventDto;
    }
}
