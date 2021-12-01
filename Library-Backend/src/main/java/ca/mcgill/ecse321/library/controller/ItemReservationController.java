package ca.mcgill.ecse321.library.controller;

import ca.mcgill.ecse321.library.dto.ItemDto;
import ca.mcgill.ecse321.library.dto.ItemReservationDto;
import ca.mcgill.ecse321.library.dto.PersonDto;
import ca.mcgill.ecse321.library.model.Item;
import ca.mcgill.ecse321.library.model.ItemReservation;
import ca.mcgill.ecse321.library.model.Person;
import ca.mcgill.ecse321.library.service.ItemReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform item reservation database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/itemReservations")
public class ItemReservationController {

    @Autowired
    private ItemReservationService itemReservationService;

    /**
     * get all item reservation records
     * @return a list of data transfer objects of the item reservation records
     */
    @GetMapping(value = {"/getItemReservationList","/getItemReservationList/"})
    public List<ItemReservationDto> getAllItemReservations(){
        List<ItemReservation> itemReservations= itemReservationService.getAllItemReservations();
        return itemReservations.stream().map(itemReservation->HelperMethods.convertToDto(itemReservation)).collect(Collectors.toList());
    }

    /**
     * a person wants to check out the item, call corresponding service method
     * @param pid
     * @param itemId
     * @param startDate check out start date
     * @param startTime check out start time
     * @return a data transfer object of updated item reservation record if successful
     */
    @PostMapping(value = {"/checkout","/checkout/"})
    public ItemReservationDto checkout(@RequestParam(name="pid") int pid,
                                       @RequestParam(name="itemId") int itemId,
                                       @RequestParam(name="startDate") String  startDate,
                                       @RequestParam(name="startTime") String startTime
                                       ) throws IllegalArgumentException{
        ItemReservation itemReservation = itemReservationService.checkOutItem(pid,itemId,Date.valueOf(startDate),Time.valueOf(startTime));
        ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),HelperMethods.convertToDto(itemReservation.getPerson()),HelperMethods.convertToDto(itemReservation.getItem()),HelperMethods.convertToDto(itemReservation.getTimeSlot()));
        return itemReservationDto;
    }

    /**
     * a person wants to reserve the item, call corresponding service method
     * @param pid
     * @param itemId
     * @param startDate
     * @param startTime
     * @param endDate
     * @param endTime
     * @return a data transfer object of item reservation record if successful
     */
    @PostMapping(value = {"/reserve","/reserve/"})
    public ItemReservationDto reserve (@RequestParam(name="pid") int pid,
                                       @RequestParam(name="itemId") int itemId,
                                       @RequestParam(name="startDate")String startDate,
                                       @RequestParam(name="startTime") String startTime,
                                       @RequestParam(name="endDate")String endDate,
                                       @RequestParam(name="endTime") String endTime
                                       ) throws IllegalArgumentException{
        ItemReservation itemReservation = itemReservationService.reserveItem(pid,itemId,Date.valueOf(startDate),Date.valueOf(endDate),Time.valueOf(startTime),Time.valueOf(endTime));
        ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),HelperMethods.convertToDto(itemReservation.getPerson()),HelperMethods.convertToDto(itemReservation.getItem()),HelperMethods.convertToDto(itemReservation.getTimeSlot()));
        return itemReservationDto;
    }

    /**
     * a person wants to renew the item, call corresponding service method
     * @param pid
     * @param itemId
     * @param endDate wanted extend date
     * @return a data transfer object of updated item reservation record if successful
     */
    @PutMapping(value = {"/renew","/renew/"})
    public ItemReservationDto renew(@RequestParam(name="pid") int pid,
                                    @RequestParam(name="itemId") int itemId,
                                    @RequestParam(name="itemReservationId") int itemReservationId,
                                    @RequestParam(name ="endDate") String endDate,
                                    @RequestParam(name = "endTime") String endTime) throws IllegalArgumentException{
        ItemReservation itemReservation = itemReservationService.renewItem(pid,itemId,itemReservationId,Date.valueOf(endDate),Time.valueOf(endTime));
        ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),HelperMethods.convertToDto(itemReservation.getPerson()),HelperMethods.convertToDto(itemReservation.getItem()),HelperMethods.convertToDto(itemReservation.getTimeSlot()));
        return itemReservationDto;
    }

    /**
     * a person wants to return the item, call corresponding service method
     * @param pid
     * @param itemId
     * @param itemReservationId
     * @param endDate return date
     * @param endTime return time
     * @return a data transfer object of updated item reservation record if successful
     */
    @PutMapping(value = {"/return","/return/"})
    public ItemReservationDto returnItem(@RequestParam(name="pid") int pid,
                                         @RequestParam(name="itemId") int itemId,
                                         @RequestParam(name="itemReservationId") int itemReservationId,
                                         @RequestParam(name ="endDate") Date endDate,
                                         @RequestParam(name ="endTime") Time endTime) throws IllegalArgumentException{
        ItemReservation itemReservation = itemReservationService.returnItem(pid,itemId,itemReservationId,endDate,endTime);
        ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),HelperMethods.convertToDto(itemReservation.getPerson()),HelperMethods.convertToDto(itemReservation.getItem()),HelperMethods.convertToDto(itemReservation.getTimeSlot()));
        return itemReservationDto;
    }

    /**
     * call service method to find all items checkout/reserved by the person
     * @param pid
     * @return a list of data transfer objects of items checkout/reserved by the person
     */
    @GetMapping(value = {"/getItemReservedByPerson","/getItemReservedByPerson/"})
    public List<ItemDto> getItemReservedByPerson (@RequestParam(name="pid") int pid)  throws IllegalArgumentException{
        List<Item> items = itemReservationService.getItemsReservedByPerson(pid);
        ArrayList<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items){
            ItemDto itemDto = new ItemDto(item.getId(),item.getItemCategory(),item.getIsInLibrary(),item.getName());
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    /**
     * call service method to find all people who checkout/reserve the item
     * @param itemId
     * @return a list of transfer objects of people who checkout/reserve the item
     */
    @GetMapping(value = {"/getPersonsReserveItem","/getPersonsReserveItem/"})
    public List<PersonDto> getPersonsReserveItem(@RequestParam(name="itemId") int itemId)  throws IllegalArgumentException {
        List<Person> personList = itemReservationService.getPersonsReserveItem(itemId);
        ArrayList<PersonDto> personDtos = new ArrayList<>();
        for (Person person : personList){
            PersonDto personDto = new PersonDto(person.getId(),person.getName(),person.getAddress());
            personDtos.add(personDto);
        }
        return personDtos;
    }

    /**
     * call service method to find all item reservation done by the person to the item
     * @param pid
     * @param itemId
     * @return a list of item reservations done by the person to the item
     */
    @GetMapping(value = {"/getItemReservationsByPersonAndItem","/getItemReservationsByPersonAndItem/"})
    public List<ItemReservationDto> getItemReservationsByPersonAndItem(@RequestParam(name="pid") int pid, @RequestParam(name="itemId") int itemId)  throws IllegalArgumentException{
        List<ItemReservation> itemReservations = itemReservationService.getItemReservationByPersonAndItem(pid,itemId);
        ArrayList<ItemReservationDto> itemReservationDtos = new ArrayList<>();
        for (ItemReservation itemReservation:itemReservations){
            ItemReservationDto itemReservationDto = new ItemReservationDto(itemReservation.getId(),HelperMethods.convertToDto(itemReservation.getPerson()),HelperMethods.convertToDto(itemReservation.getItem()),HelperMethods.convertToDto(itemReservation.getTimeSlot()));
            itemReservationDtos.add(itemReservationDto);
        }
        return itemReservationDtos;
    }

    /**
     * a person wants to cancel the reservation, call corresponding service method
     * @param pid
     * @param itemId
     * @param itemReservationId
     * @return a flag indication whether it is successful. True means successfully cancelled.
     */
    @DeleteMapping(value = {"/cancelReservation","/cancelReservation/"})
    public boolean cancelReservation (@RequestParam(name="pid") int pid,
                                      @RequestParam(name="itemId") int itemId,
                                      @RequestParam(name="itemReservationId") int itemReservationId) throws IllegalArgumentException{
        boolean flag=false;
        try{
            itemReservationService.deleteItemReservation(pid,itemId,itemReservationId);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

}
