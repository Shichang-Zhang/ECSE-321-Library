package ca.mcgill.ecse321.library.dto;

import java.sql.Date;
import java.sql.Time;

/**
 * data transfer object of timeslot model, indicating a period of time.
 * have startTime, endTime, startDate, endDate fields.
 * have the id of the time slot.
 * have getter and setter methods for private fields.
 */
public class TimeSlotDto {
    private int id;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;

    public TimeSlotDto(int id){
        this.id=id;
        this.startDate=Date.valueOf("1971-01-01");
        this.endDate=Date.valueOf("1971-01-01");
        this.startTime=Time.valueOf("00:00:00");
        this.endTime=Time.valueOf("00:00:00");
    }

    public TimeSlotDto(int id, Date startDate, Date endDate, Time startTime, Time endTime) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
