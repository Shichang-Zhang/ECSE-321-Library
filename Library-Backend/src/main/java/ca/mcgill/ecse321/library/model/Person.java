package ca.mcgill.ecse321.library.model;

import javax.persistence.*;

/**
 * person involves the librarian and the user.
 * person contains a unique id, a name and an address.
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Person {

    //Person Attributes
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String address;


    public void setName(String aName) {
        name = aName;
    }

    public void setAddress(String aAddress) {
        address = aAddress;
    }

    public void setId(int aId) {
        id = aId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Id
    public int getId() {
        return id;
    }

}