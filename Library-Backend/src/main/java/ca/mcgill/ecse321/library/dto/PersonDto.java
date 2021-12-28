package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of person model.
 * have id, name, address fields.
 * have getter and setter methods for private fields.
 */
public class PersonDto {

    private int id;
    private String name;
    private String address;

    public PersonDto(int id) {
        this.id = id;
        this.name="NoNamePerson";
        this.address="NoNameAddress";
    }

    public PersonDto(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
