package ca.mcgill.ecse321.library.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.library.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.library.dao.*;
import ca.mcgill.ecse321.library.model.*;


/**
 * librarian service methods to manipulate data in backend, used in controller.
 * librarian service extends person service, so librarian can have access the person service methods.
 * The class involves some behaviors that can only be done by head librarian : create librarian,
 * assign librarian business hours, remove librarian business hour, set library opening time, fire librarian.
 * The class also involves some rights that common librarians can use : delete user
 */
@Service
public class LibrarianService extends PersonService {
    @Autowired
    LibrarianRepository librarianRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BusinessHourRepository businessHourRepository;
    @Autowired
    LibrarySystemRepository librarySystemRepository;

    /**
     * when a library system is built, a head librarian should be created.
     */
    @Transactional
    public void initHeadLibrarian() {
        Librarian librarian = (Librarian) createPerson("HEAD", "LibraryAddress", "Librarian");
        librarian.setAddress("LibraryAddress");
        librarian.setIsHeadLibrarian(true);
        librarianRepository.save(librarian);
    }

    /**
     * when a library system is built, two librarian is hired automatically
     */
    @Transactional
    public void initLibrarian() {
        //create first librarian
        Librarian librarian = (Librarian) createPerson("Joe", "LibraryAddress1", "Librarian");
        librarian.setIsHeadLibrarian(false);
        librarianRepository.save(librarian);

        //create second librarian
        Librarian librarian1 = (Librarian) createPerson("Messi", "LibraryAddress1", "Librarian");
        librarian1.setIsHeadLibrarian(false);
        librarianRepository.save(librarian1);
    }

    /**
     * head librarian can create librarian with the legal inputs
     *
     * @param headLibrarianId head librarian id
     * @param name            new librarian name
     * @param address         new librarian address
     * @param isHeadLibrarian whether the new librarian is head librarian
     * @return new librarian object with the input information
     */
    @Transactional
    public Librarian createLibrarian(int headLibrarianId, String name, String address, boolean isHeadLibrarian) {

        Librarian headLibrarian = librarianRepository.findLibrarianById(headLibrarianId);
        if (headLibrarian != null) {
            // if the input id is not a head librarian, but a librarian
            if (!headLibrarian.getIsHeadLibrarian()) {
                throw new IllegalArgumentException("Librarian does not have authority to hire new librarian!");
            }
        } else {
            throw new IllegalArgumentException("input librarian does not exist!");
        }

        //check whether the system has the had librarian
        boolean isHeadLibrarianExist = false;
        for (Person person : getAllLibrarian()) {
            Librarian librarian = (Librarian) person;
            if (librarian.getIsHeadLibrarian()) {
                isHeadLibrarianExist = true;
                break;
            }
        }

        //if the head librarian already exist, cannot create a head librarian
        if (isHeadLibrarian && isHeadLibrarianExist) {
            throw new IllegalArgumentException("Head librarian already exists!");
        }

        Librarian librarian = (Librarian) createPerson(name, address, "Librarian");
        librarian.setIsHeadLibrarian(isHeadLibrarian);
        librarianRepository.save(librarian);

        return librarian;
    }

    /**
     * get all librarians in the library
     *
     * @return a list of librarians that work in the library.
     */
    @Transactional
    public List<Person> getAllLibrarian() {
        List<Person> tempList = HelperMethods.toList(librarianRepository.findAll());
        List<Person> librarianList = new ArrayList<>();
        for (Person p : tempList) {
            if (p instanceof Librarian) {
                librarianList.add(p);
            }
        }
        return librarianList;
    }

    /**
     * get librarian by id
     *
     * @param id librarian id
     * @return librarian object with the input id, otherwise null
     */
    @Transactional
    public Librarian getLibrarianById(int id) {
        return librarianRepository.findLibrarianById(id);
    }

    /**
     * head librarian can assign the business hour for librarians
     *
     * @param headLibrarianId head librarian id
     * @param librarianId librarian id
     * @param dayOfWeek       ex. Monday, Tuesday
     * @return the librarian with the update business hour
     */
    @Transactional
    public Librarian assignBusinessHour(int headLibrarianId, int librarianId, int dayOfWeek) {
        String error = "";

        //check the input
        Librarian librarian = librarianRepository.findLibrarianById(librarianId);
        if (librarian == null) error = error + ("Librarian does not exist!");
        Librarian headLibrarian = librarianRepository.findLibrarianById(headLibrarianId);
        if (headLibrarian != null) {
            if (!headLibrarian.getIsHeadLibrarian())
                error = error + "Librarian does not have authority to assign business hour!";
        } else {
            error = error + "Head librarian does not exist!";
        }
        BusinessHour businessHour = businessHourRepository.findBusinessHourById(dayOfWeek);
        if (businessHour == null) {
            error = error + ("business hour does not exist!");
        }
        if (librarian != null) {
            for (BusinessHour businessHour1 : librarian.getBusinessHours()) {
                if (businessHour1.getId() == businessHour.getId())
                    error += "Librarian already has this business hour!";
            }
        }

        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        librarian.getBusinessHours().add(businessHour);
        librarianRepository.save(librarian);
        return librarian;
    }

    /**
     * the head librarian removes the business hour at a day for a librarian
     *
     * @param headLibrarianId head librarian id
     * @param librarianId librarian id
     * @param dayOfWeek day of week
     * @return the librarian object with the update business hour
     */
    @Transactional
    public Librarian unAssignBusinessHour(int headLibrarianId, int librarianId, int dayOfWeek) {
        String error = "";
        Librarian librarian = librarianRepository.findLibrarianById(librarianId);
        if (librarian == null) error = error + ("Librarian does not exist!");

        Librarian headLibrarian = librarianRepository.findLibrarianById(headLibrarianId);
        if (headLibrarian != null) {
            if (!headLibrarian.getIsHeadLibrarian())
                error = error + "Librarian does not have authority to unassign business hour!";
        } else {
            error = error + "Head librarian does not exist!";
        }
        // check business hour existence
        BusinessHour businessHour = businessHourRepository.findBusinessHourById(dayOfWeek);
        if (businessHour == null) error = error + ("business hour does not exist!");
        boolean flag = false;
        if (librarian != null && businessHour != null) {
            for (BusinessHour businessHour1 : librarian.getBusinessHours()) {
                if (businessHour1.getId() == businessHour.getId()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) error = error + "Librarian does not have this business hour!";
        }
        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        //unassign
        librarian.getBusinessHours().removeIf(businessHour1 -> businessHour1.getDayOfWeek().equals(businessHour.getDayOfWeek()));
        librarianRepository.save(librarian);
        return librarian;
    }

    /**
     * update the head librarian in the library.
     * Head librarian set the librarian with the given id to be the head librarian and degrade himself/herself to be the normal librarian.
     *
     * @param headLibrarianId head librarian id
     * @param librarianId     librarian id
     * @return the new head librarian
     */
    @Transactional
    public Librarian updateIsHeadLibrarian(int headLibrarianId, int librarianId) {
        String error = "";
        //check the input librarian and head librarian id
        Librarian librarian = librarianRepository.findLibrarianById(librarianId);
        if (librarian == null) error = error + "Librarian does not exist!";
        Librarian headLibrarian = librarianRepository.findLibrarianById(headLibrarianId);
        if (headLibrarian != null) {
            if (!headLibrarian.getIsHeadLibrarian()) error = error + "Librarian does not have authority to update!";
        } else {
            error = error + "Head librarian does not exist!";
        }
        if (headLibrarianId == librarianId)
            error = error + "There is no need to set a head librarian to be a head librarian";

        error = error.trim();
        if (error.length() > 0) throw new IllegalArgumentException(error);

        //update
        librarian.setIsHeadLibrarian(true);
        headLibrarian.setIsHeadLibrarian(false);
        librarianRepository.save(librarian);
        return librarian;
    }

    /**
     * head librarian fires librarian with the given id
     *
     * @param headLibrarianId
     * @param id              the id of the librarian that is going to be fired
     * @return the fired librarian
     */
    @Transactional
    public Librarian fireLibrarian(int headLibrarianId, int id) {
        String error = "";
        Librarian librarian = librarianRepository.findLibrarianById(id);
        if (librarian == null) {
            error = error + "Librarian does not exist!";
        }
        Librarian headLibrarian = librarianRepository.findLibrarianById(headLibrarianId);
        if (headLibrarian != null) {
            if (!headLibrarian.getIsHeadLibrarian()) {
                error = error + "Librarian does not have authority to fire!";
            }
        } else {
            error = error + "Head librarian does not exist!";
        }
        error = error.trim();
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //delete librarian
        librarianRepository.deleteById(id);
        return librarian;
    }


    /**
     * librarian can remove the membership of users
     *
     * @param librarianId librarian id
     * @param uid         user id
     */
    @Transactional
    public User deleteUser(int librarianId, int uid) {
        User user = userRepository.findUserById(uid);
        Librarian librarian = librarianRepository.findLibrarianById(librarianId);
        if (librarian == null) {
            throw new IllegalArgumentException("non-existing librarian, no authority to delete the user");
        }
        if (user == null) {
            throw new IllegalArgumentException("non-existing user");
        }

        //delete user
        userRepository.deleteById(uid);
        return user;
    }

}
