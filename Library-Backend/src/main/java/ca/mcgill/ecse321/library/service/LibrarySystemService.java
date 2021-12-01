package ca.mcgill.ecse321.library.service;

import ca.mcgill.ecse321.library.HelperMethods;
import ca.mcgill.ecse321.library.dao.LibrarySystemRepository;
import ca.mcgill.ecse321.library.model.LibrarySystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * library system service methods to manipulate data in backend, used in controller.
 */
@Service
public class LibrarySystemService {
    @Autowired
    LibrarySystemRepository librarySystemRepository;

    /**
     * create the library system
     */
    @Transactional
    public void createLibrarySystem(){
        if (HelperMethods.toList(librarySystemRepository.findAll()).size()==0){
            LibrarySystem librarySystem = new LibrarySystem();
            librarySystemRepository.save(librarySystem);
        }
    }
}
