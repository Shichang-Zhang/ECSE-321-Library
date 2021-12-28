package ca.mcgill.ecse321.library.model;

import javax.persistence.*;
import java.util.*;

/**
 * the librarian who help the users and control the library.
 * the librarian has attribute indicating whether the librarian is a head librarian.
 * the librarian has a set of business hours that indicate the work time of librarians.
 */
@Entity
public class Librarian extends Person
{
    //Librarian Attributes
    private boolean isHeadLibrarian;

    //Librarian Associations
    private Set<BusinessHour> BusinessHours;

    public boolean getIsHeadLibrarian() {
        return isHeadLibrarian;
    }

    public void setIsHeadLibrarian(boolean aIsHeadLibrarian) {
        isHeadLibrarian = aIsHeadLibrarian;
    }

    public void setBusinessHours(Set<BusinessHour> businessHours) {
        this.BusinessHours = businessHours;
    }

    @OneToMany(cascade = { CascadeType.ALL },fetch = FetchType.EAGER)
    public Set<BusinessHour> getBusinessHours() {
        return BusinessHours;
    }
}