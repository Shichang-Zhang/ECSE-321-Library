package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.LibrarySystem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * library system repository
 * containing methods that can interact with the librarySystem table in the database
 */
@RepositoryRestResource(collectionResourceRel = "librarySystem_data", path = "librarySystem_data")
public interface LibrarySystemRepository extends CrudRepository<LibrarySystem, Integer> {
    LibrarySystem findLibrarySystemById(int id);
}
