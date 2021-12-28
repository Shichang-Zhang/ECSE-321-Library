package ca.mcgill.ecse321.library.dto;

import java.util.Set;

/**
 * data transfer object of librarian model.
 * extends the person DTO so it can also access the name, address and id of the librarian.
 * has the field isHeadLibrarian showing whether the librarian is head librarian.
 * contains the business hour DTO for librarian work time.
 * have getter and setter methods for private fields.
 */
public class LibrarianDto extends PersonDto{
    private boolean isHeadLibrarian;
    private Set<BusinessHourDto> businessHourDtos;

    public LibrarianDto(int id) {
        super(id);
    }

    public LibrarianDto(int id, String name, String address) {
        super(id, name, address);
    }

    public LibrarianDto(int id, boolean isHeadLibrarian, Set<BusinessHourDto> businessHourDtos) {
        super(id);
        this.isHeadLibrarian = isHeadLibrarian;
        this.businessHourDtos = businessHourDtos;
    }

    public LibrarianDto(int id, String name, String address, boolean isHeadLibrarian, Set<BusinessHourDto> businessHourDtos) {
        super(id, name, address);
        this.isHeadLibrarian = isHeadLibrarian;
        this.businessHourDtos = businessHourDtos;
    }

    public boolean isHeadLibrarian() {
        return isHeadLibrarian;
    }

    public void setHeadLibrarian(boolean headLibrarian) {
        isHeadLibrarian = headLibrarian;
    }

    public Set<BusinessHourDto> getBusinessHourDtos() {
        return businessHourDtos;
    }

    public void setBusinessHourDtos(Set<BusinessHourDto> businessHourDtos) {
        this.businessHourDtos = businessHourDtos;
    }
}
