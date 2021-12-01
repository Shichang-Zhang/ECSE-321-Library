package ca.mcgill.ecse321.library.controller;

import ca.mcgill.ecse321.library.dto.*;
import ca.mcgill.ecse321.library.model.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * helper methods to support the controller layer methods
 */
public class HelperMethods {

    /**
     * transfer a iterable input to a list
     * @param iterable iterable type input
     * @param <T> data type in the iterable
     * @return the list form of the iterable
     */
    public static <T> List<T> toList(Iterable<T> iterable){
        if (iterable==null){
            return null;
        }else {
            List<T> resultList = new ArrayList<T>();
            for (T t : iterable) {
                resultList.add(t);
            }
            return resultList;
        }
    }

    /**
     * convert an eventRegistration object to the data transfer object of the eventRegistration
     * @param eventRegistration
     * @return the data transfer object of the input eventRegistration
     */
    public static EventRegistrationDto convertToDto(EventRegistration eventRegistration){
        if (eventRegistration == null) {
            throw new IllegalArgumentException("There is no such eventRegistration!");
        }
        PersonDto personDto=convertToDto(eventRegistration.getPerson());
        EventDto eventDto=convertToDto(eventRegistration.getEvent());
        int id=eventRegistration.getId();
        return new EventRegistrationDto(id,personDto,eventDto);
    }

    /**
     * convert an onlineAccount object to the data transfer object of the onlineAccount
     * @param onlineAccount
     * @return the data transfer object of the input onlineAccount
     */
    public static OnlineAccountDto convertToDto(OnlineAccount onlineAccount){
        if (onlineAccount == null) {
            throw new IllegalArgumentException("There is no such online account!");
        }
        OnlineAccountDto onlineAccountDto=new OnlineAccountDto(onlineAccount.getId(),onlineAccount.getUsername(), onlineAccount.getEmail(),onlineAccount.getUser().getId());
        return onlineAccountDto;
    }

    /**
     * convert an user object to the data transfer object of the user
     * @param user
     * @return the data transfer object of the input user
     */
    public static UserDto convertToDto(User user){
        if (user == null) {
            throw new IllegalArgumentException("There is no such user!");
        }
        UserDto userDto=null;
        if(user.getOnlineAccount()==null){
            userDto=new UserDto(user.getId(),user.getName(),user.getAddress(),user.getIsLocal(),null);
        }else{
            userDto=new UserDto(user.getId(),user.getName(),user.getAddress(),user.getIsLocal(),convertToDto(user.getOnlineAccount()));
        }
        return userDto;
    }

    /**
     * convert a librarian object to the data transfer object of the librarian
     * @param librarian
     * @return the data transfer object of the input librarian
     */
    public static LibrarianDto convertToDto(Librarian librarian){
        if (librarian == null) {
            throw new IllegalArgumentException("There is no such librarian!");
        }
        LibrarianDto librarianDto=null;
        Set<BusinessHourDto> bhDtos=new HashSet<BusinessHourDto>();
        if(librarian.getBusinessHours()==null){
            librarianDto=new LibrarianDto(librarian.getId(),librarian.getName(),librarian.getAddress(),librarian.getIsHeadLibrarian(),null);
        }else{
            for(BusinessHour bh:librarian.getBusinessHours()){
                bhDtos.add(convertToDto(bh));
            }
            librarianDto=new LibrarianDto(librarian.getId(),librarian.getName(),librarian.getAddress(),librarian.getIsHeadLibrarian(),bhDtos);
        }
        return librarianDto;
    }

    /**
     * convert an itemReservation object to the data transfer object of the itemReservation
     * @param itemReservation
     * @return the data transfer object of the input itemReservation
     */
    public static ItemReservationDto convertToDto(ItemReservation itemReservation){
        if (itemReservation == null) {
            throw new IllegalArgumentException("There is no such itemReservation!");
        }
        ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),convertToDto(itemReservation.getPerson()),convertToDto(itemReservation.getItem()),convertToDto(itemReservation.getTimeSlot()));
        return itemReservationDto;
    }

    /**
     * convert an item object to the data transfer object of the item
     * @param item
     * @return the data transfer object of the input item
     */
    public static ItemDto convertToDto(Item item){
        if (item == null) {
            throw new IllegalArgumentException("There is no such Item!");
        }
        ItemDto itemDto = new ItemDto(item.getId(),item.getItemCategory(), item.getIsInLibrary(),item.getName());
        return itemDto;
    }

    /**
     * convert an event object to the data transfer object of the event
     * @param event
     * @return the data transfer object of the input event
     */
    public static EventDto convertToDto(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("There is no such event!");
        }
        EventDto eventDto = new EventDto(event.getId(),event.getName(),convertToDto(event.getTimeSlot()));
        return eventDto;
    }

    /**
     * convert a businessHour object to the data transfer object of the event
     * @param businessHour
     * @return the data transfer object of the input businessHour
     */
    public static BusinessHourDto convertToDto(BusinessHour businessHour) {
        if (businessHour == null) {
            throw new IllegalArgumentException("There is no such businessHour!");
        }
        BusinessHourDto businessHourDto = new BusinessHourDto(businessHour.getId(),businessHour.getDayOfWeek(),businessHour.getStartTime(),businessHour.getEndTime());
        return businessHourDto;
    }

    /**
     * convert a person object to the data transfer object of the person
     * @param person
     * @return the data transfer object of the input person
     */
    public static PersonDto convertToDto(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("There is no such Person!");
        }
        PersonDto personDto = new PersonDto(person.getId(), person.getName(),person.getAddress());
        return personDto;
    }

    /**
     * convert a timeSlot object to the data transfer object of the timeSlot
     * @param timeSlot
     * @return the data transfer object of the input timeSlot
     */
    public static TimeSlotDto convertToDto(TimeSlot timeSlot){
        if (timeSlot == null) {
            throw new IllegalArgumentException("There is no such timeslot!");
        }
        TimeSlotDto timeSlotDto=new TimeSlotDto(timeSlot.getId(),timeSlot.getStartDate(),timeSlot.getEndDate(),timeSlot.getStartTime(),timeSlot.getEndTime());
        return timeSlotDto;
    }







}
