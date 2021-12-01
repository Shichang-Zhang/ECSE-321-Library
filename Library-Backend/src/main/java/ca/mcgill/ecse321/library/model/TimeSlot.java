package ca.mcgill.ecse321.library.model;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

/**
 * time slot, involving the start date, end date, start time, end time
 */
@Entity
@Table(name = "time_slot")
public class TimeSlot {

    //TimeSlot Attributes
    @Column
    private int id;
    @Column
    private Date startDate;
    @Column
    private Time startTime;
    @Column
    private Date endDate;
    @Column
    private Time endTime;

    public TimeSlot(){

    }

    @Id
    public int getId() {
        return id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Time getStartTime()
    {
        return startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Time getEndTime()
    {
        return endTime;
    }

}
