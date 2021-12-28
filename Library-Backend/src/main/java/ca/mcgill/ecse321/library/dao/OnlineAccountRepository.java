package ca.mcgill.ecse321.library.dao;

import ca.mcgill.ecse321.library.model.OnlineAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * online account repository.
 * containing methods that can interact with the onlineAccount table in the database.
 * findOnlineAccountById method will return online account with the input id.
 */
@RepositoryRestResource(collectionResourceRel = "onlineAccount_data", path = "onlineAccount_data")
public interface OnlineAccountRepository extends CrudRepository<OnlineAccount, Integer> {
    OnlineAccount findOnlineAccountById(int userID);

    OnlineAccount findOnlineAccountByUsername(String username);
}