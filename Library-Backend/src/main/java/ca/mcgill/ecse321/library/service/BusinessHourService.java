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
 * business hour service methods to manipulate data in backend, used in controller.
 */
@Service
public class BusinessHourService {
    @Autowired
    BusinessHourRepository businessHourRepository;
    @Autowired
    LibrarySystemRepository librarySystemRepository;

    /**
     * when the library is bulit, set the default business hour
     */
    @Transactional
    public void initBusinessHour() {
        this.createBusinessHour(1, Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        this.createBusinessHour(3, Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
    }

    /**
     * create a business hour
     *
     * @param dayOfWeek ex. 1->Monday, 2->Tuesday
     * @param startTime start time
     * @param endTime   end time
     * @return a business hour object
     */
    @Transactional
    public BusinessHour createBusinessHour(int dayOfWeek, Time startTime, Time endTime) {
        String error = "";
        if (businessHourRepository.findBusinessHourById(dayOfWeek) != null) {
            error = error + ("Businesshour on this day already exists!");
        }
        error = error + HelperMethods.checkStartTimeAndEndTime(startTime, endTime);
        if (endTime != null && startTime != null && endTime.before(startTime)) {
            error = error + "Timeslot end time cannot be before event start time!";
        }
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            error = error + ("Invalid day of week!");
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //create the business hour
        BusinessHour businessHour = new BusinessHour();
        businessHour.setId(dayOfWeek);
        BusinessHour.DayOfWeek day = HelperMethods.day(dayOfWeek);
        businessHour.setDayOfWeek(day);
        businessHour.setStartTime(startTime);
        businessHour.setEndTime(endTime);
        businessHourRepository.save(businessHour);
        return businessHour;
    }

    /**
     * get all business hours
     *
     * @return a list of business hours
     */
    @Transactional
    public List<BusinessHour> getAllBusinessHours() {
        return HelperMethods.toList(businessHourRepository.findAll());
    }

    /**
     * get business hour by id
     *
     * @param id business hour id
     * @return the business object with the input id, otherwise null
     */
    @Transactional
    public BusinessHour getBusinessHour(int id) {
        return businessHourRepository.findBusinessHourById(id);
    }

    /**
     * get business hour by day of week
     *
     * @param dayOfWeek ex. 1->Monday, 2->Tuesday
     * @return the business hour schedule at the day
     */
    @Transactional
    public BusinessHour getBusinessHourByDayOfWeek(int dayOfWeek) {
        return businessHourRepository.findBusinessHourById(dayOfWeek);
    }

    /**
     * update the business hour information
     *
     * @param dayOfWeek ex. 1->Monday, 2->Tuesday
     * @param startTime business hour start day
     * @param endTime end time
     * @return if fails, return original business hour, if successful, return update business hour
     */
    @Transactional
    public BusinessHour updateBusinessHourTime(int dayOfWeek, Time startTime, Time endTime) {
        String error = "";

        //day of week between 1-7
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            error = error + ("Invalid day of week!");
        }
        BusinessHour businessHour = businessHourRepository.findBusinessHourById(dayOfWeek);
        if (businessHour == null) {
            error = error + ("BusinessHour does not exist!");
        }
        error = error + HelperMethods.checkStartTimeAndEndTime(startTime, endTime);
        if (endTime != null && startTime != null && endTime.before(startTime)) {
            error = error + "Timeslot end time cannot be before event start time!";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }
        businessHour.setStartTime(startTime);
        businessHour.setEndTime(endTime);
        businessHourRepository.save(businessHour);
        return businessHour;
    }

    /**
     * Get current date and time of the system
     *
     * @return string of current date and time
     */
    @Transactional
    public String getSystemDateAndTime() {
        Date date = HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTDate();
        Time time = HelperMethods.toList(librarySystemRepository.findAll()).get(0).getCurrenTTime();
        return date.toString() + " " + time.toString();
    }


}
