package ca.mcgill.ecse321.library.controller;
import ca.mcgill.ecse321.library.service.BusinessHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.dto.*;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform business hour database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/businessHours")
public class BusinessHourController {
    @Autowired
    BusinessHourService businessHourService;

    /**
     * get all business hour records in database and return the data transfer object for these businessHour records
     * @return a list of data transfer objects for existing businessHour records
     */
    @GetMapping("/businessHourList")
    public List<BusinessHourDto> getAllBusinessHours(){
        return businessHourService.getAllBusinessHours().stream().map(businessHour -> HelperMethods.convertToDto(businessHour)).collect(Collectors.toList());
    }

    /**
     * create business hour for a day
     * @param dayOfWeekIndex 1->Monday, 2->Tuesday ...
     * @param startTime
     * @param endTime
     * @return the transfer object of the created business hour
     * @throws IllegalArgumentException
     */
    @PostMapping("/createBusinessHour")
    public BusinessHourDto createBusinessHour(@RequestParam(name = "dayOfWeek") int dayOfWeekIndex,
                                              @RequestParam(name = "startTime") String startTime,
                                              @RequestParam(name = "endTime") String endTime)
            throws IllegalArgumentException {
        BusinessHour businessHour = businessHourService.createBusinessHour(dayOfWeekIndex,Time.valueOf(startTime),Time.valueOf(endTime));
        return HelperMethods.convertToDto(businessHour);
    }

    /**
     * get the business hour by business id
     * @param id
     * @return the transfer object of the business hour with the given id
     * @throws IllegalArgumentException
     */
    @GetMapping("/getBusinessHourById")
    public BusinessHourDto getBusinessHourById(@RequestParam(name = "id") int id)
            throws IllegalArgumentException {
        BusinessHour businessHour = businessHourService.getBusinessHour(id);
        return HelperMethods.convertToDto(businessHour);
    }

    /**
     * get business hour by day of week
     * @param dayOfWeek 1->Monday, 2->Tuesday ...
     * @return the transfer object of the business hour with the given day of week
     * @throws IllegalArgumentException
     */
    @GetMapping("/getBusinessHourByDayOfWeek")
    public BusinessHourDto getBusinessHourByDayOfWeek(@RequestParam(name = "dayOfWeek") int dayOfWeek)
            throws IllegalArgumentException {
        BusinessHour businessHour = businessHourService.getBusinessHourByDayOfWeek(dayOfWeek);
        return HelperMethods.convertToDto(businessHour);
    }

    /**
     * update the business hour
     * @param dayOfWeek 1->Monday, 2->Tuesday ...
     * @param startTime
     * @param endTime
     * @return the transfer object of the business hour with the update parameters
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateBusinessHourTime")
    public BusinessHourDto updateBusinessHourTime(@RequestParam(name = "dayOfWeek") int dayOfWeek,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime)
            throws IllegalArgumentException {
        BusinessHour businessHour = businessHourService.updateBusinessHourTime(dayOfWeek,Time.valueOf(startTime),Time.valueOf(endTime));
        return HelperMethods.convertToDto(businessHour);
    }

    @GetMapping("/getCurrentTime")
    public String getCurrentTime()
            throws IllegalArgumentException {
        return businessHourService.getSystemDateAndTime();
    }
}
