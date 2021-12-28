package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.User;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * user repository.
 * containing methods that can interact with the user table in the database.
 * findUserById method will return a user object that has the input id
 */
@RepositoryRestResource(collectionResourceRel = "user_data", path = "user_data")
public interface UserRepository extends PersonRepository {
    User findUserById(int id);
}
