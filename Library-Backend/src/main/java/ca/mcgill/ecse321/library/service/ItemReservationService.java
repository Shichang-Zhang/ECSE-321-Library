package ca.mcgill.ecse321.library.service;

import ca.mcgill.ecse321.library.HelperMethods;
import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * item reservation service methods to manipulate data in backend, used in controller.
 * The class mainly contains the checkout item, reserve item, renew item and return item services.
 */
@Service
public class ItemReservationService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemReservationRepository itemReservationRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private LibrarySystemRepository librarySystemRepository;
    //although these two variables are shown as unused, but without the variable, the test will fail
    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * a person wants to check out an item at a given time for 14 days long
     *
     * @param pid       person id
     * @param itemId    item id
     * @param startDate start date
     * @param startTime start time
     * @return an item reservation record if successful
     */
    @Transactional
    public ItemReservation checkOutItem(int pid, int itemId, Date startDate, Time startTime) throws IllegalArgumentException {
        String error = "";
        //check input person and item existence
        if (!personRepository.existsById(pid)) {
            throw new IllegalArgumentException("Person does not exist!");
        }
        Person person = personRepository.findPersonById(pid);

        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("item does not exist");
        }
        Item item = itemRepository.findItemById(itemId);

        if (!item.getIsInLibrary()) {
            error = error + "Item is not in library!";
        } else if (item.getItemCategory() == Item.ItemCategory.Newspaper) {
            error = error + "Newspaper cannot be checked out!";
        } else if (item.getItemCategory() == Item.ItemCategory.Archive) {
            error = error + "Archive cannot be checked out!";
        }

        //add 14 days to get the end date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 14);
        Date endDate = new Date(calendar.getTime().getTime());
        //change to compare to!!!!!
        if (startDate.compareTo(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate()) < 0) {
            if (!HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate().toString().equals(startDate.toString())) {
                throw new IllegalArgumentException("can not register an item reservation with past time");
            }
        }

        // in this condition, the end time is same as the start time, but the date is 14 days later.
        if (this.checkItemReserveStatus(item, startDate, endDate, startTime, startTime)) {
            error = error + "item is reserved in this period";
        }

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        // in this condition, the end time is same as the start time, but the date is 14 days later.
        TimeSlot timeSlot = createTimeSlot(startDate, endDate, startTime, startTime);

        item.setIsInLibrary(false);
        itemRepository.save(item);

        ItemReservation itemReservation = new ItemReservation();
        itemReservation.setId(itemReservation.hashCode() * person.hashCode() + item.hashCode());
        itemReservation.setPerson(person);
        itemReservation.setItem(item);
        itemReservation.setTimeSlot(timeSlot);

        itemReservationRepository.save(itemReservation);

        return itemReservation;
    }

    /**
     * a person wants to reserve the item at a given timeslot
     *
     * @param pid       person id
     * @param itemId    item id
     * @param startDate start date
     * @param endDate   end date
     * @param startTime start time
     * @param endTime   end time
     * @return item reservation record if successful
     */
    @Transactional
    public ItemReservation reserveItem(int pid, int itemId, Date startDate, Date endDate, Time startTime, Time endTime) {
        String error = "";
        //check input person id and item id existence
        if (!personRepository.existsById(pid)) {
            throw new IllegalArgumentException("Person does not exist!");
        }
        Person person = personRepository.findPersonById(pid);

        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("item does not exist");
        }
        Item item = itemRepository.findItemById(itemId);

        if (item.getItemCategory() == Item.ItemCategory.Newspaper) {
            error = error + "Newspaper cannot be reserved to checkout!";
        } else if (item.getItemCategory() == Item.ItemCategory.Archive) {
            error = error + "Archive cannot be reserved to checkout!";
        }

        //check input time format
        if (startDate.before(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate())) {
            throw new IllegalArgumentException("can not register an item reservation with past time");
        }
        if (endDate != null && startDate != null && endDate.before(startDate)) {
            throw new IllegalArgumentException("Timeslot end date cannot be before reservation start date!");
        }
        if (endTime != null && startTime != null && endTime.before(startTime)) {
            throw new IllegalArgumentException("Timeslot end time cannot be before reservation start time!");
        }
        if (this.checkItemReserveStatus(item, startDate, endDate, startTime, endTime)) {
            error = error + "item is reserved in this period";
        }
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        //reserve the item
        TimeSlot timeSlot = createTimeSlot(startDate, endDate, startTime, endTime);
        ItemReservation itemReservation = new ItemReservation();
        itemReservation.setId(itemReservation.hashCode() * person.hashCode() + item.hashCode());
        itemReservation.setPerson(person);
        itemReservation.setItem(item);
        itemReservation.setTimeSlot(timeSlot);
        item.setIsReserved(true);
        itemReservationRepository.save(itemReservation);

        return itemReservation;
    }

    /**
     * a person wants to renew the item that he/she has checked out
     *
     * @param pid               person id
     * @param itemId            item id
     * @param itemReservationId item reservation id
     * @param endDate           renew extended end date
     * @param endTime           renew extended end time
     * @return the renew record if it is successful, or reservation record unchanged if fails.
     */
    @Transactional
    public ItemReservation renewItem(int pid, int itemId, int itemReservationId, Date endDate, Time endTime) {
        String error = "";
        //check the input person and item id adn item reservation id existence
        if (!personRepository.existsById(pid)) {
            throw new IllegalArgumentException("Person does not exist!");
        }
        Person person = personRepository.findPersonById(pid);
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("item does not exist!");
        }
        Item item = itemRepository.findItemById(itemId);

        if (!itemReservationRepository.existsById(itemReservationId)) {
            throw new IllegalArgumentException("ItemReservation does not exist");
        }
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(itemReservationId);
        if (itemReservation.getItem().getId() != itemId) {
            throw new IllegalArgumentException("Item id does not match!");
        }
        if (itemReservation.getPerson().getId() != pid) {
            throw new IllegalArgumentException("Person id does not match!");
        }
        if (item.getIsInLibrary()) {
            error = error + "Item is not checked out!";
        }

        //check whether the renew time is in the past
        error = error + checkEventTimeIsInPast(endDate, endTime);

        //current time slot
        TimeSlot timeSlot = itemReservation.getTimeSlot();
        if (timeSlot.getStartDate().after(endDate)) {
            error = error + "invalid renew date";
        } else if (timeSlot.getEndDate().after(endDate)) {
            error = error + "you have sufficient check out period, no need to renew";
        }

        // check whether the item will be reserved at the input renew time
        List<ItemReservation> itemReservations = itemReservationRepository.findByItem(item);
        for (ItemReservation itemReservation1 : itemReservations) {
            if (!itemReservation1.equals(itemReservation)) {
                TimeSlot timeSlot1 = itemReservation1.getTimeSlot();
                // startDate < input date < endDate, not feasible
                if (endDate.compareTo(timeSlot1.getStartDate()) > 0 && endDate.compareTo(timeSlot1.getEndDate()) <= 0) {
                    error = error + "item will be reserved at the input date, choose another renew time";
                } else if (endDate.compareTo(timeSlot1.getStartDate()) == 0) {
                    // at the same day, endTime > next startTime, not feasible
                    if (endTime.after(timeSlot1.getStartTime())) {
                        error = error + "item will be reserved at the input date, choose another renew time";
                    }
                }
            }
        }

        //get original end date +14 days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(itemReservation.getTimeSlot().getEndDate());
        calendar.add(Calendar.DATE, 14);
        Date secondEndDate = new Date(calendar.getTime().getTime());
        //renew no more than 14 days after the original end date
        if (endDate.after(secondEndDate)) {
            error = error + ("The new end date can only be 14 days after the origin checkout timeslot") + secondEndDate.toString() + " " + endDate.toString();
        }

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //renew
        timeSlot.setEndDate(endDate);
        timeSlotRepository.save(timeSlot);
        itemReservation.setTimeSlot(timeSlot);
        itemReservationRepository.save(itemReservation);

        return itemReservation;
    }

    /**
     * a person wants to return the item that he/she has checked out
     *
     * @param pid               the person id
     * @param itemId            item id
     * @param itemReservationId item reservation id
     * @param endDate           the return date
     * @param endTime           the return time
     * @return item reservation with return record
     */
    @Transactional
    public ItemReservation returnItem(int pid, int itemId, int itemReservationId, Date endDate, Time endTime) {
        String error = "";
        //check whether the input ids is valid
        if (!personRepository.existsById(pid)) {
            throw new IllegalArgumentException("Person does not exist!");
        }
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("item does not exist");
        }
        Item item = itemRepository.findItemById(itemId);
        if (!itemReservationRepository.existsById(itemReservationId)) {
            throw new IllegalArgumentException("ItemReservation does not exist");
        }
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(itemReservationId);

        //check whether the input item reservation is persistence with the input item and person
        if (itemReservation.getItem().getId() != itemId) throw new IllegalArgumentException("Item id does not match!");
        if (itemReservation.getPerson().getId() != pid) throw new IllegalArgumentException("Person id does not match!");

        if (item.getIsInLibrary()) {
            error = error + "Item is not checked out!";
        }
        //check whether the renew time is in the past
        error = error + checkEventTimeIsInPast(endDate, endTime);

        //current time slot
        TimeSlot timeSlot = itemReservation.getTimeSlot();
        if (!compareDateString(timeSlot.getStartDate(), endDate) && timeSlot.getStartDate().after(endDate)) {
            error = error + "invalid return date";
        }

        //return the item but exceed the end date
        String warning = "";
        //exceed the 14 days limit
        if (timeSlot.getEndDate().before(endDate)) {
            warning = warning + "not return on time, need penalty";
        } else if (timeSlot.getEndDate().compareTo(endDate) == 0) {
            //at same day, but endtime exceed
            if (timeSlot.getEndTime().before(endTime)) {
                warning = warning + "not return on time, need penalty";
            }
        }
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        //return
        timeSlot.setEndDate(endDate);
        timeSlot.setEndTime(endTime);
        timeSlotRepository.save(timeSlot);
        item.setIsInLibrary(true);
        itemRepository.save(item);
        itemReservation.setTimeSlot(timeSlot);
        itemReservation.setItem(item);

        itemReservationRepository.save(itemReservation);
        warning = warning.trim();
        if (!(warning.length() <= 0)) throw new IllegalArgumentException(warning);

        return itemReservation;
    }

    /**
     * get all the items that have reserved by the person, both in the past and now
     *
     * @param pid person id
     * @return a list of all items reserved by the person
     */
    @Transactional
    public List<Item> getItemsReservedByPerson(int pid) {
        Person person = personRepository.findPersonById(pid);
        if (person == null) {
            throw new IllegalArgumentException("person not exist!");
        }
        List<Item> itemReservedByPerson = new ArrayList<>();
        for (ItemReservation ir : itemReservationRepository.findByPerson(person)) {
            itemReservedByPerson.add(ir.getItem());
        }
        return itemReservedByPerson;
    }

    /**
     * get all people who have reserved the item, both in the past and now
     *
     * @param itemId item id
     * @return a list of all people reserving the item
     */
    @Transactional
    public List<Person> getPersonsReserveItem(int itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("item not exist!");
        }
        List<Person> personsReserveTheItem = new ArrayList<>();
        for (ItemReservation ir : itemReservationRepository.findByItem(item)) {
            personsReserveTheItem.add(ir.getPerson());
        }
        return personsReserveTheItem;
    }

    /**
     * a person wants to candle a reservation
     *
     * @param pid               person id
     * @param itemId            item id
     * @param itemReservationId itemReservation id
     */
    @Transactional
    public void deleteItemReservation(int pid, int itemId, int itemReservationId) {
        //check input ids
        if (!personRepository.existsById(pid)) {
            throw new IllegalArgumentException("Person does not exist!");
        }
        Person person = personRepository.findPersonById(pid);
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("item does not exist");
        }
        Item item = itemRepository.findItemById(itemId);
        if (!itemReservationRepository.existsById(itemReservationId)) {
            throw new IllegalArgumentException("ItemReservation does not exist");
        }
        ItemReservation itemReservation = itemReservationRepository.findItemReservationById(itemReservationId);
        if (itemReservation.getItem().getId() != itemId) {
            throw new IllegalArgumentException("Item id does not match!");
        }
        if (itemReservation.getPerson().getId() != pid) {
            throw new IllegalArgumentException("Person id does not match!");
        }
        itemReservationRepository.deleteById(itemReservationId);
    }

    /**
     * find reservation record by person and item id
     *
     * @param pid    person id
     * @param itemId item id
     * @return a list of reservations that were done by the person with the item
     */
    @Transactional
    public List<ItemReservation> getItemReservationByPersonAndItem(int pid, int itemId) {
        Person person = personRepository.findPersonById(pid);
        String error = "";
        if (person == null) {
            error = error + ("person does not exist");
        }

        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            error = error + ("item does not exist");
        }

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        List<ItemReservation> a = itemReservationRepository.findByPersonAndItem(person, item);
        return itemReservationRepository.findByPersonAndItem(person, item);
    }

    /**
     * find all item reservation records
     *
     * @return a list of existing item reservation records
     */
    @Transactional
    public List<ItemReservation> getAllItemReservations() {
        return HelperMethods.toList(itemReservationRepository.findAll());
    }

    /**
     * create the timeslot with the input time fields
     *
     * @param startDate start date
     * @param endDate   end date
     * @param startTime start time
     * @param endTime   end time
     * @return timeslot created
     */
    @Transactional
    public TimeSlot createTimeSlot(Date startDate, Date endDate, Time startTime, Time endTime) {
        String error = "";
        //check whether they are null
        if (endDate == null) {
            error = error + "Timeslot end date cannot be empty! ";
        }
        if (startDate == null) {
            error = error + "Timeslot start date cannot be empty! ";
        }
        if (startTime == null) {
            error = error + "Timeslot start time cannot be empty! ";
        }
        if (endTime == null) {
            error = error + "Timeslot end time cannot be empty! ";
        }
        if (endDate != null && startDate != null && endDate.before(startDate)) {
            error = error + "Timeslot end date cannot be before event start date!";
        }
        if (startDate != null && endDate != null && startDate.compareTo(endDate) == 0) {
            if (endTime != null && startTime != null && endTime.before(startTime)) {
                error = error + "Timeslot end time cannot be before event start time!";
            }
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //create the time slot
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(timeSlot.hashCode() * startTime.hashCode());
        timeSlot.setStartDate(startDate);
        timeSlot.setEndDate(endDate);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlotRepository.save(timeSlot);
        return timeSlot;
    }

    /**
     * given a list of item reservations done by same person with same item, find the item reservation that matches the time slot infomation
     *
     * @param itemReservations a list of item reservations done by same person with same item
     * @param startDate        start date
     * @param endDate          end date
     * @param startTime        start time
     * @param endTime          end time
     * @return the found item reservation if successful
     */
    private ItemReservation findItemReservationByTimeSlot(List<ItemReservation> itemReservations, Date startDate, Date endDate, Time startTime, Time endTime) {
        for (ItemReservation itemReservation : itemReservations) {
            TimeSlot timeSlot = itemReservation.getTimeSlot();
            //find the item reservation at the certain time slot
            if (timeSlot.getStartDate().compareTo(startDate) == 0 &&
                    timeSlot.getEndDate().compareTo(endDate) == 0 &&
                    timeSlot.getStartTime().compareTo(startTime) == 0 &&
                    timeSlot.getEndDate().compareTo(endTime) == 0) {
                return itemReservation;
            }
        }
        return null;
    }

    /**
     * check whether the item is reserved at this time slot
     *
     * @param item      item
     * @param startDate reservation start date
     * @param endDate   reservation end date
     * @param startTime reservation start time
     * @param endTime   reservation end time
     * @return if the item is reserved at the given time, return true, otherwise false
     */
    private boolean checkItemReserveStatus(Item item, Date startDate, Date endDate, Time startTime, Time endTime) throws IllegalArgumentException {
        List<ItemReservation> itemReservationList = itemReservationRepository.findByItem(item);
        for (ItemReservation itemReservation : itemReservationList) {
            TimeSlot t = itemReservation.getTimeSlot();
            //Check if the  start date we want to borrow/reserve is in the existing timeslot
            if (!compareDateString(t.getStartDate(), startDate) && !compareDateString(t.getEndDate(), startDate)) {
                if (t.getStartDate().compareTo(startDate) < 0 && startDate.compareTo(t.getEndDate()) < 0) {
                    return true;
                }
            }
            //Check if the end date we want to borrow/reserve is in the existing timeslot
            if (!compareDateString(t.getStartDate(), endDate) && !compareDateString(t.getEndDate(), endDate)) {
                if (t.getStartDate().compareTo(endDate) < 0 && endDate.compareTo(t.getEndDate()) < 0) {
                    return true;
                }
            }

            //Check if the existing timeslot start date is in  the timeslot we want to borrow/reserve
            if (!compareDateString(t.getStartDate(), startDate) && !compareDateString(t.getStartDate(), endDate)) {
                if (startDate.compareTo(t.getStartDate()) < 0 && t.getStartDate().compareTo(endDate) < 0) {
                    return true;
                }
            }

            //Check if the existing timeslot end date is in  the timeslot we want to borrow/reserve
            if (!compareDateString(t.getEndDate(), startDate) && !compareDateString(t.getEndDate(), endDate)) {
                if (startDate.compareTo(t.getEndDate()) < 0 && t.getEndDate().compareTo(endDate) < 0) {
                    return true;
                }
            }

            //When the existing timeslot start date and the start date we want to borrow/reserve are the same
            //check start/end time overlap
            if (compareDateString(startDate, t.getEndDate()) && !compareDateString(t.getEndDate(), t.getStartDate())) {
                if (compareTimeString(startTime, t.getEndTime()) < 0) {
                    return true;
                }
            }

            //When the existing timeslot end date and the end date we want to borrow/reserve are the same
            //check start/end time overlap
            if (compareDateString(endDate, t.getStartDate()) && !compareDateString(t.getStartDate(), t.getEndDate())) {
                if (compareTimeString(endTime, t.getStartTime()) > 0) {
                    return true;
                }
            }

            if (compareDateString(t.getStartDate(), t.getEndDate())) {
                if (compareDateString(startDate, endDate)) {
                    if (compareTimeString(t.getStartTime(), startTime) <= 0 && compareTimeString(startTime, t.getEndTime()) <= 0) {
                        return true;
                    }
                    if (compareTimeString(t.getStartTime(), endTime) <= 0 && compareTimeString(endTime, t.getEndTime()) <= 0) {
                        return true;
                    }
                }
            }

            if (compareDateString(t.getStartDate(), startDate)) {
                if (compareDateString(t.getEndDate(), endDate)) {
                    if (compareTimeString(t.getStartTime(), startTime) <= 0 || compareTimeString(t.getEndTime(), endTime) >= 0) {
                        return true;
                    }
                }
            }


        }
        return false;
    }

    /**
     * check whether the input date is in the past. Cannot record the reservation/checkout time to be in the past
     *
     * @param endDate event end date
     * @param endTime event end time
     * @return empty message if input date is not in the past, otherwise an error message.
     */
    private String checkEventTimeIsInPast(Date endDate, Time endTime) {
        String error = "";
        if (!compareDateString(endDate, HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate())) {
            if (endDate.before(HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate())) {
                error = error + "can not reserve/checkout with past time";
            }

        } else {
            if (compareTimeString(endTime, HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTTime()) < 0) {
                error = error + "can not reserve/checkout with past time";
            }
        }
        return error;
    }

    /**
     * Check if two dates are the same by comparing String
     *
     * @param date1 date1 in string format (yyyy-mm-dd)
     * @param date2 date2 in string format (yyyy-mm-dd)
     * @return if two date string are equal, return true, otherwise false
     */
    private boolean compareDateString(Date date1, Date date2) {
        return date1.toString().equals(date2.toString());
    }

    /**
     * Check if two time are the same by comparing String
     *
     * @param time1 time1 in the string format (hh:mm:ss)
     * @param time2 time2 in the string format (hh:mm:ss)
     * @return if two time string are equal, return true, otherwise false
     */
    private int compareTimeString(Time time1, Time time2) {
        return time1.toString().compareTo(time2.toString());
    }
}
