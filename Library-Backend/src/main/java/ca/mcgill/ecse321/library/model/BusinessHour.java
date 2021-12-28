package ca.mcgill.ecse321.library.model;


import javax.persistence.*;
import java.sql.Time;

/**
 * the class illustrates the opening hour of library and the work time of librarians
 * the class contains the id of the business, the day of week(ex. Monday), and the start time and
 * end time of the day.
 */
@Entity
@Table(name = "business_hour")
public class BusinessHour
{
    @Column
    private int id;
    @Column
    private Time startTime;
    @Column
    private Time endTime;
    @Column
    private DayOfWeek dayOfWeek;
    public enum DayOfWeek { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
