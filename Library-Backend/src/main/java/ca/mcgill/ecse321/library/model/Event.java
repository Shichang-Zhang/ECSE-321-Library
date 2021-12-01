package ca.mcgill.ecse321.library.model;

import javax.persistence.*;

/**
 * event that can be registered by members and held in the library
 * it contains the event id, the name of the event, and the holding time indicated by a timeslot
 * @author Junjian Chen
 */

@Entity
@Table(name = "event")
public class Event
{
    @Column
    private int id;
    @Column
    private String name;

    //Event Associations
    @Column
    private TimeSlot timeSlot;


    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String aName)
    {
        name = aName;
    }

    public String getName()
    {
        return name;
    }


    @OneToOne(cascade = CascadeType.ALL)
    public TimeSlot getTimeSlot()
    {
        return timeSlot;
    }
    public void setTimeSlot(TimeSlot aNewTimeSlot)
    {
        this.timeSlot=aNewTimeSlot;
    }
}
