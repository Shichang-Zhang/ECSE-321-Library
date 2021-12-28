package ca.mcgill.ecse321.library.model;
import javax.persistence.*;

/**
 * item reservation record is generated when a person reserve/checkout an item.
 * item reservations have their own id.
 * item reservations records the person who wants to reserve/checkout an item and the item will be reserved/checkout.
 * item reservations also contain the reserve/checkout start time and expected return time, by using a timeslot.
 */
@Entity
public class ItemReservation {
    private int id;

    public void setId(int id) {
        this.id = id;
    }
    @Id
    public int getId() {
        return id;
    }
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }
    @ManyToOne(optional = false)
    public Person getPerson() {
        return person;
    }

    private Item item;

    @ManyToOne(optional = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Column(nullable = false)
    private TimeSlot timeSlot;

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
