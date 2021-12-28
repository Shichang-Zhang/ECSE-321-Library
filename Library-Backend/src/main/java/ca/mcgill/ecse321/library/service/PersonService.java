package ca.mcgill.ecse321.library.service;

import ca.mcgill.ecse321.library.HelperMethods;
import ca.mcgill.ecse321.library.dao.PersonRepository;
import ca.mcgill.ecse321.library.model.Librarian;
import ca.mcgill.ecse321.library.model.Person;
import ca.mcgill.ecse321.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * person service methods to manipulate data in backend, used in controller.
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * create a member for the library
     *
     * @param name     person's name
     * @param address  person's address
     * @param userRole Librarian or User, indicating the member's position
     * @return the created person with the input information
     */
    @Transactional
    public Person createPerson(String name, String address, String userRole) {
        String error = "";
        //check the input parameters
        if (name == null || name.trim().length() == 0) {
            error = error + ("Person name cannot be empty!");
        }
        if (address == null || address.trim().length() == 0) {
            error = error + ("Person address cannot be empty!");
        }
        if (userRole == null || !userRole.equals("Librarian") && !userRole.equals("User")) {
            error = error + ("Invalid Userrole input, please input Librarian or User!");
        }

        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //create the person
        Person person;
        if (userRole.equals("Librarian")) {
            person = new Librarian();
        } else {
            person = new User();
        }
        person.setId((person.hashCode() + name.hashCode()) * (address.hashCode() - userRole.hashCode()));
        person.setName(name);
        person.setAddress(address);

        personRepository.save(person);

        return person;
    }

    /**
     * update the member's address.
     *
     * @param pid     person id
     * @param address person's address
     * @return the person object with the update address
     */
    @Transactional
    public Person updateAddress(int pid, String address) {
        //check the input
        if (address == null || address.trim().length() == 0) {
            throw new IllegalArgumentException("address cannot be empty!");
        }
        Person person = getPersonById(pid);
        if (person == null) {
            throw new IllegalArgumentException("operator must be a user or librarian");
        }
        //update the address
        person.setAddress(address);
        personRepository.save(person);

        return person;
    }

    /**
     * get person by id
     *
     * @param pid person id
     * @return the person with the input id
     */
    @Transactional
    public Person getPersonById(int pid) {
        return personRepository.findPersonById(pid);
    }


    /**
     * get all members of the library system
     *
     * @return a list of people
     */
    @Transactional
    public List<Person> getAllPerson() {
        return HelperMethods.toList(personRepository.findAll());
    }

}
