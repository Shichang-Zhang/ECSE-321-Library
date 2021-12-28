package ca.mcgill.ecse321.library.servicetest;

import java.sql.Time;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.library.model.*;
import ca.mcgill.ecse321.library.service.BusinessHourService;
import ca.mcgill.ecse321.library.dao.*;

/**
 * unit test of the business hour service
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class BusinessHourServiceTest {
    @Mock
    private BusinessHourRepository businessHourRepository;
    @InjectMocks
    private BusinessHourService businessHourService;

    private static final int dayOfWeek = 2;
    private static final Time startTime = Time.valueOf(LocalTime.parse("09:00"));
    private static final Time endTime = Time.valueOf(LocalTime.parse("09:30"));
    private static final int invalidDayOfWeek = 7;
    private static final Time startTime2 = Time.valueOf(LocalTime.parse("09:00"));
    private static final Time endTime2 = Time.valueOf(LocalTime.parse("09:30"));

    @BeforeEach
    public void setMockOutput() {
        //        Set mock output: find business hour by an id
        lenient().when(businessHourRepository.findBusinessHourById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(dayOfWeek)) {
                BusinessHour businessHour = new BusinessHour();
                businessHour.setDayOfWeek(day(dayOfWeek));
                businessHour.setStartTime(startTime);
                businessHour.setEndTime(endTime);
                businessHour.setId(dayOfWeek);
                return businessHour;
            } else {
                return null;
            }
        });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(businessHourRepository.save(any(BusinessHour.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * The method tests the successful output of the createBusinessHour method
     */
    @Test
    public void testCreateBusinessHour() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        int testDayOfWeek = 3;
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.createBusinessHour(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(businessHour);
        assertEquals(day(testDayOfWeek), businessHour.getDayOfWeek());
        assertEquals(startTime, businessHour.getStartTime());
        assertEquals(endTime, businessHour.getEndTime());
    }

    /**
     * The method tests the output of creating business hour when there is a business hour on the selected day of week.
     */
    @Test
    public void alreadyExistBusinessHour() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        BusinessHour businessHour = null;

        try {
            businessHour = businessHourService.createBusinessHour(dayOfWeek, startTime2, endTime2);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("Businesshour on this day already exists!", error);
    }

    /**
     * The method tests the output of creating business hour when the start time is null.
     */
    @Test
    public void emptyStartTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 3;
        Time startTime = null;
        Time endTime = Time.valueOf(LocalTime.parse("09:30"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.createBusinessHour(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("start time cannot be empty!", error);
    }

    /**
     * The method tests the output of creating business hour when the endTime is null.
     */
    @Test
    public void emptyEndTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 3;
        Time endTime = null;
        Time startTime = Time.valueOf(LocalTime.parse("09:30"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.createBusinessHour(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("end time cannot be empty!", error);
    }

    /**
     * The method tests the output of creating business hour when the input endTime is before the input starttime.
     */
    @Test
    public void createEndTimeBeforeStartTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 3;
        Time endTime = Time.valueOf(LocalTime.parse("09:20"));
        Time startTime = Time.valueOf(LocalTime.parse("09:30"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.createBusinessHour(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("Timeslot end time cannot be before event start time!", error);
    }

    /**
     * The method tests the output of creating business hour when the input day of week is invalid.
     */
    @Test
    public void invalidDayOfWeek() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 10;
        Time startTime = Time.valueOf(LocalTime.parse("09:00"));
        Time endTime = Time.valueOf(LocalTime.parse("09:30"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.createBusinessHour(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("Invalid day of week!", error);
    }

    /**
     * The method tests the output of the updateBusinessHour method when there is no mistake.
     */
    @Test
    public void testUpdateBusinessHour() {
        BusinessHour businessHour = null;
        Time updateStartTime = Time.valueOf(LocalTime.parse("11:00"));
        Time updateEndTime = Time.valueOf(LocalTime.parse("11:30"));
        try {
            businessHour = businessHourService.updateBusinessHourTime(dayOfWeek, updateStartTime, updateEndTime);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(businessHour);
        assertEquals(day(dayOfWeek), businessHour.getDayOfWeek());
        assertEquals(updateStartTime, businessHour.getStartTime());
        assertEquals(updateEndTime, businessHour.getEndTime());
    }

    /**
     * The method tests the output of the updateBusinessHour method when the input day of week is invalid.
     */
    @Test
    public void invalidUpdateDayOfWeek() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 10;
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.updateBusinessHourTime(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("Invalid day of week!BusinessHour does not exist!", error);
    }

    /**
     * The method tests the output of the updateBusinessHour method when the input dayof week does not have a business hour.
     */
    @Test
    public void updateNoBusinessHourOnDay() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        int testDayOfWeek = 4;
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.updateBusinessHourTime(testDayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("BusinessHour does not exist!", error);
    }

    /**
     * The method tests the output of the updateBusinessHour method when the input starttime is null.
     */
    @Test
    public void updateEmptyStartTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        Time startTime = null;
        Time endTime = Time.valueOf(LocalTime.parse("09:30"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.updateBusinessHourTime(dayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("start time cannot be empty!", error);
    }

    /**
     * The method tests the output of the updateBusinessHour method when the input endTime is null.
     */
    @Test
    public void updateEmptyEndTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        Time startTime = Time.valueOf(LocalTime.parse("09:00"));
        Time endTime = null;
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.updateBusinessHourTime(dayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("end time cannot be empty!", error);
    }

    /**
     * The method tests the output of the updateBusinessHour method when the input endTime is before the starttime.
     */
    @Test
    public void updateEndTimeBeforeStartTime() {
        assertEquals(0, businessHourService.getAllBusinessHours().size());
        String error = null;
        Time startTime = Time.valueOf(LocalTime.parse("09:00"));
        Time endTime = Time.valueOf(LocalTime.parse("08:00"));
        BusinessHour businessHour = null;
        try {
            businessHour = businessHourService.updateBusinessHourTime(dayOfWeek, startTime, endTime);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(businessHour);
        assertEquals("Timeslot end time cannot be before event start time!", error);
    }

    /**
     * The method tests the getBusinessHour method.
     */
    @Test
    public void testGetExistingBusinessHour() {
        assertNotNull(businessHourService.getBusinessHour(dayOfWeek));
        assertEquals(businessHourRepository.findBusinessHourById(dayOfWeek).getId(), businessHourService.getBusinessHour(dayOfWeek).getId());
    }

    /**
     * The method tests the output of the getBusinessHour method when the input id is invalid.
     */
    @Test
    public void testGetNullBusinessHour() {
        assertNull(businessHourService.getBusinessHourByDayOfWeek(invalidDayOfWeek));
    }

    /**
     * Helper method　that returns the day of week for an input integer.
     *
     * @param dayOfWeek integer from 0-7
     * @return the day of week for an input integer.
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