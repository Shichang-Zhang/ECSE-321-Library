package ca.mcgill.ecse321.library.dto;


import ca.mcgill.ecse321.library.model.BusinessHour;

import java.sql.Time;

/**
 * data transfer object of business hour model.
 * have id, dayOfWeek, startTime, endTime fields.
 * have getter and setter methods for private fields.
 */
public class BusinessHourDto {
    private int id;
    private BusinessHour.DayOfWeek dayOfWeek;
    private Time startTime;
    private Time endTime;

    public BusinessHourDto(int id){
        this.id=id;
        this.dayOfWeek= BusinessHour.DayOfWeek.Monday;
        this.startTime=Time.valueOf("00:00:00");
        this.endTime=Time.valueOf("00:00:00");
    }

    public BusinessHourDto(int id, BusinessHour.DayOfWeek dayOfWeek, Time startTime, Time endTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BusinessHour.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(BusinessHour.DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
