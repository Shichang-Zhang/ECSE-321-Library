package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of event model.
 * have id, name fields.
 * link to a time slot DTO indicating the event holding time period.
 * have getter and setter methods for private fields.
 */
public class EventDto {
    private int id;
    private String name;
    private TimeSlotDto timeSlotDto;

    public EventDto(int id,String name,TimeSlotDto timeSlotDto){
        this.id=id;
        this.name=name;
        this.timeSlotDto=timeSlotDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeSlotDto getTimeSlotDto() {
        return timeSlotDto;
    }

    public void setTimeSlotDto(TimeSlotDto timeSlotDto) {
        this.timeSlotDto = timeSlotDto;
    }
}
