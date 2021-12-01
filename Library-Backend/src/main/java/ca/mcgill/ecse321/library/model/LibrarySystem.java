
package ca.mcgill.ecse321.library.model;

import javax.persistence.*;
import java.util.*;
import java.sql.Time;
import java.sql.Date;

/**
 * library system containing the users and facilities
 */
@Entity
@Table(name = "library_system")
public class LibrarySystem {

  private Date currenTDate;

  private Time currenTTime;

  private int id;

  public void setId(int id) {
    this.id = id;
  }
  @Id
  public int getId() {
    return id;
  }

  public Date getCurrenTDate() {
    this.setCurrenTDate(new Date(System.currentTimeMillis()));
    return currenTDate;
  }

  public void setCurrenTDate(Date currenTDate) {
    this.currenTDate = currenTDate;
  }

  public Time getCurrenTTime() {
    this.setCurrenTTime(new Time(System.currentTimeMillis()));
    return currenTTime;
  }

  public void setCurrenTTime(Time currenTTime) {
    this.currenTTime = currenTTime;
  }

  private Set<BusinessHour> businessHours;

  public void setBusinessHours(Set<BusinessHour> businessHours) {
    this.businessHours = businessHours;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<BusinessHour> getBusinessHours() {
    return businessHours;
  }

  private Set<Event> events;

  public void setEvents(Set<Event> events) {
    this.events = events;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<Event> getEvents() {
    return events;
  }

  private Set<EventRegistration> eventRegistrations;

  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<EventRegistration> getEventRegistrations() {
    return eventRegistrations;
  }

  public void setEventRegistrations(Set<EventRegistration> eventRegistrations) {
    this.eventRegistrations = eventRegistrations;
  }

  private Set<ItemReservation> itemReservations;
  public void setItemReservations(Set<ItemReservation> itemReservations) {
    this.itemReservations = itemReservations;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<ItemReservation> getItemReservations() {
    return itemReservations;
  }


  private Set<Item> items;
  public void setItems(Set<Item> items) {
    this.items = items;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<Item> getItems() {
    return items;
  }

  private Set<Librarian> librarians;

  public void setLibrarians(Set<Librarian> librarians) {
    this.librarians = librarians;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<Librarian> getLibrarians() {
    return librarians;
  }

  private Set<OnlineAccount> onlineAccounts;

  public void setOnlineAccounts(Set<OnlineAccount> onlineAccounts) {
    this.onlineAccounts = onlineAccounts;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<OnlineAccount> getOnlineAccounts() {
    return onlineAccounts;
  }

  private Set<Person> persons;

  public void setPersons(Set<Person> persons) {
    this.persons = persons;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<Person> getPersons() {
    return persons;
  }

  private Set<TimeSlot> timeSlots;

  public void setTimeSlots(Set<TimeSlot> timeSlots) {
    this.timeSlots = timeSlots;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<TimeSlot> getTimeSlots() {
    return timeSlots;
  }

  private Set<User> users;

  public void setUsers(Set<User> users) {
    this.users = users;
  }
  @OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
  public Set<User> getUsers() {
    return users;
  }


}

