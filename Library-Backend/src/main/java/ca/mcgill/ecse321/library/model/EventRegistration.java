package ca.mcgill.ecse321.library.model;
import javax.persistence.*;


/**
 * when a member of the library register an event, an event registration record will be stored in the library
 * the event registration record will contain its own id, the person and the registered event
 */
@Entity
public class EventRegistration {
    private int id;

    public void setId(int id) {
        this.id = id;
    }
    @Id
    public int getId() {
        return id;
    }

    private Person person;

    @ManyToOne(optional = false)
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    private Event event;

    @ManyToOne(optional = false)
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
