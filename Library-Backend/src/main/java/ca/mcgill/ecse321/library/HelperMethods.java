package ca.mcgill.ecse321.library;

import ca.mcgill.ecse321.library.model.BusinessHour;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * provide some helper methods to support the service layer
 */
public class HelperMethods {

    /**
     * transfer a iterable input to a list
     *
     * @param iterable iterable type input
     * @param <T>      data type in the iterable
     * @return the list form of the iterable
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * check whether the input startTime and endTime parameters are null
     *
     * @param startTime start time
     * @param endTime   end time
     * @return error message if parameters are null, otherwise an empty string
     */
    public static String checkStartTimeAndEndTime(Time startTime, Time endTime) {
        String error = "";
        if (startTime == null) {
            error = error + ("start time cannot be empty!");
        }
        if (endTime == null) {
            error = error + ("end time cannot be empty!");
        }
        return error;
    }

    /**
     * check whether the input startDate and endDate parameters are null
     *
     * @param startDate start time
     * @param endDate   end time
     * @return error message if parameters are null, otherwise an empty string
     */
    public static String checkStartDateAndEndDate(Date startDate, Date endDate) {
        String error = "";
        if (startDate == null) {
            error = error + ("Businesshour start time cannot be empty!");
        }
        if (endDate == null) {
            error = error + ("Businesshour end time cannot be empty!");
        }
        return error;
    }

    /**
     * transfer the int input to the day of week .
     * 1->Monday, 2->Tuesday, 3->Wednesday, 4->Thursday,5->Friday, 6->Saturday, 7-> Sunday
     *
     * @param dayOfWeek day of week
     * @return the DayOfWeek model
     */
    public static BusinessHour.DayOfWeek day(int dayOfWeek) {
        BusinessHour.DayOfWeek day = null;
        if (dayOfWeek == 1) {
            day = BusinessHour.DayOfWeek.Monday;
        } else if (dayOfWeek == 2) {
            day = BusinessHour.DayOfWeek.Tuesday;
        } else if (dayOfWeek == 3) {
            day = BusinessHour.DayOfWeek.Wednesday;
        } else if (dayOfWeek == 4) {
            day = BusinessHour.DayOfWeek.Thursday;
        } else if (dayOfWeek == 5) {
            day = BusinessHour.DayOfWeek.Friday;
        } else if (dayOfWeek == 6) {
            day = BusinessHour.DayOfWeek.Saturday;
        } else if (dayOfWeek == 7) {
            day = BusinessHour.DayOfWeek.Sunday;
        }

        return day;
    }

}
