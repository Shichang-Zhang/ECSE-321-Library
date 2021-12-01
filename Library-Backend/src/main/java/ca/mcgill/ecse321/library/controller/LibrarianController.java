package ca.mcgill.ecse321.library.controller;

import ca.mcgill.ecse321.library.dto.*;
import ca.mcgill.ecse321.library.model.Librarian;
import ca.mcgill.ecse321.library.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains the controller methods to call perform librarian database operations using business service methods
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/librarians")
public class LibrarianController {
    @Autowired
    LibrarianService librarianService;

    /**
     * Get all librarians in the library
     * @return a list of data transfer objects of all librarians in the library
     */
    @GetMapping("/librarianList")
    public List<LibrarianDto> getAllLibrarian(){
        return librarianService.getAllLibrarian().stream().map(l->HelperMethods.convertToDto((Librarian)l)).collect(Collectors.toList());
    }

    /**
     * @param name name of the librarian
     * @param address librarian address
     * @param isHeadLibrarian whether the librarian will be the head librarian
     * @return data transfer objects of the create librarian object
     * @throws IllegalArgumentException
     */
    @PostMapping("/createLibrarian")
    public LibrarianDto createLibrarian(@RequestParam("headLibrarianId") int headLibrarianId, @RequestParam("name") String name, @RequestParam("address") String address, @RequestParam("isHead") boolean isHeadLibrarian)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.createLibrarian(headLibrarianId, name, address, isHeadLibrarian);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * get librarian by id
     * @param id librarian id
     * @return the data transfer object of the librarian instance with the input id
     * @throws IllegalArgumentException
     */
    @GetMapping("/getLibrarianById")
    public LibrarianDto getLibrarianById(@RequestParam("id") int id)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.getLibrarianById(id);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * add the working time of the librarian with the given id
     * @param headLibrarianId
     * @param librarianId
     * @param dayOfWeek
     * @return data transfer objects of the librarian object with added business hour
     * @throws IllegalArgumentException
     */
    @PutMapping("/assignBusinessHour")
    public LibrarianDto assignBusinessHour(@RequestParam("headLibrarianId") int headLibrarianId,@RequestParam("librarianId") int librarianId, @RequestParam("dayOfWeek")int dayOfWeek)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.assignBusinessHour(headLibrarianId,librarianId,dayOfWeek);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * remove the working time of the librarian with the given id
     * @param headLibrarianId
     * @param librarianId
     * @param dayOfWeek
     * @return data transfer objects of the librarian object with removed business hour
     * @throws IllegalArgumentException
     */
    @PutMapping("/unassignBusinessHour")
    public LibrarianDto unassignBusinessHour(@RequestParam("headLibrarianId") int headLibrarianId, @RequestParam("librarianId") int librarianId, @RequestParam("dayOfWeek")int dayOfWeek)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.unAssignBusinessHour(headLibrarianId, librarianId,dayOfWeek);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * update the head librarian in the system.
     * @param headLibrarianId
     * @param librarianId
     * @return data transfer objects of the librarian object with update isHeadLibrarian attribute
     * @throws IllegalArgumentException
     */
    @PutMapping("/updateIsHeadLibrarian")
    public LibrarianDto updateIsHeadLibrarian(@RequestParam("headLibrarianId") int headLibrarianId, @RequestParam("librarianId") int librarianId)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.updateIsHeadLibrarian(headLibrarianId,librarianId);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * a head librarian can fire the librarian with the given id
     * @param headLibrarianId
     * @param librarianId
     * @return data transfer objects of the delete librarian
     * @throws IllegalArgumentException
     */
    @DeleteMapping("/fireLibrarian")
    public LibrarianDto fireLibrarian(@RequestParam("headLibrarianId") int headLibrarianId, @RequestParam("librarianId") int librarianId)
            throws IllegalArgumentException{
        Librarian librarian= librarianService.fireLibrarian(headLibrarianId,librarianId);
        return HelperMethods.convertToDto(librarian);
    }

    /**
     * librarian can delete user if user id is given
     * @param lid librarian id
     * @param uid user id
     * @return true means delete successfully, otherwise false.
     * @throws IllegalArgumentException
     */
    @DeleteMapping("/deleteUser")
    public boolean deleteUserById(@RequestParam("lid") int lid,@RequestParam("uid") int uid) throws IllegalArgumentException {
        boolean flag=false;
        try{
            librarianService.deleteUser(lid,uid);
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

}
