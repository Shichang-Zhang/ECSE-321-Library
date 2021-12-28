package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.Librarian;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * librarian repository extends the person repository which containing the methods that can interact with the person table.
 * containing methods that can interact with the librarian table in the database.
 * findLibrarianById method will return the librarian with the given id.
 */
@RepositoryRestResource(collectionResourceRel = "librarian_data", path = "librarian_data")
public interface LibrarianRepository extends PersonRepository {
    Librarian findLibrarianById(int id);
}
