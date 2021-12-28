package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of business hour model.
 * have id, dayOfWeek, startTime, endTime fields.
 * have getter and setter methods for private fields.
 */
public class ItemReservationDto {
    private int id;
    private PersonDto personDto;
    private ItemDto itemDto;
    private TimeSlotDto timeSlotDto;

    public ItemReservationDto(int id) {
        this.id = id;
    }

    public ItemReservationDto(int id, PersonDto personDto, ItemDto itemDto, TimeSlotDto timeSlotDto) {
        this.id = id;
        this.personDto = personDto;
        this.itemDto = itemDto;
        this.timeSlotDto = timeSlotDto;
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

    public ItemDto getItemDto() {
        return itemDto;
    }

    public void setItemDto(ItemDto itemDto) {
        this.itemDto = itemDto;
    }

    public TimeSlotDto getTimeSlotDto() {
        return timeSlotDto;
    }

    public void setTimeSlotDto(TimeSlotDto timeSlotDto) {
        this.timeSlotDto = timeSlotDto;
    }
}
